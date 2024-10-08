package com.example.md6projecthndn.model.entity.booking;


import com.example.md6projecthndn.model.entity.property.Property;
import com.example.md6projecthndn.model.entity.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference("user-review")
    @ManyToOne
    @JoinColumn(name = "guest_id")
    private User guest;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    private int rating; // Scale 1 to 5

    @Lob
    private String comment;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Constructors, getters and setters
}