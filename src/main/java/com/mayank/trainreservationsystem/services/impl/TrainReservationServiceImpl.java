package com.mayank.trainreservationsystem.services.impl;

import com.mayank.trainreservationsystem.dtos.SeatBookingResult;
import com.mayank.trainreservationsystem.dtos.TrainSeat;
import com.mayank.trainreservationsystem.enums.BookingStatus;
import com.mayank.trainreservationsystem.enums.Status;
import com.mayank.trainreservationsystem.models.BookingInfo;
import com.mayank.trainreservationsystem.models.RouteSegment;
import com.mayank.trainreservationsystem.models.SeatAllocation;
import com.mayank.trainreservationsystem.models.Train;
import com.mayank.trainreservationsystem.models.TrainStation;
import com.mayank.trainreservationsystem.models.UserInfo;
import com.mayank.trainreservationsystem.repositories.impl.BookingInfoRepository;
import com.mayank.trainreservationsystem.repositories.impl.RouteSegmentRepository;
import com.mayank.trainreservationsystem.repositories.impl.SeatAllocationRepository;
import com.mayank.trainreservationsystem.repositories.impl.UserInfoRepository;
import com.mayank.trainreservationsystem.requests.TicketBookingRequest;
import com.mayank.trainreservationsystem.responses.TicketBookingResponse;
import com.mayank.trainreservationsystem.responses.UserBookingsResponse;
import com.mayank.trainreservationsystem.services.TrainReservationService;
import com.mayank.trainreservationsystem.utils.CommonUtils;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainReservationServiceImpl implements TrainReservationService {
    private final RouteSegmentRepository routeSegmentRepository;
    private final SeatAllocationRepository seatAllocationRepository;
    private final UserInfoRepository userInfoRepository;
    private final BookingInfoRepository bookingInfoRepository;

    @Override
    public TicketBookingResponse bookTicket(TicketBookingRequest request) {
        var booking = initiateBooking(request);
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
                        .map(availableSeat -> bookSeatForAllRouteSegments(availableSeat, routeSegments, booking))
                        .filter(seatBookingResult -> Status.COMPLETED.equals(seatBookingResult.getStatus()))
                        .findFirst();

                if (seatBookingResultOptional.isPresent()) {
                    seatBookingResultOptional.ifPresent(seatBookingResult -> {
                        responseBuilder.status(seatBookingResult.getStatus());

                        TrainSeat trainSeat = seatBookingResult.getTrainSeat();
                        booking.setStatus(BookingStatus.BOOKED);
                        booking.setSeatNumber(trainSeat.getNumber());
                        booking.setSection(trainSeat.getSection());
                        booking.setTrain(train);
                        TrainStation fromTrainStation = TrainStation.builder().id(request.getFrom()).build();
                        TrainStation toTrainStation = TrainStation.builder().id(request.getTo()).build();
                        booking.setFromTrainStation(fromTrainStation);
                        booking.setToTrainStation(toTrainStation);

                        bookingInfoRepository.save(booking);
                    });

                    return responseBuilder.build();
                } else {
                    updateResponseForNoSeats(responseBuilder);
                    booking.setStatus(BookingStatus.FAILED);
                    bookingInfoRepository.save(booking);
                }
            }
        } else {
            updateResponseForNoSeats(responseBuilder);
            booking.setStatus(BookingStatus.FAILED);
            bookingInfoRepository.updateBookingStatus(booking.getId(), BookingStatus.FAILED);
        }

        return responseBuilder.build();
    }

    @Override
    public UserBookingsResponse getBookingsForUser(String userEmail) {
        try {
            UserInfo userInfo = userInfoRepository.fetchUserInfo(userEmail);
            List<BookingInfo> seatAllocations = bookingInfoRepository.fetchBookingsForUserId(userInfo.getId());
            var trainBookingResponses = mapToTrainBookingResponse(seatAllocations);

            return UserBookingsResponse.builder()
                    .userEmail(userInfo.getEmailId())
                    .bookings(trainBookingResponses)
                    .build();
        } catch (EmptyResultDataAccessException | NoResultException ignored) {
            return UserBookingsResponse.builder()
                    .userEmail(userEmail)
                    .bookings(new ArrayList<>())
                    .build();
        }
    }

    private List<UserBookingsResponse.TrainBookingResponse> mapToTrainBookingResponse(List<BookingInfo> bookingInfos) {
        return bookingInfos.stream()
                .map(bookingInfo -> {
                    final Train train = bookingInfo.getTrain();
                    final TrainStation fromStation = bookingInfo.getFromTrainStation();
                    final TrainStation toStation = bookingInfo.getToTrainStation();

                    return UserBookingsResponse.TrainBookingResponse.builder()
                            .routeId(bookingInfo.getRouteId())
                            .trainId(train.getId())
                            .trainName(train.getName())
                            .trainSection(bookingInfo.getSection())
                            .seatNumber(bookingInfo.getSeatNumber())
                            .bookingId(bookingInfo.getId())
                            .amountPaid(bookingInfo.getAmount())
                            .fromStationId(fromStation.getId())
                            .fromStationName(fromStation.getName())
                            .toStationId(toStation.getId())
                            .toStationName(toStation.getName())
                            .build();
                }).toList();
    }

    private BookingInfo initiateBooking(TicketBookingRequest request) {
        UserInfo userInfo = userInfoRepository.createOrFetchUser(request.getUserInfo());
        BookingInfo bookingInfo = BookingInfo.builder().amount(request.getPricePaid()).status(BookingStatus.INITIATED)
                .userInfo(userInfo).build();

        return bookingInfoRepository.save(bookingInfo);
    }

    private void updateResponseForNoSeats(TicketBookingResponse.TicketBookingResponseBuilder responseBuilder) {
        responseBuilder.status(Status.FAILED);
        responseBuilder.errorMessage("No seats available.");
    }

    private SeatBookingResult bookSeatForAllRouteSegments(TrainSeat availableSeat, List<RouteSegment> routeSegments,
                                                          BookingInfo booking) {
        var responseBuilder = SeatBookingResult.builder().trainSeat(availableSeat).status(Status.FAILED);
        try {
            var seatAllocations = routeSegments.stream()
                    .map(routeSegment -> {
                        responseBuilder.train(routeSegment.getTrain());
                        responseBuilder.routeId(routeSegment.getRouteId());

                        return SeatAllocation.builder().number(availableSeat.getNumber())
                                .section(availableSeat.getSection()).routeSegment(routeSegment).bookingInfo(booking)
                                .build();
                    })
                    .toList();
            seatAllocationRepository.persistAll(seatAllocations);

            return responseBuilder.status(Status.COMPLETED).build();
        } catch (DataIntegrityViolationException e) {
            log.error("Exception occurred while booking seat : {};", availableSeat, e);
        }

        return responseBuilder.build();
    }

    private List<RouteSegment> getRouteSegments(TicketBookingRequest request) {
        final long fromStation = request.getFrom();
        final long toStation = request.getTo();
        final LocalDate localDate = request.getBookingDate();

        return routeSegmentRepository.getRouteSegments(fromStation, toStation, localDate);
    }
}
