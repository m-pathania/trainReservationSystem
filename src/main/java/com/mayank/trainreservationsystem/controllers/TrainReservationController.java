package com.mayank.trainreservationsystem.controllers;

import com.mayank.trainreservationsystem.enums.TrainSection;
import com.mayank.trainreservationsystem.requests.TicketBookingRequest;
import com.mayank.trainreservationsystem.requests.UserSeatModificationRequest;
import com.mayank.trainreservationsystem.responses.TicketBookingResponse;
import com.mayank.trainreservationsystem.responses.UserBookingsResponse;
import com.mayank.trainreservationsystem.services.TrainReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<TicketBookingResponse> bookTicket(@RequestBody @Valid TicketBookingRequest request) {
        if (Objects.isNull(request.getBookingDate())) {
            request.setBookingDate(LocalDate.now());
        }

        return ResponseEntity.ok(trainReservationService.bookTicket(request));
    }

    @GetMapping("/user_bookings")
    public ResponseEntity<UserBookingsResponse> getBookingsForUser(@RequestHeader("x-trs-user-email") String userEmail) {
        return ResponseEntity.ok(trainReservationService.getBookingsForUser(userEmail));
    }

    @GetMapping("/section_info")
    public ResponseEntity<?> getBookingsFroTrainSection(@RequestParam("train_id") String trainId, @RequestParam("section_name") TrainSection section) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove_user_from_train")
    public ResponseEntity<?> remove_user_from_train(@RequestHeader("x-trs-user-email") String userEmail, @RequestParam("train_id") String trainId) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/modify_user_seat")
    public ResponseEntity<?> modify_user_seat(UserSeatModificationRequest request) {
        return ResponseEntity.ok().build();
    }
}
