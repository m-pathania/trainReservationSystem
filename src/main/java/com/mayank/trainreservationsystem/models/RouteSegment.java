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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = Entities.ROUTE_SEGMENT)
public class RouteSegment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = Fields.SEGMENT_ORDER)
    private Long segmentOrder;

    @Column(name = Fields.DATE)
    private LocalDate date;

    @Column(name = Fields.ROUTE_ID)
    private Long routeId;

    @ManyToOne
    @JoinColumn(name = Fields.TRAIN_ID)
    private Train train;

    @ManyToOne
    @JoinColumn(name = Fields.STATION_ID)
    private TrainStation station;
}
