package com.example.md6projecthndn.controller;

import com.example.md6projecthndn.model.User;
import com.example.md6projecthndn.service.user.IUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final IUserService userService;
    public UserController(IUserService userService) {
        this.userService = userService;
    }

//    @GetMapping("/list")
//    public ResponseEntity<?> getUserList() {
//        List<User> users = userService.findAll();
//        return new ResponseEntity<>(users, HttpStatus.OK);
//    }

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
            return ResponseEntity.badRequest().body(registrationResult);
        }
    }

    // Mock method for checking if the username or email is taken
    private boolean isUsernameOrEmailTaken(String username, String email) {
        // Replace with real database logic
        return false; // Assume no conflicts for now
    }
}
