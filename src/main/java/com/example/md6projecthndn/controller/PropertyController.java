package com.example.md6projecthndn.controller;


import com.example.md6projecthndn.model.entity.property.*;

import com.example.md6projecthndn.model.entity.user.User;
import com.example.md6projecthndn.service.property.property.IPropertyService;
import com.example.md6projecthndn.service.user.IUserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {
    @Autowired
    private IPropertyService propertyService;

    @Autowired
    private IUserService userService;

    @PostMapping
    public ResponseEntity<Property> createProperty(@Valid @RequestBody PropertyDTO propertyDTO) {
        String username = propertyDTO.getOwner(); // Giả sử bạn đã cung cấp owner trong PropertyDTO
        User owner = userService.findByUsername(username);
        if (owner==null) {
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
        Property p = propertyService.findById(id);
        if (p!= null) {

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

    @GetMapping("/search")
    public ResponseEntity<List<PropertyDTO  >> searchProperties(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) PropertyType propertyType,
            @RequestParam(required = false) RoomType roomType,
            @RequestParam(required = false) Integer minBedrooms,
            @RequestParam(required = false) Integer maxBedrooms,
            @RequestParam(required = false) Integer minBathrooms,
            @RequestParam(required = false) Integer maxBathrooms,
            @RequestParam(required = false) LocalDate checkInDate,
            @RequestParam(required = false) LocalDate checkOutDate
    ) {
        List<Property> properties = (List<Property>) propertyService.searchProperties(
                name, address, minPrice, maxPrice, propertyType, roomType,
                minBedrooms, maxBedrooms, minBathrooms, maxBathrooms,
                checkInDate, checkOutDate );
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






}