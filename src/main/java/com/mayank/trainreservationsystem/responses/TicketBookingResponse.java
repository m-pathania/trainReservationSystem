package com.mayank.trainreservationsystem.responses;

import com.mayank.trainreservationsystem.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketBookingResponse {
    private Status status;
    private String errorMessage;
}
