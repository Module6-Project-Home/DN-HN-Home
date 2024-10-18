package com.example.md6projecthndn.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class RentalBookingDTO {
    private Long id;
    private String guest; // Hoặc bạn có thể để đối tượng guest ở đây
    private String propertyName; // Hoặc bạn có thể để đối tượng property ở đây
    @JsonFormat(pattern="dd/MM/YYYY")
    private LocalDate checkInDate;
    @JsonFormat(pattern="dd/MM/YYYY")
    private LocalDate checkOutDate;
    private String bookingStatus; // Hoặc bookingStatusId
}
