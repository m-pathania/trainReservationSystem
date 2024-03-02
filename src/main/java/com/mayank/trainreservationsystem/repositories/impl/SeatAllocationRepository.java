package com.mayank.trainreservationsystem.repositories.impl;

import com.mayank.trainreservationsystem.dtos.TrainSeat;
import com.mayank.trainreservationsystem.models.SeatAllocation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SeatAllocationRepository {
    private static final Class<SeatAllocation> KLASS = SeatAllocation.class;
    private final EntityManager entityManager;

    private static final String FIND_BY_ROUTE_SEGMENT_IDS_QUERY = "SELECT sa FROM SeatAllocation sa where sa.routeSegment.id in :route_segment_ids";
    private static final String FIND_BY_BOOKING_IDS = "SELECT sa FROM SeatAllocation sa WHERE sa.bookingInfo.id in :booking_ids";
    private static final String FIND_BY_USER_ID = """
            SELECT sa FROM SeatAllocation sa
            JOIN BookingInfo bi ON bi.id = sa.bookingInfo.id
            WHERE bi.userInfo.id = :user_id
            """;

    @Transactional
    public void persistAll(List<SeatAllocation> seatAllocationList) {
        seatAllocationList.forEach(this::persist);
    }

    private void persist(SeatAllocation seatAllocation) {
        entityManager.persist(seatAllocation);
    }

    public List<SeatAllocation> findAllByRouteSegmentIds(List<Long> routeSegmentIds) {
        TypedQuery<SeatAllocation> query = entityManager.createQuery(FIND_BY_ROUTE_SEGMENT_IDS_QUERY, KLASS);
        query.setParameter("route_segment_ids", routeSegmentIds);

        return Optional.ofNullable(query.getResultList()).orElseGet(ArrayList::new);
    }

    public List<SeatAllocation> findSeatAllocationForBookingId(Collection<Long> bookingIds) {
        var query = entityManager.createQuery(FIND_BY_BOOKING_IDS, SeatAllocation.class);
        query.setParameter("booking_ids", bookingIds);

        return Optional.ofNullable(query.getResultList()).orElseGet(ArrayList::new);
    }

    public List<SeatAllocation> findSeatAllocationsForUser(Long userId) {
        var query = entityManager.createQuery(FIND_BY_USER_ID, SeatAllocation.class);
        query.setParameter("user_id", userId);

        return Optional.ofNullable(query.getResultList()).orElseGet(ArrayList::new);
    }
}
