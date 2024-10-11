package com.example.md6projecthndn.controller;


import com.example.md6projecthndn.model.dto.UserProfileDTO;
import com.example.md6projecthndn.model.dto.UserProfileMapper;
import com.example.md6projecthndn.model.entity.user.User;
import com.example.md6projecthndn.service.user.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final IUserService userService;
    private final UserProfileMapper userProfileMapper;

    public UserController(IUserService userService, UserProfileMapper userProfileMapper) {
        this.userService = userService;
        this.userProfileMapper = userProfileMapper;
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
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldErrors());
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }
        userService.updateUser(user);
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
}
