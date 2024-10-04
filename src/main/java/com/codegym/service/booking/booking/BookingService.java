package com.codegym.service.booking.booking;

import com.codegym.model.entity.booking.Booking;
import com.codegym.model.entity.booking.Status;
import com.codegym.model.entity.property.Property;
import com.codegym.repository.booking.IBookingRepository;
import com.codegym.service.booking.status.IStatusService;
import com.codegym.service.property.property.IPropertyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Service
public class BookingService implements IBookingService {
    private final IBookingRepository bookingRepository;
    private final IPropertyService propertyService;
    private final IStatusService statusService;

    public BookingService(IBookingRepository bookingRepository, IPropertyService propertyService, IStatusService statusService) {
        this.bookingRepository = bookingRepository;
        this.propertyService = propertyService;
        this.statusService = statusService;
    }


    @Override
    public Iterable<Booking> findAll() {
        return bookingRepository.findAll();
    }

    @Override
    public void save(Booking booking) {
    bookingRepository.save(booking);
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
    bookingRepository.deleteById(id);
    }

    @Override
    public void updatePropertyStatusIfCheckOutDatePassed() {
        // Lấy danh sách các booking có ngày trả phòng nhỏ hơn hoặc bằng ngày hiện tại
        List<Booking> bookings = bookingRepository.findByCheckOutDateBefore(LocalDate.now().plusDays(1));

        // Lặp qua các booking và cập nhật trạng thái property
        for (Booking booking : bookings) {
            Property property = booking.getProperty();
            Status availableStatus = statusService.findById(1L).orElseThrow(() -> new RuntimeException("Status not found"));

            property.setStatus(availableStatus);
            propertyService.save(property);
        }
    }


    @Override
    public List<Booking> findOverlappingBookings(Long propertyId, LocalDate startDate, LocalDate endDate) {
        return bookingRepository.findOverlappingBookings(propertyId, startDate, endDate);
    }


    @Override
    public Page<Booking> findBookingByOwnerId(Long ownerId, Pageable pageable) {
        return bookingRepository.findBookingByOwnerId(ownerId,pageable);

    }

    @Override
    public Page<Booking> findBookingByGuestId(Long guestId, Pageable pageable) {
        return bookingRepository.findBookingByGuestId(guestId,pageable);
    }

    @Override
    public Page<Booking> findBookingByOwnerIdByPropertyId(Long ownerId, Long propertyId, Pageable pageable) {
        return bookingRepository.findBookingByOwnerIdByPropertyId(ownerId,propertyId,pageable);
    }
}
