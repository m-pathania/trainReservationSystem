package com.mayank.trainreservationsystem.models;

import com.mayank.trainreservationsystem.constants.Entities;
import com.mayank.trainreservationsystem.constants.Fields;
import com.mayank.trainreservationsystem.dtos.TrainSeat;
import com.mayank.trainreservationsystem.enums.TrainSection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = Entities.TRAIN)
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = Fields.NAME)
    private String name;

    @Column(name = Fields.SECTION_SEAT_COUNT)
    private Long sectionSeatCount;

    public List<TrainSeat> getApplicableSeats() {
        if (Objects.isNull(sectionSeatCount)) {
            return Collections.emptyList();
        }

        return Arrays.stream(TrainSection.values())
                .flatMap(section -> LongStream.range(0, sectionSeatCount).mapToObj(seatNumber -> TrainSeat.builder()
                        .number(seatNumber).section(section).build()))
                .toList();
    }
}
