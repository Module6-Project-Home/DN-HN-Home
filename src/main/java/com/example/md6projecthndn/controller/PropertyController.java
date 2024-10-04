package com.example.md6projecthndn.controller;


import com.example.md6projecthndn.model.entity.property.PropertyImage;

import com.example.md6projecthndn.model.entity.property.Property;
import com.example.md6projecthndn.model.entity.property.PropertyDTO;
import com.example.md6projecthndn.model.entity.user.User;
import com.example.md6projecthndn.service.property.property.IPropertyService;
import com.example.md6projecthndn.service.user.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/properties") // Đường dẫn gốc cho controller
public class PropertyController {
    @Autowired
    private IPropertyService propertyService;

    @Autowired
    private IUserService userService;

    @PostMapping
    public ResponseEntity<Property> createProperty(@Valid @RequestBody PropertyDTO propertyDTO) {
        String username = propertyDTO.getOwner(); // Giả sử bạn đã cung cấp owner trong PropertyDTO
        Optional<User> owner = userService.findByUsername(username);
        if (owner.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Property savedProperty = propertyService.addPropertyPost(propertyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProperty);
    }
    @GetMapping
    public ResponseEntity<List<PropertyDTO>> findAllProperties() {
        List<Property> properties =(List<Property>) propertyService.findAll();

        // Chuyển đổi danh sách Property thành danh sách PropertyDTO
        List<PropertyDTO> propertyDTOs = properties.stream().map(p -> {
            PropertyDTO dto = new PropertyDTO();
            dto.setId(p.getId());
            dto.setName(p.getName());
            dto.setAddress(p.getAddress());
            dto.setPricePerNight(p.getPricePerNight());
            dto.setOwner(p.getOwner().getUsername());
            dto.setBathrooms(p.getBathrooms());
            dto.setBedrooms(p.getBedrooms());
            dto.setStatus(p.getStatus().getName());
            dto.setDescription(p.getDescription());
            dto.setPropertyType(p.getPropertyType().getName());
            dto.setRoomType(p.getRoomType().getName());

            // Thêm danh sách ảnh
            List<String> imageUrls = p.getImages().stream()
                    .map(PropertyImage::getImageUrl)
                    .toList();
            dto.setImageUrls(imageUrls);

            return dto;
        }).toList();

        return ResponseEntity.ok(propertyDTOs);
    }




    @GetMapping("/{id}")
    public ResponseEntity<PropertyDTO> findRPropertyById(@PathVariable Long id) {
        Optional<Property> property = propertyService.findById(id);
        if (property.isPresent()) {
            Property p = property.get();
            PropertyDTO dto = new PropertyDTO();

            // Set các trường cho DTO
            dto.setId(p.getId());
            dto.setName(p.getName());
            dto.setAddress(p.getAddress());
            dto.setPricePerNight(p.getPricePerNight());
            dto.setOwner(p.getOwner().getUsername());
            dto.setBathrooms(p.getBathrooms());
            dto.setBedrooms(p.getBedrooms());
            dto.setStatus(p.getStatus().getName());
            dto.setDescription(p.getDescription());

            // Thêm PropertyType và RoomType
            dto.setPropertyType(p.getPropertyType().getName());
            dto.setRoomType(p.getRoomType().getName());
            System.out.println("Images: " + p.getImages());  // Debug xem ảnh có được lấy từ CSDL hay không


            // Thêm imageUrls
            List<String> imageUrls = p.getImages().stream()
                    .map(PropertyImage::getImageUrl) // Sử dụng PropertyImage
                    .toList();
            dto.setImageUrls(imageUrls);

            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }




}