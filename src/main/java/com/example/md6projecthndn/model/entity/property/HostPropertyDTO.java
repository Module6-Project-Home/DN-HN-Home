package com.example.md6projecthndn.model.entity.property;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostPropertyDTO {
    private Long id;
    private String name;
    private String propertyType;
    private String roomType;
    private String address;
    private Integer bedrooms;
    private Integer bathrooms;
    private String description;
    private Double pricePerNight;
    private String owner;
    private String status;

    public HostPropertyDTO(Long id, String name, String propertyType, String roomType, String address, Integer bedrooms, Integer bathrooms, String description, Double pricePerNight, String owner, String status) {
        this.id = id;
        this.name = name;
        this.propertyType = propertyType;
        this.roomType = roomType;
        this.address = address;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.description = description;
        this.pricePerNight = pricePerNight;
        this.owner = owner;
        this.status = status;
    }
}
