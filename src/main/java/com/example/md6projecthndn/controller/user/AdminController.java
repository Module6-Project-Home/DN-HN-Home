package com.example.md6projecthndn.controller.user;


import com.example.md6projecthndn.model.dto.ROLENAME;
import com.example.md6projecthndn.model.entity.user.Role;
import com.example.md6projecthndn.model.entity.user.User;
import com.example.md6projecthndn.service.role.IRoleService;
import com.example.md6projecthndn.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;


    @PutMapping("/approve-upgrade")
    public ResponseEntity<?> approveUpgrade(@RequestParam Long userId, @RequestParam boolean isApproved) {
        try {
            User user = userService.findById(userId); // Kiểm tra người dùng
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Nếu phê duyệt, thay đổi vai trò
            if (isApproved) {
                Role userRole = roleService.findByName(ROLENAME.ROLE_USER); // Tìm vai trò ROLE_USER
                Role hostRole = roleService.findByName(ROLENAME.ROLE_HOST); // Tìm vai trò ROLE_HOST

                // Nếu tìm thấy vai trò ROLE_USER, loại bỏ nó
                if (userRole != null) {
                    user.getRoles().remove(userRole);
                }

                // Nếu tìm thấy vai trò ROLE_HOST, thêm nó vào
                if (hostRole != null) {
                    user.getRoles().add(hostRole);
                }
            } else {
                // Nếu không phê duyệt, có thể thêm logic khác nếu cần
            }

            userService.save(user); // Lưu người dùng
            return ResponseEntity.ok("User upgrade status updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @PutMapping("/deny-upgrade")
    public ResponseEntity<String> denyUpgrade(@RequestParam Long userId) {
        User user = userService.findById(userId);
        if (user != null) {
            // Nếu người dùng có yêu cầu nâng cấp, đặt lại trạng thái yêu cầu nâng cấp
            if (user.isUpgradeRequested()) {
                user.setUpgradeRequested(false); // Đặt lại trạng thái yêu cầu nâng cấp
                userService.save(user);
                return ResponseEntity.ok("Yêu cầu nâng cấp đã bị từ chối");
            } else {
                return ResponseEntity.badRequest().body("Không có yêu cầu nâng cấp từ người dùng này");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Người dùng không tồn tại");
        }
    }

}
