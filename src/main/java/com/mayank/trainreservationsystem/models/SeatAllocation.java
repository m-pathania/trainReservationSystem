package com.mayank.trainreservationsystem.models;

import com.mayank.trainreservationsystem.constants.Entities;
import com.mayank.trainreservationsystem.constants.Fields;
import com.mayank.trainreservationsystem.enums.TrainSection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


import jakarta.persistence.Index;
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
@Table(name = Entities.SEAT_ALLOCATION, indexes = {
        @Index(name = "seat_allocation_unique_index", columnList = "number, section, route_segment_id", unique = true)
})
public class SeatAllocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = Fields.NUMBER)
    private Long number;

    @Column(name = Fields.SECTION)
    @Enumerated(EnumType.STRING)
    private TrainSection section;

    @ManyToOne
    @JoinColumn(name = Fields.ROUTE_SEGMENT_ID)
    private RouteSegment routeSegment;

    @ManyToOne
    @JoinColumn(name = Fields.BOOKING_ID)
    private BookingInfo bookingInfo;
}
