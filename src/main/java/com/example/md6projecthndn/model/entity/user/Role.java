package com.example.md6projecthndn.model.entity.user;


import com.example.md6projecthndn.model.dto.ROLENAME;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING) // Enum sẽ được lưu dưới dạng chuỗi trong cột name
    @Column(length = 20)
    private ROLENAME name;

//    public enum USER_ROLE {
//        ROLE_ADMIN,
//        ROLE_USER,
//        ROLE_HOST
//    }
    // Constructors, getters and setters
}
