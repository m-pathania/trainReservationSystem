package com.mayank.trainreservationsystem.models;

import com.mayank.trainreservationsystem.constants.Entities;
import com.mayank.trainreservationsystem.constants.Fields;
import com.mayank.trainreservationsystem.enums.TrainSection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity(name = Entities.TRAIN_SEAT)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = Fields.ROUTE_ID)
    private Long routeId;

    @Column(name = Fields.NAME)
    private Long name;

    @Column(name = Fields.SECTION_NAME)
    private TrainSection section;

    @Column(name = Fields.DATE)
    private LocalDate date;

    @Column(name = Fields.IS_BOOKED)
    private Boolean isBooked;
}
