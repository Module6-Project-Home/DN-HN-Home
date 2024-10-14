package com.example.md6projecthndn.repository.property;


import com.example.md6projecthndn.model.dto.PropertyTopBookingDTO;
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


    @Query("SELECT p FROM Property p WHERE "
            + "(:name IS NULL OR p.name LIKE %:name%) AND "
            + "(:address IS NULL OR p.address LIKE %:address%) AND "
            + "(:minPrice IS NULL OR p.pricePerNight >= :minPrice) AND "
            + "(:maxPrice IS NULL OR p.pricePerNight <= :maxPrice) AND "
            + "(:propertyType IS NULL OR p.propertyType = :propertyType) AND "
            + "(:roomType IS NULL OR p.roomType = :roomType) AND "
            + "(:minBedrooms IS NULL OR p.bedrooms >= :minBedrooms) AND "
            + "(:maxBedrooms IS NULL OR p.bedrooms <= :maxBedrooms) AND "
            + "(:minBathrooms IS NULL OR p.bathrooms >= :minBathrooms) AND "
            + "(:maxBathrooms IS NULL OR p.bathrooms <= :maxBathrooms) AND "
            + "(:checkInDate IS NULL OR :checkOutDate IS NULL OR NOT EXISTS ("
            + "    SELECT b FROM Booking b WHERE b.property.id = p.id "
            + "    AND b.checkInDate <= :checkOutDate "
            + "    AND b.checkOutDate >= :checkInDate))")
    Iterable<Property> searchProperties(
            @Param("name") String name,
            @Param("address") String address,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("propertyType") PropertyType propertyType,
            @Param("roomType") RoomType roomType,
            @Param("minBedrooms") Integer minBedrooms,
            @Param("maxBedrooms") Integer maxBedrooms,
            @Param("minBathrooms") Integer minBathrooms,
            @Param("maxBathrooms") Integer maxBathrooms,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate
    );

    List<Property> findByOwnerUsername(String username);




    List<Property> findByOwnerId(Long ownerId);

    @Query(nativeQuery = true, value = "SELECT p.id as id, p.name as name, p.price_per_night as pricePerNight, p.address as address, MIN(pi.image_url) as imageUrl " +
            "FROM properties p " +
            "JOIN bookings b ON p.id = b.property_id " +
            "JOIN property_images pi ON p.id = pi.property_id " +
            "GROUP BY p.id " +
            "LIMIT 5")
    List<Object[]> findPropertyTopBookingDTO();


    Page<Property> findByOwnerUsername(String username, Pageable pageable);


    Long countByOwnerId(Long ownerId);

}
