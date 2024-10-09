package com.example.md6projecthndn.model.dto;



import com.example.md6projecthndn.model.entity.booking.Booking;
import com.example.md6projecthndn.model.entity.booking.Status;
import com.example.md6projecthndn.model.entity.property.Property;
import com.example.md6projecthndn.model.entity.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BookingDTO implements Validator {


    private User guest;

    private Property property;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;

    private Status status;
    private List<Booking> overlappingBookings;

    @Override
    public void validate(Object target, Errors errors) {
        BookingDTO bookingDTO = (BookingDTO) target;

        // Kiểm tra booking chồng chéo
        if (bookingDTO.getProperty() != null && bookingDTO.getCheckInDate() != null && bookingDTO.getCheckOutDate() != null) {
            if (!this.overlappingBookings.isEmpty()) {
                errors.rejectValue("property", "property.booking.overlap", "Nhà này đã được đặt trong thời gian bạn chọn, Vui lòng chọn lại!");
            }
        }

        // Kiểm tra ngày đặt
        if (bookingDTO.getCheckInDate() != null && bookingDTO.getCheckOutDate() != null) {
            LocalDate today = LocalDate.now();
            if (!bookingDTO.getCheckInDate().isAfter(today)) {
                errors.rejectValue("checkInDate", "checkInDate.past", "Xin lỗi. Bạn phải đặt phòng trước ít nhất 1 ngày");
            }
            if (!bookingDTO.getCheckInDate().isBefore(bookingDTO.getCheckOutDate().minusDays(1))) {
                errors.rejectValue("checkInDate", "checkInDate.invalid", "Xin lỗi. Bạn phải ở lại ít nhất 1 đêm");
            }
        }

        // Kiểm tra nếu ngày không được chọn
        if (bookingDTO.getCheckInDate() == null || bookingDTO.getCheckOutDate() == null) {
            errors.rejectValue("checkInDate", "checkInDate.null", "Vui lòng chọn thời gian");
        }
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return BookingDTO.class.equals(clazz);
    }

}
