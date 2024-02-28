package com.mayank.trainreservationsystem.services;

import com.mayank.trainreservationsystem.requests.TicketBookingRequest;
import com.mayank.trainreservationsystem.responses.TicketBookingResponse;

public interface TrainReservationService {
    TicketBookingResponse bookTicket(TicketBookingRequest request);
}
