package com.codegym.service.booking.bookingstatus;

import com.codegym.model.entity.booking.BookingStatus;
import com.codegym.repository.booking.IBookingStatusRepository;
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
    public Optional<BookingStatus> findById(Long id) {
        return bookingStatusRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
    bookingStatusRepository.deleteById(id);
    }
}
