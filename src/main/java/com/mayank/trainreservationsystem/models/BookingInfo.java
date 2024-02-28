package com.mayank.trainreservationsystem.models;

import com.mayank.trainreservationsystem.constants.Entities;
import com.mayank.trainreservationsystem.constants.Fields;
import com.mayank.trainreservationsystem.enums.BookingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = Entities.BOOKING_INFO)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = Fields.AMOUNT)
    private BigDecimal amount;

    @Column(name = Fields.BOOKED_AT)
    private Instant bookedAt;

    @Column(name = Fields.STATUS)
    private BookingStatus status;

    @ManyToOne
    @JoinColumn(name = Fields.SEAT_ALLOCATION_ID)
    private SeatAllocation seatAllocation;

    @ManyToOne
    @JoinColumn(name = Fields.USER_ID)
    private UserInfo userInfo;
}
