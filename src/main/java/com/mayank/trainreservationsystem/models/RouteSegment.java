package com.mayank.trainreservationsystem.models;

import com.mayank.trainreservationsystem.constants.Entities;
import com.mayank.trainreservationsystem.constants.Fields;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = Entities.ROUTE_SEGMENT)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteSegment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = Fields.SEGMENT_ORDER)
    private Long segmentOrder;

    @ManyToOne
    @JoinColumn(name = Fields.TRAIN_ID)
    private Train train;

    @ManyToOne
    @JoinColumn(name = Fields.STATION_ID)
    private TrainStation station;

    @OneToMany
    @JoinColumn(name = Fields.ROUTE_SEGMENT_ID, referencedColumnName = Fields.ID)
    private List<SeatAllocation> seatAllocations;
}
