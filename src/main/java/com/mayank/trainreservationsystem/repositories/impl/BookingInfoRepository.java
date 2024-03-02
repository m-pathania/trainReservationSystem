package com.mayank.trainreservationsystem.repositories.impl;

import com.mayank.trainreservationsystem.enums.BookingStatus;
import com.mayank.trainreservationsystem.models.BookingInfo;
import com.mayank.trainreservationsystem.repositories.BaseBookingRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookingInfoRepository {
    private static final Class<BookingInfo> KLASS = BookingInfo.class;
    private final EntityManager entityManager;
    private final BaseBookingRepository baseBookingRepository;

    private static final String UPDATE_BOOKING_STATUS = "UPDATE BookingInfo bi SET bi.status = :status WHERE bi.id = :booking_id";
    private static final String FETCH_BOOKINGS_FOR_USER_ID = "SELECT bi FROM BookingInfo bi WHERE bi.userInfo.id = :user_id";

    @Transactional
    public BookingInfo save(BookingInfo bookingInfo) {
        return baseBookingRepository.save(bookingInfo);
    }

    @Transactional
    public int updateBookingStatus(long bookingId, BookingStatus status) {
        var query = entityManager.createQuery(UPDATE_BOOKING_STATUS);
        query.setParameter("status", status);
        query.setParameter("booking_id", bookingId);

        return query.executeUpdate();
    }

    public List<BookingInfo> fetchBookingsForUserId(String userId) {
        var query = entityManager.createQuery(FETCH_BOOKINGS_FOR_USER_ID, BookingInfo.class);
        query.setParameter("user_id", userId);

        return Optional.ofNullable(query.getResultList()).orElseGet(ArrayList::new);
    }
}
