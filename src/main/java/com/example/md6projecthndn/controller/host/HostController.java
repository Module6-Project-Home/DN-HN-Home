package com.example.md6projecthndn.controller.host;


import com.example.md6projecthndn.model.entity.property.Property;
import com.example.md6projecthndn.model.entity.property.PropertyDTO;
import com.example.md6projecthndn.model.entity.property.PropertyImage;
import com.example.md6projecthndn.service.property.property.IPropertyService;
import com.example.md6projecthndn.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/host")
public class HostController {
    @Autowired
    private IPropertyService propertyService;

    @Autowired
    private IUserService userService;


    @GetMapping("/listMyHomestay")
    public ResponseEntity<List<PropertyDTO>> getMyHomestay() {
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

        // Lấy danh sách tài sản của chủ nhà
        List<Property> properties = propertyService.findByOwnerUsername(username);

        // Chuyển đổi sang DTO (nếu cần)

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
