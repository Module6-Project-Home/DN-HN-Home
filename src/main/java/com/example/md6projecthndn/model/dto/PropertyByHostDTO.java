package com.example.md6projecthndn.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class PropertyByHostDTO {
    private Long id;
    private String name;
    private String propertyType;
    private String roomType;
    private String address;
    private Integer bedrooms;
    private Integer bathrooms;
    private Double pricePerNight;
    private String status;


}
