package com.example.md6projecthndn.model.entity.booking;

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
@Table(name = "booking_status")
public class BookingStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, unique = true)
    private BookingStatusEnum status;

    @Column(name = "description", nullable = false)
    private String description;

    @JsonManagedReference("booking_status-booking")
    @OneToMany(mappedBy = "bookingStatus")
    private Set<Booking> bookings;

    public BookingStatus(BookingStatusEnum status, String description) {
        this.status = status;
        this.description = description;
    }
}
