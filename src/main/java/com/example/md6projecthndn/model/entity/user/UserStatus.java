package com.example.md6projecthndn.model.entity.user;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "status")
@Data
public class UserStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING) // Enum sẽ được lưu dưới dạng chuỗi trong cột name
    private USER_STATUS status;

    public enum USER_STATUS {
        ACTIVE,
        SUSPENDED
    }
}


