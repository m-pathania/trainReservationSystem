package com.mayank.trainreservationsystem.repositories;

import com.mayank.trainreservationsystem.dtos.TrainSeat;
import com.mayank.trainreservationsystem.models.SeatAllocation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SeatAllocationRepository {
    private static final Class<SeatAllocation> KLASS = SeatAllocation.class;
    private final EntityManager entityManager;

    private static final String FIND_BY_ROUTE_SEGMENT_IDS_QUERY =
            "SELECT sa FROM SeatAllocation sa where sa.routeSegment.id in :route_segment_ids";

    @Transactional
    public void persistAll(List<SeatAllocation> seatAllocationList) {
        seatAllocationList.parallelStream().forEach(this::persist);
    }

    private void persist(SeatAllocation seatAllocation) {
        entityManager.persist(seatAllocation);
    }

    public List<SeatAllocation> findAllByRouteSegmentIds(List<Long> routeSegmentIds) {
        TypedQuery<SeatAllocation> query = entityManager.createQuery(FIND_BY_ROUTE_SEGMENT_IDS_QUERY, KLASS);
        query.setParameter("route_segment_ids", routeSegmentIds);

        return Optional.ofNullable(query.getResultList()).orElseGet(ArrayList::new);
    }
}
