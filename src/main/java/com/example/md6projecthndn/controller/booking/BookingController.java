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

import java.security.Principal;
import java.util.List;

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


    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@RequestBody BookingDTO bookingDTO, BindingResult bindingResult, Principal principal) {
        Property property = propertyService.findById(bookingDTO.getProperty().getId());
        String username = principal.getName();
        User userCurrent = userService.findByUsername(username);

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Người dùng chưa đăng nhập");
        }

        if (property == null || userCurrent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy tài sản hoặc người dùng");
        }

        List<Booking> overlappingBookings = bookingService.findOverlappingBookings(
                bookingDTO.getProperty().getId(),
                bookingDTO.getCheckInDate(),
                bookingDTO.getCheckOutDate()
        );

        bookingDTO.setOverlappingBookings(overlappingBookings);
        bookingDTO.validate(bookingDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }

        Status status = statusService.findById(2L);
        if (status == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy trạng thái");
        }

        property.setStatus(status);

        Booking booking = new Booking();
        BeanUtils.copyProperties(bookingDTO, booking);
        booking.setStatus(status);
        bookingService.save(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body("Đặt phòng thành công");
    }


    @GetMapping("/{bookingId}")
    public ResponseEntity<?> getBookingDetails(@PathVariable Long bookingId) {
        Booking booking = bookingService.findById(bookingId);
        if (booking == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy booking");
        }
        return ResponseEntity.ok(booking);
    }
}
