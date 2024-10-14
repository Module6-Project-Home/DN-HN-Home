package com.example.md6projecthndn.service.booking.booking;


import com.example.md6projecthndn.model.dto.BookingByUserDTO;
import com.example.md6projecthndn.model.entity.booking.Booking;
import com.example.md6projecthndn.model.entity.property.Status;
import com.example.md6projecthndn.model.entity.property.Property;
import com.example.md6projecthndn.repository.booking.IBookingRepository;
import com.example.md6projecthndn.service.booking.status.IStatusService;
import com.example.md6projecthndn.service.property.property.IPropertyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    public Booking findById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
    bookingRepository.deleteById(id);
    }

    @Override
    public void updatePropertyStatusIfCheckOutDatePassed() {
        // Get a list of bookings with a check-out date less than or equal to the current date
        List<Booking> bookings = bookingRepository.findByCheckOutDateBefore(LocalDate.now().plusDays(1));

        // Loop through the bookings and update the property status
        for (Booking booking : bookings) {
            Property property = booking.getProperty();

            // Find the available status
            Status availableStatus = statusService.findById(1L);
            if (availableStatus == null) {
                throw new RuntimeException("Status not found"); // Handle the case when status is not found
            }

            // Update the property status
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

    @Override
    public List<BookingByUserDTO> bookingByUser(Long userId) {
      List<Object[]>  results = bookingRepository.bookingByUser(userId);
        List<BookingByUserDTO> bookingByUserDTOs = new ArrayList<>();

        for (Object[] result : results) {
            BookingByUserDTO bookingByUserDTO = new BookingByUserDTO(
                    (String) result[0],
                    (String) result[1],
                    ((Number) result[2]).intValue(),
                    ((Number) result[3]).doubleValue(),
                    (String) result[4]
            );
            bookingByUserDTOs.add(bookingByUserDTO);
        }

        return bookingByUserDTOs;
    }
}
