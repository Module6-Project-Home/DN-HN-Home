
SELECT p.*
FROM properties p
         LEFT JOIN property_type pt ON p.property_type_id = pt.id
         LEFT JOIN room_type rt ON p.room_type_id = rt.id
WHERE (p.name LIKE CONCAT('%', :name, '%') OR :name IS NULL)
  AND (p.address LIKE CONCAT('%', :address, '%') OR :address IS NULL)
  AND (p.price_per_night >= :minPrice OR :minPrice IS NULL)
  AND (p.price_per_night <= :maxPrice OR :maxPrice IS NULL)
  AND (p.property_type_id = :propertyType OR :propertyType IS NULL)
  AND (p.room_type_id = :roomType OR :roomType IS NULL)
  AND (p.bedrooms >= :minBedrooms OR :minBedrooms IS NULL)
  AND (p.bedrooms <= :maxBedrooms OR :maxBedrooms IS NULL)
  AND (p.bathrooms >= :minBathrooms OR :minBathrooms IS NULL)
  AND (p.bathrooms <= :maxBathrooms OR :maxBathrooms IS NULL)
  AND (
    (:checkInDate IS NULL OR :checkOutDate IS NULL) OR
    NOT EXISTS (
        SELECT 1
        FROM bookings b
        WHERE b.property_id = p.id
          AND b.check_in_date <= :checkOutDate
          AND b.check_out_date >= :checkInDate
    )
    );
