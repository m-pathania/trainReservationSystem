package com.mayank.trainreservationsystem.repositories;

import com.mayank.trainreservationsystem.enums.TrainSection;
import com.mayank.trainreservationsystem.models.RouteSegment;
import com.mayank.trainreservationsystem.models.SeatAllocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SeatAllocationRepositoryTest {
    @Autowired
    private SeatAllocationRepository seatAllocationRepository;
    @Autowired
    private RouteSegmentRepository routeSegmentRepository;

    @Test
    void testUniqueIndex() {
        long routeSegmentId = 1;
        RouteSegment routeSegment = routeSegmentRepository.findById(routeSegmentId);
        Assertions.assertNotNull(routeSegment);
        SeatAllocation seatAllocation = SeatAllocation.builder()
                .number(1L)
                .section(TrainSection.A)
                .routeSegment(routeSegment)
                .build();

        seatAllocationRepository.persistAll(Collections.singletonList(seatAllocation));

        RouteSegment routeSegment1 = routeSegmentRepository.findById(routeSegmentId);

        Assertions.assertNotNull(routeSegment1);
        Assertions.assertNotNull(routeSegment1.getTrain());
        Assertions.assertNotNull(routeSegment1.getStation());

        List<SeatAllocation> seatAllocationList = seatAllocationRepository.findAllByRouteSegmentIds(Collections.singletonList(routeSegment1.getId()));
        Assertions.assertEquals(1, seatAllocationList.size());
        Assertions.assertEquals(seatAllocation, seatAllocationList.get(0));

        SeatAllocation seatAllocation1 = SeatAllocation.builder()
                .number(1L)
                .section(TrainSection.A)
                .routeSegment(routeSegment)
                .build();

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> seatAllocationRepository.persistAll(Collections.singletonList(seatAllocation1)));
    }

}
