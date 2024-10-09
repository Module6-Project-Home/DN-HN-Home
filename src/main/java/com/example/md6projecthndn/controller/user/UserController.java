package com.example.md6projecthndn.controller.user;


import com.example.md6projecthndn.model.entity.user.User;
import com.example.md6projecthndn.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService userService;

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
}
