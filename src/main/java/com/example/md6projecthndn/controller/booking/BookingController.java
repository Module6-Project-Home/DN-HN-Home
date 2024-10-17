package com.example.md6projecthndn.controller.booking;


import com.example.md6projecthndn.model.dto.BookingByUserDTO;
import com.example.md6projecthndn.model.dto.BookingDTO;
import com.example.md6projecthndn.model.dto.review.ReviewDTO;
import com.example.md6projecthndn.model.dto.RentalBookingDTO;
import com.example.md6projecthndn.model.entity.booking.Booking;
import com.example.md6projecthndn.model.entity.booking.BookingStatus;
import com.example.md6projecthndn.model.entity.booking.Review;
import com.example.md6projecthndn.model.entity.property.Status;
import com.example.md6projecthndn.model.entity.property.Property;
import com.example.md6projecthndn.model.entity.user.User;
import com.example.md6projecthndn.service.booking.booking.IBookingService;
import com.example.md6projecthndn.service.booking.bookingstatus.IBookingStatusService;
import com.example.md6projecthndn.service.booking.review.IReviewService;
import com.example.md6projecthndn.service.booking.status.IStatusService;
import com.example.md6projecthndn.service.notification.INotificationService;
import com.example.md6projecthndn.service.property.property.IPropertyService;
import com.example.md6projecthndn.service.user.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @Autowired
    private IBookingStatusService bookingStatusService;

    @Autowired
    private IReviewService reviewService;

    @Autowired
    private INotificationService notificationService;

    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingDTO bookingDTO, BindingResult bindingResult) {
        try {
            List<Booking> overlappingBookings = bookingService.findOverlappingBookings(
                    bookingDTO.getProperty().getId(),
                    bookingDTO.getCheckInDate(),
                    bookingDTO.getCheckOutDate()
            );
            bookingDTO.setOverlappingBookings(overlappingBookings);

            BookingStatus status = bookingStatusService.findById(bookingDTO.getBookingStatus().getId());
            bookingDTO.setBookingStatus(status);
            Property property = propertyService.findById(bookingDTO.getProperty().getId());
            Status newStatus = statusService.findById(2l);
            property.setStatus(newStatus);
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
            String guestName = bookingDTO.getGuest().getUsername();
            String propertyName = bookingDTO.getProperty().getName();
            User owner = bookingDTO.getProperty().getOwner();
            notificationService.notifyOwnerOfBooking(guestName, propertyName, owner);
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

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelBooking(@RequestParam Long bookingId) {
        Booking booking = bookingService.findById(bookingId);

        if (booking == null) {
            return new ResponseEntity<>("Booking not found", HttpStatus.NOT_FOUND);
        }

        BookingStatus cancelStatus = bookingStatusService.findById(4L);
        if (cancelStatus == null) {
            return new ResponseEntity<>("Cancel status not found", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        booking.setBookingStatus(cancelStatus);
        bookingService.save(booking);
        String guestName = booking.getGuest().getUsername();
        String propertyName = booking.getProperty().getName();
        User owner = booking.getProperty().getOwner();
        notificationService.notifyOwnerOfCancellation(guestName, propertyName, owner);

        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    @PostMapping("/review")
    public ResponseEntity<?> reviewBooking( @RequestBody Review review) {
        User user = userService.findByUsername(review.getGuest().getUsername());
        Property property = propertyService.findById(review.getProperty().getId());

        List<Booking> bookings = bookingService.findByGuestIdAndPropertyIdAndBookingStatusId(user.getId(),property.getId(),3l);

        if(bookings == null || bookings.isEmpty()) {
            return new ResponseEntity<>("Bạ chưa thuê nhà nên không để lại đánh giá", HttpStatus.BAD_REQUEST);
        }

        String guest = review.getGuest().getUsername();
        String propertyName = property.getName();
        User owner = property.getOwner();
        notificationService.notifyOwnerOfReview(guest, propertyName, owner);

        review.setProperty(property);
        review.setGuest(user);
        reviewService.save(review);
        return new ResponseEntity<>(review, HttpStatus.OK);
    }

    @GetMapping("/ownerHistory")
    public ResponseEntity<List<RentalBookingDTO>> getBookingsByOwnerUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername(); // Lấy username từ UserDetails
        }

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<RentalBookingDTO> bookings = bookingService.findBookingByOwnerUsername(username);
        return ResponseEntity.ok(bookings);
    }



}