package com.example.md6projecthndn.model.entity.property;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
public class PropertyDTO {
    private Long id;

    @NotBlank(message = "Tiêu đề không được để trống")
    private String name;

    @NotBlank(message = "Tên loại bất động sản không được để trống")
    private String propertyType; // Chứa tên loại bất động sản

    @NotBlank(message = "Tên loại phòng không được để trống")
    private String roomType;     // Chứa tên loại phòng

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @NotNull(message = "Số phòng ngủ không được để trống")
    @Min(value = 1, message = "Số phòng ngủ phải lớn hơn hoặc bằng 1")
    @Max(value = 3, message = "Số phòng ngủ phải nhỏ hơn hoặc bằng 10")
    private Integer bedrooms;

    @NotNull(message = "Số phòng tắm không được để trống")
    @Min(value = 1, message = "Số phòng tắm phải lớn hơn hoặc bằng 1")
    @Max(value = 3, message = "Số phòng tắm phải nhỏ hơn hoặc bằng 3")
    private Integer bathrooms;

    private String description;

    @NotNull(message = "Giá mỗi đêm không được để trống")
    @Min(value = 0, message = "Giá mỗi đêm phải lớn hơn hoặc bằng 0")
    private Double pricePerNight;

    private String owner;
    private String status;
    @NotNull(message = "Ảnh không được để trống")
    private List<String> imageUrls;

}
