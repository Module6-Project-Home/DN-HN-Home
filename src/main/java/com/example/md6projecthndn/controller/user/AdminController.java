package com.example.md6projecthndn.controller.user;


import com.example.md6projecthndn.model.dto.*;
import com.example.md6projecthndn.model.entity.property.HostPropertyDTO;
import com.example.md6projecthndn.model.entity.property.Property;
import com.example.md6projecthndn.model.entity.property.PropertyDTO;
import com.example.md6projecthndn.model.dto.BookingByUserDTO;
import com.example.md6projecthndn.model.dto.HostDetailDTO;
import com.example.md6projecthndn.model.dto.ROLENAME;
import com.example.md6projecthndn.model.dto.UserDTO;
import com.example.md6projecthndn.model.dto.UserDetailDTO;
import com.example.md6projecthndn.model.entity.user.Role;
import com.example.md6projecthndn.model.entity.user.User;
import com.example.md6projecthndn.model.entity.user.UserStatus;

import com.example.md6projecthndn.service.booking.booking.IBookingService;

import com.example.md6projecthndn.service.email.IEmailService;
import com.example.md6projecthndn.repository.user.IUserRepository;
import com.example.md6projecthndn.service.property.property.IPropertyService;
import com.example.md6projecthndn.service.role.IRoleService;
import com.example.md6projecthndn.service.user.IUserService;
import com.example.md6projecthndn.service.user.status.IUserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserStatusService userStatusService;

    @Autowired
    private IRoleService roleService;

    @Autowired

    private IEmailService emailService;

    @Autowired
    private IBookingService bookingService;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IPropertyService propertyService;

    @PutMapping("/deny-upgrade")
    public ResponseEntity<String> denyUpgrade(@RequestParam Long userId, @RequestParam String reason) {
        User user = userService.findById(userId);
        if (user != null) {
            // Nếu người dùng có yêu cầu nâng cấp, đặt lại trạng thái yêu cầu nâng cấp
            if (user.isUpgradeRequested()) {
                user.setUpgradeRequested(false); // Đặt lại trạng thái yêu cầu nâng cấp

                // Gửi email thông báo từ chối yêu cầu nâng cấp
                String emailSubject = "Yêu cầu nâng cấp chủ nhà bị từ chối";
                String emailContent = "Xin chào " + user.getFullName() + ",\n\nYêu cầu trở thành chủ nhà của bạn đã bị từ chối. Lý do: " + reason;

                emailService.sendEmail(user.getEmail(), emailSubject, emailContent); // Gửi email cho người dùng

                userService.save(user); // Lưu thay đổi
                return ResponseEntity.ok("Yêu cầu nâng cấp đã bị từ chối và email đã được gửi cho người dùng.");
            } else {
                return ResponseEntity.badRequest().body("Không có yêu cầu nâng cấp từ người dùng này.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Người dùng không tồn tại.");
        }
    }


    @GetMapping("/users")// lấy danh sách người dùng theo role + phân trang 5 user 1 page
    public ResponseEntity<Page<UserDTO>> getUsersWithRoleUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<User> users = userService.findAllByUser(PageRequest.of(page, size));
        //String fullName, String phoneNumber, String status, Long userId, String userName
        Page<UserDTO> UserDTOs = users.map(user -> new UserDTO(
                user.getFullName(),
                user.getPhoneNumber(),
                user.getCurrentStatus().name(),
                user.getId(),
                user.getUsername(),
                user.getAvatar(),
                user.getAddress(),
                user.isUpgradeRequested()
        ));
        return ResponseEntity.ok(UserDTOs);
    }


    @GetMapping("/hosts")
    public ResponseEntity<Page<HostDetailDTO>> getHostsWithRoleHost(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Object[]> results = userRepository.getHosts(PageRequest.of(page, size));
        Page<HostDetailDTO> hostDetailDTOs = results.map(fields -> new HostDetailDTO(
                ((Number) fields[0]).longValue(),  // id
                (String) fields[1],                // avatar
                (String) fields[2],                // username
                (String) fields[3],                // full_name
                (String) fields[4],                // phone_number
                (String) fields[5],                // address
                ((Number) fields[6]).doubleValue(), // total_revenue
                ((Number) fields[7]).intValue(),   // property_count
                (String) fields[8]                 // status
        ));
        return ResponseEntity.ok(hostDetailDTOs);
    }

    @PutMapping("/update-status") // cập nhạt status cho user,host
    public ResponseEntity<String> updateUserStatus(@RequestParam("userId") Long userId, @RequestParam("status") String status) {
        try {
            User user = userService.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            //kiểm tra xem status đã tồn tại chưa, nếu chưa thì tạo mới
            UserStatus userStatus = userStatusService.findByStatus(UserStatus.USER_STATUS.valueOf(status.toUpperCase()))
                    .orElseGet(() -> {
                        UserStatus newUserStatus = new UserStatus();
                        newUserStatus.setStatus(UserStatus.USER_STATUS.valueOf(status.toUpperCase()));
                        return userStatusService.save(newUserStatus);
                    });

            // cập nhật status cho user
            user.getUserStatuses().clear();
            user.getUserStatuses().add(userStatus);
            userService.save(user);

            return ResponseEntity.ok("Trạng thái của nguời dùng đã được cập nhật.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status value");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/approve-upgrade")
    public ResponseEntity<?> approveUpgrade(@RequestParam Long userId, @RequestParam boolean isApproved,@RequestParam String reason) {
        try {
            User user = userService.findById(userId); // Kiểm tra người dùng
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            String emailSubject;
            String emailContent;
            // Nếu phê duyệt, thay đổi vai trò
            if (isApproved) {
                Role userRole = roleService.findByName(ROLENAME.ROLE_USER); // Tìm vai trò ROLE_USER
                Role hostRole = roleService.findByName(ROLENAME.ROLE_HOST); // Tìm vai trò ROLE_HOST

                // Nếu tìm thấy vai trò ROLE_USER, loại bỏ nó
                if (userRole != null) {
                    user.getRoles().remove(userRole);
                }
                emailSubject = "Yêu cầu nâng cấp chủ nhà đã được chấp thuận";
                emailContent = "Xin chào " + user.getFullName() + ",\n\nYêu cầu trở thành chủ nhà của bạn đã được phê duyệt. Lý do: " + reason;
                // Nếu tìm thấy vai trò ROLE_HOST, thêm nó vào
                if (hostRole != null) {
                    user.getRoles().add(hostRole);
                    user.setUpgradeRequested(false); // Đặt lại trạng thái yêu cầu nâng cấp
                }



            } else {
                // Nếu không phê duyệt, có thể thêm logic khác nếu cần
                user.setUpgradeRequested(false); // Hủy yêu cầu nâng cấp

                emailSubject = "Yêu cầu nâng cấp chủ nhà bị từ chối";
                emailContent = "Xin chào " + user.getFullName() + ",\n\nYêu cầu trở thành chủ nhà của bạn đã bị từ chối. Lý do: " + reason;
            }
// Gửi email thông báo
            emailService.sendEmail(user.getEmail(), emailSubject, emailContent);

            userService.save(user); // Lưu người dùng
            return ResponseEntity.ok("User upgrade status updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/user-detail")
    public ResponseEntity<?> getUserDetail(@RequestParam("userId") Long userId) {
        UserDetailDTO userDetailDTO = userService.getUserDetails(userId);
        if (userDetailDTO == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Không tìm thấy thông tin người dùng.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(userDetailDTO);
    }

    @GetMapping("/history")
    public ResponseEntity<?> getBookingHistory(@RequestParam Long userId) {
        List<BookingByUserDTO> bookingByUserDTOList = bookingService.bookingByUser(userId);

        if (bookingByUserDTOList.isEmpty()) {
            // Trả về mảng rỗng với status OK thay vì NOT_FOUND
            Map<String, String> response = new HashMap<>();
            response.put("message", "Người dùng chưa thuê nhà.");
            return ResponseEntity.ok(response);
        }

        // Nếu có booking, trả về danh sách
        return ResponseEntity.ok(bookingByUserDTOList);
    }

    @GetMapping("/host-properties")
    public ResponseEntity<?> getPropertiesByHost(@RequestParam Long hostId) {
        try {
            List<Property> properties = propertyService.findByOwnerId(hostId);

            if (properties.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy property nào cho chủ nhà này.");
            }

            List<HostPropertyDTO> propertyDTOs = properties.stream()
                    .map(property -> new HostPropertyDTO(
                            property.getId(),
                            property.getName(),
                            property.getPropertyType().getName(),
                            property.getRoomType().getName(),
                            property.getAddress(),
                            property.getBedrooms(),
                            property.getBathrooms(),
                            property.getDescription(),
                            property.getPricePerNight(),
                            property.getOwner().getFullName(),
                            property.getStatus().getName().name().toUpperCase()
                    ))
                    .toList();
            return ResponseEntity.ok(propertyDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}






