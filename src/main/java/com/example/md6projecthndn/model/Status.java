package com.example.md6projecthndn.model;

import com.example.md6projecthndn.model.enums.STATUS;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "status")
@Data
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING) // Enum sẽ được lưu dưới dạng chuỗi trong cột name
    private STATUS status;
}
