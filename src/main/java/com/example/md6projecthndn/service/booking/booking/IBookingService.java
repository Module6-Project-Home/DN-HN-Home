package com.example.md6projecthndn.service.booking.booking;


import com.example.md6projecthndn.model.dto.BookingByHostDTO;
import com.example.md6projecthndn.model.dto.BookingByUserDTO;
import com.example.md6projecthndn.model.entity.booking.Booking;
import com.example.md6projecthndn.service.IGenerateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IBookingService extends IGenerateService<Booking> {
    void updatePropertyStatusIfCheckOutDatePassed();

    List<Booking> findOverlappingBookings(Long propertyId, LocalDate startDate, LocalDate endDate);

    Page<Booking> findBookingByGuestId(Long guestId, Pageable pageable);

    Page<Booking> findBookingByOwnerId(Long ownerId, Pageable pageable);

    Page<Booking> findBookingByOwnerIdByPropertyId(Long ownerId, Long propertyId, Pageable pageable);

    List<BookingByUserDTO> bookingByUser(@Param("userId") Long userId);




}
