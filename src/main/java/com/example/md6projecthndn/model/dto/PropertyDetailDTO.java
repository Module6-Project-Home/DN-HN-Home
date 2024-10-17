package com.example.md6projecthndn.model.dto;

import com.example.md6projecthndn.model.dto.review.ReviewDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class PropertyDetailDTO {
    private Long id;
    private String name;
    private String address;
    private int bedrooms;
    private int bathrooms;
    private String description;
    private double pricePerNight;
    private Long ownerId;
    private Set<PropertyImageDTO> images;
    private List<ReviewDTO> reviews;
}
