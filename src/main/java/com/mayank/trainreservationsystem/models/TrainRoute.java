package com.mayank.trainreservationsystem.models;

import com.mayank.trainreservationsystem.constants.Entities;
import com.mayank.trainreservationsystem.constants.Fields;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = Entities.TRAIN_ROUTE)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = Fields.TRAIN_ID)
    private Long trainId;

    @Column(name = Fields.STATION_ID)
    private Long stationId;

    @Column(name = Fields.ORDER)
    private Long order;
}
