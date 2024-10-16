package com.example.md6projecthndn.service.booking.bookingstatus;


import com.example.md6projecthndn.model.entity.booking.BookingStatus;
import com.example.md6projecthndn.model.entity.booking.BookingStatusEnum;
import com.example.md6projecthndn.repository.booking.IBookingStatusRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class BookingStatusService implements IBookingStatusService {
    private final IBookingStatusRepository bookingStatusRepository;
    public BookingStatusService(IBookingStatusRepository bookingStatusRepository) {
        this.bookingStatusRepository = bookingStatusRepository;
    }


    @Override
    public Iterable<BookingStatus> findAll() {
        return bookingStatusRepository.findAll();
    }

    @Override
    public void save(BookingStatus bookingStatus) {
    bookingStatusRepository.save(bookingStatus);
    }

    @Override
    public BookingStatus findById(Long id) {
        return bookingStatusRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
    bookingStatusRepository.deleteById(id);
    }

    public void preloadBookingStatusData() {
        if (bookingStatusRepository.count() == 0) {
            bookingStatusRepository.save(new BookingStatus(BookingStatusEnum.PENDING, "Chờ nhận phòng"));
            bookingStatusRepository.save(new BookingStatus(BookingStatusEnum.CHECKIN, "Đang ở"));
            bookingStatusRepository.save(new BookingStatus(BookingStatusEnum.CHECKOUT, "Đã trả phòng"));
            bookingStatusRepository.save(new BookingStatus(BookingStatusEnum.CANCELED, "Đã hủy"));
        }
    }
}
