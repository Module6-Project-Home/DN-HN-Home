package com.example.md6projecthndn.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PropertyTopBookingDTO {
    private Long id;
    private String name;
    private double pricePerNight;
    private String address;
    private String images;

    public PropertyTopBookingDTO(Long id, String name, double pricePerNight, String address, String images) {
        this.id = id;
        this.name = name;
        this.pricePerNight = pricePerNight;
        this.address = address;
        this.images = images;
    }
}
