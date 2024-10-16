package com.example.md6projecthndn.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class BookingByUserDTO {
    private String propertyName;
    private String address;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Double totalSpent;
    private String status;
    private Long bookingId;

    public BookingByUserDTO(String propertyName, String address, LocalDate checkInDate, LocalDate checkOutDate, Double totalSpent, String status, Long bookingId) {
        this.propertyName = propertyName;
        this.address = address;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalSpent = totalSpent;
        this.status = status;
        this.bookingId = bookingId;
    }
}
