package com.mayank.trainreservationsystem.repositories;

import com.mayank.trainreservationsystem.models.BookingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseBookingRepository extends JpaRepository<BookingInfo, Long> {
}
