package com.example.md6projecthndn.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookingByUserDTO {
    private String propertyName;
    private String address;
    private Integer rentalPeriod;
    private Double totalSpent;
    private String status;

    public BookingByUserDTO (String propertyName, String address, Integer rentalPeriod, Double totalSpent, String status) {
        this.propertyName = propertyName;
        this.address = address;
        this.rentalPeriod = rentalPeriod;
        this.totalSpent = totalSpent;
        this.status = status;
    }
}
