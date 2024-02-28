package com.mayank.trainreservationsystem.repositories.impl;

import com.mayank.trainreservationsystem.models.RouteSegment;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RouteSegmentRepository {
    private static final Class<RouteSegment> KCLASS = RouteSegment.class;
    private final EntityManager entityManager;

    private static final String FETCH_ROUTES_QUERY = """
            SELECT rs
            FROM RouteSegment rs
            JOIN RouteSegment frs ON rs.routeId = frs.routeId
            JOIN RouteSegment trs ON rs.routeId = trs.routeId
            WHERE frs.station.id = :from_station
            AND frs.date = :date_to_book
            AND trs.station.id = :to_station
            AND frs.segmentOrder < trs.segmentOrder
            AND rs.segmentOrder < trs.segmentOrder
            AND rs.segmentOrder >= frs.segmentOrder
            """;

    public List<RouteSegment> getRouteSegments(long fromStation, long toStation, LocalDate dateToBook) {
        var query = entityManager.createQuery(FETCH_ROUTES_QUERY, KCLASS);

        query.setParameter("from_station", fromStation).setParameter("to_station", toStation)
                .setParameter("date_to_book", dateToBook);

        return Optional.ofNullable(query.getResultList()).orElseGet(ArrayList::new);
    }

    public RouteSegment findById(long routeSegmentId) {
        return entityManager.find(KCLASS, routeSegmentId);
    }
}
