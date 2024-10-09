package com.example.md6projecthndn.model.entity.booking;


import com.example.md6projecthndn.model.entity.property.Property;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @JsonManagedReference("status-booking")
    @OneToMany(mappedBy = "status")
    private Set<Booking> bookings;


    @OneToMany(mappedBy = "status")
    private Set<Property> properties;

    // Constructors, getters and setters
}
