package com.example.md6projecthndn.controller.user;


import com.example.md6projecthndn.model.dto.BookingByUserDTO;
import com.example.md6projecthndn.model.dto.UserProfileDTO;
import com.example.md6projecthndn.model.dto.UserProfileMapper;
import com.example.md6projecthndn.model.entity.user.User;
import com.example.md6projecthndn.service.booking.booking.IBookingService;
import com.example.md6projecthndn.service.user.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final IUserService userService;
    private final UserProfileMapper userProfileMapper;
    private final IBookingService bookingService;

    public UserController(IUserService userService, UserProfileMapper userProfileMapper, IBookingService bookingService) {
        this.userService = userService;
        this.userProfileMapper = userProfileMapper;
        this.bookingService = bookingService;
    }

//    @GetMapping("/")
//    public ResponseEntity<?> testResponse() {
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result) {

        if (result.hasErrors()) {
            // Return the validation errors
            return ResponseEntity.badRequest().body(result.getFieldErrors());
        }

        // Check if passwords match
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Mật khẩu không khớp!");
        }

        // Kiểm tra xem email đã tồn tại chưa
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email đã được sử dụng!");
        }

        // Kiểm tra xem username đã tồn tại chưa
        if (userService.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Username đã được sử dụng");
        }

        // Call service to register the user
        String registrationResult = userService.registerNewUser(user);
        if (registrationResult.equals("Đăng ký tài khoản thành công!")) {
            return ResponseEntity.ok(registrationResult);
        } else {
            System.out.println("Registration result is: " + registrationResult);
            return ResponseEntity.badRequest().body(registrationResult);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getCurrentUser() {
        // Lấy thông tin người dùng đang đăng nhập từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
//        System.out.println(currentUsername+ "+++++++++++");

        // Lấy User từ cơ sở dữ liệu dựa vào username
        User user = userService.findByUsername(currentUsername);
//                .orElseThrow(() -> new RuntimeException("User not found"));

        // Chuyển đổi User thành UserDTO
        UserProfileDTO userProfileDTO = userProfileMapper.toDTO(user);

        // Trả về UserDTO
        return ResponseEntity.ok(userProfileDTO);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserProfileDTO user, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldErrors());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.findByUsername(currentUsername);

        userService.updateUser(currentUser, user);
        return ResponseEntity.ok().body("User updated successfully");
    }

    @PutMapping("/request-upgrade")
    public ResponseEntity<String> requestUpgrade(@RequestParam Long userId) {
        User user = userService.findById(userId); // Sử dụng userService thay vì userRepository
        if (user != null) {
            user.setUpgradeRequested(true);
            userService.save(user); // Giả sử UserService có phương thức save()
            return ResponseEntity.ok("Yêu cầu nâng cấp thành công");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Người dùng không tồn tại");
        }
    }

    @GetMapping("/current")
    public User getCurrentUser(Authentication authentication) {
        // Lấy thông tin người dùng từ Authentication
        String username = authentication.getName(); // Lấy tên người dùng từ Authentication
        return userService.findByUsername(username); // Gọi service để lấy thông tin người dùng
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestParam String oldPassword, @RequestParam String newPassword){

        // Lấy thông tin người dùng đang đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // Lấy thông tin người dùng từ username
        User currentUser = userService.findByUsername(currentUsername);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Người dùng không tồn tại");
        }

        boolean isUpdated = userService.changePassword(currentUser, oldPassword, newPassword);

        if(isUpdated){
            return ResponseEntity.ok("Thay đổi mật khẩu thành công!");
        } else {
            return ResponseEntity.badRequest().body("Thay đổi mật khẩu thất bại!");
        }
    }

    @GetMapping("/findByUsername")
    public User findByUsername(@RequestParam String username) {
        return userService.findByUsername(username);
    }


    @GetMapping("/history-booking")
    public ResponseEntity<?> getBookingHistory(@RequestParam Long userId) {
        List<BookingByUserDTO> bookingHistoryList = bookingService.bookingByUser(userId);

        if (bookingHistoryList.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Người dùng chưa thuê nhà.");
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.ok(bookingHistoryList);
    }

}
