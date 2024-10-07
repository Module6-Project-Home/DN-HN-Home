package com.example.md6projecthndn.controller;


import com.example.md6projecthndn.model.entity.user.User;
import com.example.md6projecthndn.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    //test
    @GetMapping("/list")
    public ResponseEntity<?> getUserList() {
        Iterable<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<?> testResponse() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result) {

        if (result.hasErrors()) {
            // Return the validation errors
            return ResponseEntity.badRequest().body(result.getFieldErrors());
        }

        // Check if passwords match
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }

        // Check if the username or email is already taken (mock logic for now)
        if (isUsernameOrEmailTaken(user.getUsername(), "mockEmail@example.com")) {
            return ResponseEntity.badRequest().body("Username or Email is already taken");
        }

        // Call service to register the user
        String registrationResult = userService.registerNewUser(user);
        if (registrationResult.equals("User registered successfully.")) {
            return ResponseEntity.ok(registrationResult);
        } else {
            System.out.println("Registration result is: " + registrationResult);
            return ResponseEntity.badRequest().body(registrationResult);
        }
    }

    // Mock method for checking if the username or email is taken
    private boolean isUsernameOrEmailTaken(String username, String email) {
        // Replace with real database logic
        return false; // Assume no conflicts for now
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
