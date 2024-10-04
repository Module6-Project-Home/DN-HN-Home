package com.example.md6projecthndn.model.entity.property;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "property_images")
public class PropertyImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;


}

