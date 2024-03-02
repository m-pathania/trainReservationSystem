package com.mayank.trainreservationsystem.services;

import com.mayank.trainreservationsystem.requests.TicketBookingRequest;
import com.mayank.trainreservationsystem.responses.TicketBookingResponse;
import com.mayank.trainreservationsystem.responses.UserBookingsResponse;

public interface TrainReservationService {
    TicketBookingResponse bookTicket(TicketBookingRequest request);

    UserBookingsResponse getBookingsForUser(String userEmail);
}
