//package com.example.md6projecthndn.model;
//
//import com.example.md6projecthndn.model.entity.user.Status;
//import jakarta.persistence.*;
//import jakarta.validation.constraints.*;
//import lombok.Data;
//
//import java.util.Set;
//
//@Entity
//@Table(name = "users")
//@Data
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @NotBlank(message = "Username is required")
//    @Column(unique = true)
//    private String username;
//
//    @Email(message = "Email should be valid")
//    @NotBlank(message = "Email is required")
//    @Column(unique = true)
//    private String email;
//
//    @NotBlank(message = "Password is required")
//    @Size(min = 6, max = 32, message = "Password must be between 6 and 32 characters")
//    private String password;
//
//    @Transient
//    @NotBlank(message = "Confirm Password is required")
//    private String confirmPassword;
//
//    @ManyToMany(fetch = FetchType.EAGER) // ManyToMany sẽ tạo thêm 1 bảng nữa
//    private Set<Role> roles;
////    @ManyToMany(fetch = FetchType.EAGER)
//    private Set<Status> statuses;
//
//    private String fullName;
//
//    private String avatar = "default_avatar.png"; // Default avatar
//
//    @NotBlank(message = "Phone number is required")
//    @Pattern(regexp = "^\\d{10}$")
//    private String phoneNumber;
//
//    private String address;
//
//    public void registrationSuccess() {
//        System.out.println("Registration successful! Redirecting to login page...");
//        System.out.println("Filling username: " + this.username);
//    }
//
//}
