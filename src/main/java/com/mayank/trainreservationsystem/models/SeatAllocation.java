package com.mayank.trainreservationsystem.models;

import com.mayank.trainreservationsystem.constants.Entities;
import com.mayank.trainreservationsystem.constants.Fields;
import com.mayank.trainreservationsystem.enums.TrainSection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = Entities.SEAT_ALLOCATION)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatAllocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = Fields.NAME)
    private Long name;

    @Column(name = Fields.SECTION_NAME)
    private TrainSection section;

    @Column(name = Fields.DATE)
    private LocalDate date;

    @Column(name = Fields.IS_BOOKED)
    private Boolean isBooked;

    @ManyToOne
    @JoinColumn(name = Fields.ROUTE_SEGMENT_ID)
    private RouteSegment routeSegment;
}
