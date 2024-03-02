package com.mayank.trainreservationsystem.dtos;

import com.mayank.trainreservationsystem.enums.Status;
import com.mayank.trainreservationsystem.models.SeatAllocation;
import com.mayank.trainreservationsystem.models.Train;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatBookingResult {
    private Status status;
    private Long routeId;
    private TrainSeat trainSeat;
    private Train train;
}
