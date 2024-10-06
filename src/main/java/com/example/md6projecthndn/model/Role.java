package com.example.md6projecthndn.model;

import com.example.md6projecthndn.model.enums.ROLE;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING) // Enum sẽ được lưu dưới dạng chuỗi trong cột name
    @Column(length = 20)
    private ROLE name;
}