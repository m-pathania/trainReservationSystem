package com.mayank.trainreservationsystem.controllers;

import com.mayank.trainreservationsystem.requests.TicketBookingRequest;
import com.mayank.trainreservationsystem.responses.TicketBookingResponse;
import com.mayank.trainreservationsystem.services.TrainReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Objects;

@Validated
@RestController
@RequestMapping(path = "/v1")
@RequiredArgsConstructor
public class TrainReservationController {
    private final TrainReservationService trainReservationService;

    @PostMapping("/book_ticket")
    public TicketBookingResponse bookTicket(@RequestBody @Valid TicketBookingRequest request) {
        if (Objects.isNull(request.getBookingDate())) {
            request.setBookingDate(LocalDate.now());
        }

        return trainReservationService.bookTicket(request);
    }
}
