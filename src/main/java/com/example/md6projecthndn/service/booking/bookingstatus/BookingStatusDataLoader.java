package com.example.md6projecthndn.service.booking.bookingstatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BookingStatusDataLoader implements CommandLineRunner {

    @Autowired
    private BookingStatusService bookingStatusService;

    @Override
    public void run(String... args) throws Exception {
        bookingStatusService.preloadBookingStatusData();
    }
}

