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

@Entity(name = Entities.TRAIN_STATION)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = Fields.NAME)
    private String name;
}
