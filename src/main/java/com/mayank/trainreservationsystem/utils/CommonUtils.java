package com.mayank.trainreservationsystem.utils;

import com.mayank.trainreservationsystem.dtos.TrainSeat;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtils {
    public static List<TrainSeat> getAvailableSeats(List<TrainSeat> applicableSeats, List<TrainSeat> allocatedSeats) {
        if (CollectionUtils.isEmpty(applicableSeats)) {
            return Collections.emptyList();
        }
        if (CollectionUtils.isEmpty(allocatedSeats)) {
            return applicableSeats;
        }

        var availableSeats = new ArrayList<>(applicableSeats);
        availableSeats.removeAll(allocatedSeats);

        return Collections.unmodifiableList(availableSeats);
    }
}
