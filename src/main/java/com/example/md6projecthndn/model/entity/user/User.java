package com.example.md6projecthndn.model.entity.user;



import com.example.md6projecthndn.model.entity.booking.Booking;
import com.example.md6projecthndn.model.entity.booking.Review;
import com.example.md6projecthndn.model.entity.property.Property;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "Tên người dùng không được để trống")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Tên người dùng chỉ được chứa chữ cái và số, không có ký tự đặc biệt")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 32, message = "Mật khẩu phải có độ dài từ 6 đến 32 ký tự")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Mật khẩu chỉ được chứa chữ cái và số, không có ký tự đặc biệt")
    private String password;

    private String email;

    private String fullName;

    private String phoneNumber;


    @ManyToMany(fetch = FetchType.EAGER)
    private Set<UserStatus> userStatuses;



    @JsonManagedReference("user-property")
    @OneToMany(mappedBy = "owner")
    private Set<Property> properties;


    @JsonManagedReference("user-booking")
    @OneToMany(mappedBy = "guest")
    private Set<Booking> bookings;



    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;


    @JsonManagedReference("user-review")
    @OneToMany(mappedBy = "guest")
    private Set<Review> reviews;


    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private boolean upgradeRequested;



    public UserStatus.USER_STATUS getCurrentStatus() {
        return userStatuses.stream().findFirst().orElse(null).getStatus();
    }

    public void setCurrentStatus(UserStatus.USER_STATUS newStatus) {
        // Xóa trạng thái hiện tại
        userStatuses.clear();

        // Thêm trạng thái mới
        UserStatus userStatus = new UserStatus();
        userStatus.setStatus(newStatus);
        userStatuses.add(userStatus);
    }



    // Constructors, getters and setters
}

