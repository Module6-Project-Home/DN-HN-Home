package com.codegym.repository.booking;

import com.codegym.model.entity.booking.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookingStatusRepository extends JpaRepository<BookingStatus, Long> {
}
