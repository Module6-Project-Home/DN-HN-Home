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

    private String username;

    private String password;

    private String confirmPassword;

    private String email;

    private String fullName;

    private String phoneNumber;

    @JsonManagedReference
    @OneToMany(mappedBy = "owner")
    private Set<Property> properties;

    @OneToMany(mappedBy = "guest")
    private Set<Booking> bookings;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @OneToMany(mappedBy = "guest")
    private Set<Review> reviews;


    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Constructors, getters and setters
}

