package com.example.md6projecthndn.repository.property;


import com.example.md6projecthndn.model.dto.PropertyRevenueDTO;
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
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
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

    @Query(value = "SELECT p.id AS propertyId, " +
            "p.name AS propertyName, " +
            "p.address, " +
            "p.price_per_night AS pricePerNight, " +
            "CASE " +
            "   WHEN s.id = 1 THEN 'MAINTENANCE' " +
            "   WHEN s.id = 2 THEN 'RENTED' " +
            "   ELSE 'VACANT' " +
            "END AS status, " +
            "COALESCE(SUM(CASE WHEN b.booking_status_id = 3 THEN p.price_per_night * DATEDIFF(b.check_out_date, b.check_in_date) ELSE 0 END), 0) AS revenue, " +
            "u.username AS owner " +
            "FROM properties p " +
            "LEFT JOIN bookings b ON p.id = b.property_id " +
            "LEFT JOIN property_status s ON p.status_id = s.id " +
            "JOIN users u ON p.owner_id = u.id " +
            "WHERE u.username = :username " +
            "GROUP BY p.id",
            nativeQuery = true)
    List<Object[]> getPropertyRevenueDetails(@Param("username") String username);

    @Query(value = "SELECT YEAR(b.check_out_date) AS year, " +
            "MONTH(b.check_out_date) AS month, " +
            "COALESCE(SUM(CASE WHEN b.booking_status_id = 3 THEN p.price_per_night * DATEDIFF(b.check_out_date, b.check_in_date) ELSE 0 END), 0) AS revenue " +
            "FROM properties p " +
            "LEFT JOIN bookings b ON p.id = b.property_id " +
            "LEFT JOIN property_status s ON p.status_id = s.id " +
            "JOIN users u ON p.owner_id = u.id " +
            "WHERE u.username = :username " +
            "AND b.check_out_date BETWEEN :start_date AND :end_date " +
            "GROUP BY YEAR(b.check_out_date), MONTH(b.check_out_date)",
            nativeQuery = true)
    List<Object[]> getMonthlyRevenueByOwner(@Param("username") String username,
                                            @Param("start_date") Date startDate,
                                            @Param("end_date") Date endDate);
}
