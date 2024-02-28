package com.mayank.trainreservationsystem.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RouteSegmentRepositoryTest {
    @Autowired
    private RouteSegmentRepository routeSegmentRepository;

    @Test
    void testFetch() {
        long fromStation = 6;
        long toStation = 12;
        LocalDate date = LocalDate.of(2024, Month.FEBRUARY, 1);

        var routeSegments = routeSegmentRepository.getRouteSegments(fromStation, toStation, date);
        Assertions.assertFalse(routeSegments.isEmpty());
        routeSegments.forEach(routeSegment -> {
            if (List.of(4L, 5L, 6L, 7L, 8L).contains(routeSegment.getId())) {
                Assertions.assertEquals(1, routeSegment.getTrain().getId());
            } else if (List.of(13L, 14L, 15L).contains(routeSegment.getId())) {
                Assertions.assertEquals(3, routeSegment.getTrain().getId());
            } else {
                Assertions.fail();
            }
        });
    }

}