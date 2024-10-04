package com.example.md6projecthndn.repository.booking;


import com.example.md6projecthndn.model.entity.booking.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookingStatusRepository extends JpaRepository<BookingStatus, Long> {
}
