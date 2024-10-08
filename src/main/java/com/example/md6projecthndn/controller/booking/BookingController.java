package com.example.md6projecthndn.controller.booking;


import com.example.md6projecthndn.model.dto.BookingDTO;
import com.example.md6projecthndn.model.entity.booking.Booking;
import com.example.md6projecthndn.model.entity.booking.Status;
import com.example.md6projecthndn.model.entity.property.Property;
import com.example.md6projecthndn.model.entity.user.User;
import com.example.md6projecthndn.service.booking.booking.IBookingService;
import com.example.md6projecthndn.service.booking.status.IStatusService;
import com.example.md6projecthndn.service.property.property.IPropertyService;
import com.example.md6projecthndn.service.user.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    private IBookingService bookingService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IPropertyService propertyService;

    @Autowired
    private IStatusService statusService;

    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingDTO bookingDTO, BindingResult bindingResult) {
        try {
            List<Booking> overlappingBookings = bookingService.findOverlappingBookings(
                    bookingDTO.getProperty().getId(),
                    bookingDTO.getCheckInDate(),
                    bookingDTO.getCheckOutDate()
            );
            bookingDTO.setOverlappingBookings(overlappingBookings);

            Status status = statusService.findById(bookingDTO.getStatus().getId());
            bookingDTO.setStatus(status);
            Property property = propertyService.findById(bookingDTO.getProperty().getId());
            bookingDTO.setProperty(property);
            User user = userService.findByUsername(bookingDTO.getGuest().getUsername());
            bookingDTO.setGuest(user);

            bookingDTO.validate(bookingDTO, bindingResult);
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );
                return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
            }

            Booking booking = new Booking();
            BeanUtils.copyProperties(bookingDTO, booking);
            bookingService.save(booking);
            return new ResponseEntity<>(booking, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }





    @GetMapping("/{bookingId}")
    public ResponseEntity<?> getBookingDetails(@PathVariable Long bookingId) {
        Booking booking = bookingService.findById(bookingId);
        if (booking == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy booking");
        }
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/overlapping")
    public ResponseEntity<List<Booking>> getOverlappingBookings(
            @RequestParam Long propertyId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        List<Booking> overlappingBookings = bookingService.findOverlappingBookings(propertyId, startDate, endDate);

        return ResponseEntity.ok(overlappingBookings);
    }
}
