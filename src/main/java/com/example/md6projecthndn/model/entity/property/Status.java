package com.example.md6projecthndn.model.entity.property;



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

    private String name; // Booked, Cancelled, Completed, Available, Unavailable, etc.

    @OneToMany(mappedBy = "status")
    private Set<Property> properties;

}