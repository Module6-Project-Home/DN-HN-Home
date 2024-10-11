package com.example.md6projecthndn.model.entity.property;


import com.example.md6projecthndn.model.entity.booking.Booking;
import com.example.md6projecthndn.model.entity.booking.Review;
import com.example.md6projecthndn.model.entity.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;


import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "properties")
@Builder


public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name; // Tên của căn nhà (*)

    @NotNull
    @ManyToOne
    @JoinColumn(name = "property_type_id", nullable = false)
    private PropertyType propertyType; // Apartment, House, etc.

    @NotNull
    @ManyToOne
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType; // Entire place, Private room, Shared room

    @NotNull
    @Column(nullable = false)
    private String address; // Địa chỉ (*)

    @NotNull
    @Min(1)
    @Max(10)
    @Column(nullable = false)
    private int bedrooms; // Số lượng phòng ngủ (*): 1-10 phòng

    @NotNull
    @Min(1)
    @Max(3)
    @Column(nullable = false)
    private int bathrooms; // Số lượng phòng tắm (*): 1-3 phòng

    @Lob
    private String description; // Mô tả chung: dịch vụ đi kèm, không gian,...

    @NotNull
    @Column(name = "price_per_night", nullable = false)
    private double pricePerNight; // Giá tiền theo ngày (VNĐ) (*)

    @JsonBackReference("user-property")
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private Status status; // Sử dụng bảng Status để quản lý trạng thái

    @OneToMany(mappedBy = "property")
    private Set<Booking> bookings;

    @OneToMany(mappedBy = "property")
    private Set<Review> reviews;

    @OneToMany(mappedBy = "property")
    private Set<PropertyImage> images;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    public void setDefaultValues() {
        if (images == null || images.isEmpty()) {
            // Add default image logic here
        }
    }
}
