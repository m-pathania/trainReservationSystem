package com.mayank.trainreservationsystem.requests;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketBookingRequest {
    @NotNull
    private Long from;
    @NotNull
    private Long to;
    @NotNull
    private BookingUserInfo userInfo;
    @NotNull
    private BigDecimal pricePaid;
    @Nullable
    private LocalDate bookingDate;
}
