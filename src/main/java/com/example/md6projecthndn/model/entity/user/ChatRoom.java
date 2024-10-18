package com.example.md6projecthndn.model.entity.user;

import com.example.md6projecthndn.model.entity.property.Property;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Data
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Người dùng ROLE_USER

    @ManyToOne
    @JoinColumn(name = "host_id", nullable = false)
    private User host;  // Người dùng ROLE_HOST

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;  // Tài sản đang được thảo luận

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ChatMessage> messages = new HashSet<>();  // Danh sách tin nhắn trong cuộc trò chuyện

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors, Getters, Setters
}
