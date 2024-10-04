package com.example.md6projecthndn.repository.property;


import com.example.md6projecthndn.model.entity.property.Property;
import com.example.md6projecthndn.model.entity.property.PropertyType;
import com.example.md6projecthndn.model.entity.property.RoomType;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IPropertyRepository extends JpaRepository<Property, Long> {

    @Query(nativeQuery = true, value = "SELECT p.*, pi.image_url FROM properties p JOIN property_images pi ON pi.property_id = p.id")
    List<Property> findAllPropertyHaveImage();

    Page<Property> findAllByAddressContaining(String address, Pageable pageable);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "CALL deleteProperty(:id)")
    void deletePropertyById(@Param("id") Long id);


    Page<Property> findAllByPropertyTypeAndAddressContaining(PropertyType propertyType, String address, Pageable pageable);
    Page<Property> findAllByRoomTypeAndAddressContaining(RoomType roomType, String address, Pageable pageable);

    Page<Property> findPropertyByOwnerId(Long ownerId, Pageable pageable);


    Page<Property> findAllByPropertyType(PropertyType propertyType, Pageable pageable);
    Page<Property> findAllByRoomType(RoomType roomType, Pageable pageable);


    @Query("SELECT p FROM Property p WHERE p.bedrooms >= :bedrooms " +
            "AND p.propertyType = :propertyType " +
            "AND p.bathrooms >= :bathrooms " +
            "AND p.address LIKE %:address% " +
            "AND p.pricePerNight BETWEEN :minPrice AND :maxPrice " +
            "AND NOT EXISTS (SELECT b FROM Booking b WHERE b.property.id = p.id " +
            "AND b.checkInDate <= :checkOutDate " +
            "AND b.checkOutDate >= :checkInDate)")
    Page<Property> findAvailableProperties(@Param("bedrooms") int bedrooms,
                                           @Param("propertyType") PropertyType propertyType,
                                           @Param("bathrooms") int bathrooms,
                                           @Param("address") String address,
                                           @Param("minPrice") double minPrice,
                                           @Param("maxPrice") double maxPrice,
                                           @Param("checkInDate") LocalDate checkInDate,
                                           @Param("checkOutDate") LocalDate checkOutDate,
                                           Pageable pageable);

    @Query("SELECT p FROM Property p WHERE p.bedrooms >= :bedrooms " +
            "AND p.bathrooms >= :bathrooms " +
            "AND p.address LIKE %:address% " +
            "AND p.pricePerNight BETWEEN :minPrice AND :maxPrice " +
            "AND NOT EXISTS (SELECT b FROM Booking b WHERE b.property.id = p.id " +
            "AND b.checkInDate <= :checkOutDate " +
            "AND b.checkOutDate >= :checkInDate)")
    Page<Property> findAvailablePropertiesNotType(@Param("bedrooms") int bedrooms,
                                           @Param("bathrooms") int bathrooms,
                                           @Param("address") String address,
                                           @Param("minPrice") double minPrice,
                                           @Param("maxPrice") double maxPrice,
                                           @Param("checkInDate") LocalDate checkInDate,
                                           @Param("checkOutDate") LocalDate checkOutDate,
                                           Pageable pageable);





    @Query(nativeQuery = true, value = "SELECT MAX(p.price_per_night) FROM properties p")
    Double getMaxPrice();

}
