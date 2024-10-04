package com.codegym.model.entity.booking;

import com.codegym.model.entity.property.Property;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "status")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Booked, Cancelled, Completed, Available, Unavailable, etc.

    @OneToMany(mappedBy = "status")
    private Set<Booking> bookings;


    @OneToMany(mappedBy = "status")
    private Set<Property> properties;

    // Constructors, getters and setters
}
