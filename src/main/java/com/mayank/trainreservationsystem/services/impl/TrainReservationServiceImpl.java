package com.mayank.trainreservationsystem.services.impl;

import com.mayank.trainreservationsystem.dtos.SeatBookingResult;
import com.mayank.trainreservationsystem.dtos.TrainSeat;
import com.mayank.trainreservationsystem.enums.Status;
import com.mayank.trainreservationsystem.models.RouteSegment;
import com.mayank.trainreservationsystem.models.SeatAllocation;
import com.mayank.trainreservationsystem.models.Train;
import com.mayank.trainreservationsystem.repositories.RouteSegmentRepository;
import com.mayank.trainreservationsystem.repositories.SeatAllocationRepository;
import com.mayank.trainreservationsystem.requests.TicketBookingRequest;
import com.mayank.trainreservationsystem.responses.TicketBookingResponse;
import com.mayank.trainreservationsystem.services.TrainReservationService;
import com.mayank.trainreservationsystem.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainReservationServiceImpl implements TrainReservationService {
    private final RouteSegmentRepository routeSegmentRepository;
    private final SeatAllocationRepository seatAllocationRepository;

    @Override
    public TicketBookingResponse bookTicket(TicketBookingRequest request) {
        TicketBookingResponse.TicketBookingResponseBuilder responseBuilder = TicketBookingResponse.builder();
        List<RouteSegment> routeSegmentList = getRouteSegments(request);

        if (!CollectionUtils.isEmpty(routeSegmentList)) {
            var groupedRouteSegments = routeSegmentList.stream()
                    .collect(Collectors.groupingBy(routeSegment -> routeSegment.getTrain().getId()));
            for (Map.Entry<Long, List<RouteSegment>> entry : groupedRouteSegments.entrySet()) {
                List<RouteSegment> routeSegments = entry.getValue();
                Train train = routeSegments.iterator().next().getTrain();
                List<TrainSeat> applicableSeats = train.getApplicableSeats();
                List<Long> routeSegmentIds = routeSegments.stream().map(RouteSegment::getId).toList();
                List<TrainSeat> allocatedSeats = seatAllocationRepository.findAllByRouteSegmentIds(routeSegmentIds)
                        .stream().map(TrainSeat::fromSeatAllocation).toList();
                List<TrainSeat> availableSeats = CommonUtils.getAvailableSeats(applicableSeats, allocatedSeats);
                var seatBookingResultOptional = availableSeats.stream()
                        .map(availableSeat -> bookSeatForAllRouteSegments(availableSeat, routeSegments))
                        .filter(seatBookingResult -> Status.COMPLETED.equals(seatBookingResult.getStatus()))
                        .findFirst();
                if (seatBookingResultOptional.isPresent()) {
                    seatBookingResultOptional.ifPresent(seatBookingResult -> responseBuilder.status(seatBookingResult.getStatus()));
                } else {
                    updateResponseForNoSeats(responseBuilder);
                }
            }
        } else {
            updateResponseForNoSeats(responseBuilder);
        }

        return responseBuilder.build();
    }

    private void updateResponseForNoSeats(TicketBookingResponse.TicketBookingResponseBuilder responseBuilder) {
        responseBuilder.status(Status.FAILED);
        responseBuilder.errorMessage("No seats available.");
    }

    private SeatBookingResult bookSeatForAllRouteSegments(TrainSeat availableSeat, List<RouteSegment> routeSegments) {
        try {
            var seatAllocations = routeSegments.stream().map(routeSegment -> SeatAllocation.builder()
                            .number(availableSeat.getNumber())
                            .section(availableSeat.getSection())
                            .routeSegment(routeSegment)
                            .build())
                    .toList();
            seatAllocationRepository.persistAll(seatAllocations);

            return SeatBookingResult.builder().status(Status.COMPLETED).build();
        } catch (DataIntegrityViolationException e) {
            log.error("Exception occurred while booking seat : {};", availableSeat, e);
            return SeatBookingResult.builder().status(Status.FAILED).build();
        }
    }

    private List<RouteSegment> getRouteSegments(TicketBookingRequest request) {
        final long fromStation = request.getFrom();
        final long toStation = request.getTo();
        final LocalDate localDate = request.getBookingDate();

        return routeSegmentRepository.getRouteSegments(fromStation, toStation, localDate);
    }
}
