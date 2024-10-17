package com.example.md6projecthndn.model.entity.user;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Data
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dialog_id", nullable = false)
    private Dialog dialog;  // Cuộc trò chuyện mà tin nhắn thuộc về

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;  // Người gửi tin nhắn, có thể là ROLE_USER hoặc ROLE_HOST

    @Lob
    @Column(nullable = false)
    private String content;  // Nội dung tin nhắn

    @CreationTimestamp
    @Column(name = "sent_at", nullable = false, updatable = false)
    private LocalDateTime sentAt;  // Thời gian gửi tin nhắn

    // Constructors, Getters, Setters
}
