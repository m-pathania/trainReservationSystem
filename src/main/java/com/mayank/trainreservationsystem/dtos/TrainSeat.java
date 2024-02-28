package com.mayank.trainreservationsystem.dtos;

import com.mayank.trainreservationsystem.enums.TrainSection;
import com.mayank.trainreservationsystem.models.SeatAllocation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainSeat {
    private Long number;
    private TrainSection section;

    public static TrainSeat fromSeatAllocation(SeatAllocation seatAllocation) {
        return TrainSeat.builder().number(seatAllocation.getNumber()).section(seatAllocation.getSection()).build();
    }
}
