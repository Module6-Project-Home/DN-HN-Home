package com.example.md6projecthndn.service.booking.booking;


import com.example.md6projecthndn.model.entity.booking.Booking;
import com.example.md6projecthndn.service.IGenerateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface IBookingService extends IGenerateService<Booking> {
    public void updatePropertyStatusIfCheckOutDatePassed();
    List<Booking> findOverlappingBookings(Long propertyId, LocalDate startDate, LocalDate endDate);

    Page<Booking> findBookingByGuestId(Long guestId, Pageable pageable);
    Page<Booking> findBookingByOwnerId(Long ownerId, Pageable pageable);

    Page<Booking> findBookingByOwnerIdByPropertyId(Long ownerId, Long propertyId, Pageable pageable);

}
