package com.mayank.trainreservationsystem.repositories.impl;

import com.mayank.trainreservationsystem.enums.BookingStatus;
import com.mayank.trainreservationsystem.models.BookingInfo;
import com.mayank.trainreservationsystem.repositories.BaseBookingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookingInfoRepository {
    private static final Class<BookingInfo> KLASS = BookingInfo.class;
    private final EntityManager entityManager;
    private final BaseBookingRepository baseBookingRepository;

    private static final String UPDATE_BOOKING_STATUS =
            "UPDATE BookingInfo bi SET bi.status = :status WHERE bi.id = :booking_id";

    @Transactional
    public BookingInfo save(BookingInfo bookingInfo) {
        return baseBookingRepository.save(bookingInfo);
    }

    @Transactional
    public int updateBookingStatus(long bookingId, BookingStatus status) {
        TypedQuery<BookingInfo> query = entityManager.createQuery(UPDATE_BOOKING_STATUS, BookingInfo.class);
        query.setParameter("status", status);
        query.setParameter("booking_id", bookingId);

        return query.executeUpdate();
    }
}
