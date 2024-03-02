package com.mayank.trainreservationsystem.responses;

import com.mayank.trainreservationsystem.enums.TrainSection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class UserBookingsResponse {
    private String userEmail;
    private List<TrainBookingResponse> bookings;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrainBookingResponse {
        private Long trainId;
        private String trainName;
        private Long seatNumber;
        private TrainSection trainSection;
        private Long bookingId;
        private BigDecimal amountPaid;
    }
}
