package com.example.md6projecthndn.controller.host;


import com.example.md6projecthndn.model.dto.MonthlyRevenueDTO;
import com.example.md6projecthndn.model.dto.PropertyRevenueDTO;
import com.example.md6projecthndn.model.entity.booking.Review;
import com.example.md6projecthndn.model.entity.property.Property;
import com.example.md6projecthndn.model.entity.property.PropertyDTO;
import com.example.md6projecthndn.model.entity.property.PropertyImage;
import com.example.md6projecthndn.service.booking.review.IReviewService;
import com.example.md6projecthndn.service.property.property.IPropertyService;
import com.example.md6projecthndn.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/host")
public class HostController {
    @Autowired
    private IPropertyService propertyService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IReviewService reviewService;
//

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

    // Đếm số lượng phòng của chủ nhà
    @GetMapping("/countHostProperties")
    public ResponseEntity<Long> countHostProperties(@RequestParam("ownerId") Long ownerId) {
        Long propertyCount = propertyService.countByOwnerId(ownerId);
        return ResponseEntity.ok(propertyCount);

    }

    @GetMapping("/revenue")
    public ResponseEntity<List<PropertyRevenueDTO>> getrevenue() {
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
        List<PropertyRevenueDTO> properties = propertyService.getPropertyRevenueDetails(username);

        // Chuyển đổi sang DTO (nếu cần)
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/monthlyRevenue")
    public ResponseEntity<List<MonthlyRevenueDTO>> getMonthlyRevenue(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
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

        // Kiểm tra xem startDate có trước endDate không
        if (startDate.after(endDate)) {
            return ResponseEntity.badRequest().body(null); // Trả về mã lỗi 400 nếu ngày không hợp lệ
        }

        // Lấy danh sách tài sản của chủ nhà
        List<MonthlyRevenueDTO> revenue = propertyService.getMonthlyRevenue(username,startDate,endDate);

        // Chuyển đổi sang DTO (nếu cần)
        return ResponseEntity.ok(revenue);
    }

    @PutMapping("/reviews/{id}/hide")
    public ResponseEntity<?> hideReview(@PathVariable Long id) {
            Review review = reviewService.findById(id);
            if (review == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bài đánh giá không tồn tại");
            }
            review.setIsValid(false);
            reviewService.save(review);
            return ResponseEntity.ok("Review hidden successfully");
    }


}
