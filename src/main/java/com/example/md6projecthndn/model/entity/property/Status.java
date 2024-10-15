package com.example.md6projecthndn.model.entity.property;
import com.example.md6projecthndn.model.entity.user.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "property_status")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "status")
    private Set<Property> properties;

    @Enumerated(EnumType.STRING) // Enum sẽ được lưu dưới dạng chuỗi trong cột name
    private PROPERTY_STATUS name;

    public enum PROPERTY_STATUS {
        VACANT,
        RENTED,
        MAINTENANCE
    }

}