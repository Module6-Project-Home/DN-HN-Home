package com.example.md6projecthndn.service.booking.booking;


import com.example.md6projecthndn.model.dto.BookingByHostDTO;
import com.example.md6projecthndn.model.dto.BookingByUserDTO;
import com.example.md6projecthndn.model.dto.RentalBookingDTO;
import com.example.md6projecthndn.model.entity.booking.Booking;
import com.example.md6projecthndn.model.entity.booking.BookingStatus;
import com.example.md6projecthndn.model.entity.booking.BookingStatusEnum;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
                    ((java.sql.Date) result[2]).toLocalDate(),
                    ((java.sql.Date) result[3]).toLocalDate(),
                    ((Number) result[4]).doubleValue(),
                    (String) result[5],
                    ((Number) result[6]).longValue()
            );
            bookingByUserDTOs.add(bookingByUserDTO);
        }

        return bookingByUserDTOs;
    }

    @Override

    public List<Booking> findByGuestIdAndPropertyIdAndBookingStatusId(Long guestId, Long propertyId, Long bookingStatusId) {
        return bookingRepository.findByGuestIdAndPropertyIdAndBookingStatusId(guestId,propertyId,bookingStatusId);
    }

    public List<RentalBookingDTO> findBookingByOwnerUsername(String username) {
        List<Booking> bookings = bookingRepository.findBookingByOwnerUsername(username);

        // Chuyển đổi từ Booking sang RentalBookingDTO
        return bookings.stream().map(booking -> {
            RentalBookingDTO dto = new RentalBookingDTO();
            dto.setId(booking.getId());
            dto.setGuest(booking.getGuest().getUsername());  // Set tên khách
            dto.setPropertyName(booking.getProperty().getName());  // Set tên nhà
            dto.setCheckInDate(booking.getCheckInDate());
            dto.setCheckOutDate(booking.getCheckOutDate());
            dto.setBookingStatus(booking.getBookingStatus().getStatus().getDescription());  // Set trạng thái đơn
            return dto;
        }).collect(Collectors.toList());

    }

    @Override
    public List<Booking> findByBookingStatus(Long propertyId) {
        return bookingRepository.findByBookingStatus(propertyId);
    }

    @Override
    public void checkIn(Long bookingId) {
        Booking booking = findById(bookingId);
        if (booking == null) {
            throw new RuntimeException("Booking not found");
        }

        // Kiểm tra nếu trạng thái hiện tại là 'Pending'
        if (booking.getBookingStatus().getStatus() == BookingStatusEnum.PENDING) {
            // Cập nhật trạng thái booking thành 'Check-in'
            booking.setBookingStatus(new BookingStatus(BookingStatusEnum.CHECKIN, BookingStatusEnum.CHECKIN.getDescription()));
            save(booking);

            // Cập nhật trạng thái nhà thành 'Đang cho thuê'
            Property property = booking.getProperty();
            Status rentingStatus = statusService.findById(2L); // 'Đang cho thuê' status
            if (rentingStatus != null) {
                property.setStatus(rentingStatus);
                propertyService.save(property);
            }
        } else {
            throw new RuntimeException("Booking is not in pending state");
        }
    }

    @Override
    public void checkOut(Long bookingId) {
        Booking booking = findById(bookingId);
        if (booking == null) {
            throw new RuntimeException("Booking not found");
        }

        // Kiểm tra nếu trạng thái hiện tại là 'Đang ở (Check-in)'
        if (booking.getBookingStatus().getStatus() == BookingStatusEnum.CHECKIN) {
            // Cập nhật trạng thái booking thành 'Đã trả phòng (Check-out)'
            booking.setBookingStatus(new BookingStatus(BookingStatusEnum.CHECKOUT, BookingStatusEnum.CHECKOUT.getDescription()));
            save(booking);

            // Cập nhật trạng thái nhà thành 'Đang trống (Available)'
            Property property = booking.getProperty();
            Status availableStatus = statusService.findById(1L); // 'Đang trống' status
            if (availableStatus != null) {
                property.setStatus(availableStatus);
                propertyService.save(property);
            }
        } else {
            throw new RuntimeException("Booking is not in check-in state");
        }
    }
}

