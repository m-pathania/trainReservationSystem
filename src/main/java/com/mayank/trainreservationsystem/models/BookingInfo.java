package com.mayank.trainreservationsystem.models;

import com.mayank.trainreservationsystem.constants.Entities;
import com.mayank.trainreservationsystem.constants.Fields;
import com.mayank.trainreservationsystem.enums.BookingStatus;
import com.mayank.trainreservationsystem.enums.TrainSection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = Entities.BOOKING_INFO)
public class BookingInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = Fields.AMOUNT)
    private BigDecimal amount;

    @Column(name = Fields.SEAT_NUMBER)
    private Long seatNumber;

    @Column(name = Fields.ROUTE_ID)
    private Long routeId;

    @Column(name = Fields.SECTION)
    private TrainSection section;

    @Column(name = Fields.STATUS)
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @ManyToOne
    @JoinColumn(name = Fields.USER_ID)
    private UserInfo userInfo;

    @ManyToOne
    @JoinColumn(name = Fields.TRAIN_ID)
    private Train train;

    @ManyToOne
    @JoinColumn(name = Fields.FROM_STATION_ID)
    private TrainStation fromTrainStation;

    @ManyToOne
    @JoinColumn(name = Fields.TO_STATION_ID)
    private TrainStation toTrainStation;
}
