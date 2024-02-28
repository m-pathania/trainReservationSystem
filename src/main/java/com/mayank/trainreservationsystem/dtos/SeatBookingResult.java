package com.mayank.trainreservationsystem.dtos;

import com.mayank.trainreservationsystem.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatBookingResult {
    private Status status;
}
