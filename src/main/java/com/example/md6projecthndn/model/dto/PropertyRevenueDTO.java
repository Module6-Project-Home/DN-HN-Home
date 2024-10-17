package com.example.md6projecthndn.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
public class PropertyRevenueDTO {
    private Long id;
    private String name;
    private String address;
    private Double pricePerNight;
    private String status;
    private double revenue;
    private String owner;

    public PropertyRevenueDTO(Long id, String name, String address, Double pricePerNight, String status, double revenue, String owner) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.pricePerNight = pricePerNight;
        this.status = status;
        this.revenue = revenue;
        this.owner = owner;
    }
}
