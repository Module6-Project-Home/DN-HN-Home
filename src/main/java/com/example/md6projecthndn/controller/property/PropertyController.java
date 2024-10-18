package com.example.md6projecthndn.controller.property;

import com.example.md6projecthndn.model.dto.PropertyDetailDTO;
import com.example.md6projecthndn.model.dto.PropertyTopBookingDTO;


import com.example.md6projecthndn.model.entity.property.*;

import com.example.md6projecthndn.model.entity.user.User;
import com.example.md6projecthndn.service.booking.status.IStatusService;
import com.example.md6projecthndn.service.property.property.IPropertyService;
import com.example.md6projecthndn.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {
    @Autowired
    private IPropertyService propertyService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IStatusService statusService;

    @PostMapping
    public ResponseEntity<Property> createProperty(@Valid @RequestBody PropertyDTO propertyDTO) {
        // Lấy thông tin từ SecurityContextHolder sau khi JWT đã được xác thực
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername(); // Lấy username từ UserDetails
        }

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        User owner = userService.findByUsername(username);
        if (owner == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Gán owner cho PropertyDTO
        propertyDTO.setOwner(username);
        Property savedProperty = propertyService.addPropertyPost(propertyDTO);
        System.out.println(savedProperty);

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
            dto.setStatus(p.getStatus().getName().toString());
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
            dto.setStatus(p.getStatus().getName().toString());
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
            dto.setStatus(p.getStatus().getName().toString());
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

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> showDetailForm(@PathVariable Long id) {
        PropertyDetailDTO property = propertyService.findPropertyById(id);

        if (property != null) {
            return new ResponseEntity<>(property, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Property not found", HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/top-booking")
    public ResponseEntity<?> showTopBooking() {
        List<PropertyTopBookingDTO> propertyTopBooking = propertyService.findPropertyTopBookingDTO();
        if (propertyTopBooking != null) {
            return new ResponseEntity<>(propertyTopBooking, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("list property not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Property> updateProperty(@PathVariable Long id, @Valid @RequestBody PropertyDTO propertyDTO) {
        // Lấy thông tin từ SecurityContextHolder sau khi JWT đã được xác thực
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername(); // Lấy username từ UserDetails
        }

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        User owner = userService.findByUsername(username);
        if (owner == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Gọi service để cập nhật Property
        Property updatedProperty = propertyService.updateProperty(id, propertyDTO, username);

        return ResponseEntity.ok(updatedProperty);
    }

//    @PutMapping("/change-status/{id}")
//    public ResponseEntity<?> changePropertyStatus(@PathVariable Long id, @RequestParam Status.PROPERTY_STATUS newStatus) {
//        Property property = propertyService.findById(id);
//        if (property == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Property not found");
//        }
//
//        Status.PROPERTY_STATUS currentStatus = property.getStatus().getName();
//        if (currentStatus == Status.PROPERTY_STATUS.VACANT || currentStatus == Status.PROPERTY_STATUS.MAINTENANCE) {
//            property.getStatus().setName(newStatus);
//            propertyService.save(property);
//            return ResponseEntity.ok("Property status updated successfully");
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Property status can only be changed if it is VACANT or MAINTENANCE");
//        }
//    }


    @PutMapping("/change-status/{id}")
    public ResponseEntity<Property> changePropertyStatus(@PathVariable Long id, @RequestParam Status.PROPERTY_STATUS newStatus) {
        Property property = propertyService.findById(id);
        if (property == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
          Optional<Status> statusEntity = statusService.findByName(newStatus);
         property.setStatus(statusEntity.get());
            propertyService.save(property);
            return ResponseEntity.ok(property);

    }

}