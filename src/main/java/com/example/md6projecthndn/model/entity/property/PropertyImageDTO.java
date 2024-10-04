package com.example.md6projecthndn.model.entity.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyImageDTO {
    private Long id;
    private String imageUrl;
    private Long propertyId;  // Thay vì object Property, chỉ lưu trữ propertyId
}
