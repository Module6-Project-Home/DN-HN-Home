package com.example.md6projecthndn.repository.property;

import com.example.md6projecthndn.model.entity.property.Property;
import com.example.md6projecthndn.model.entity.property.PropertyImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface IPropertyImageRepository extends JpaRepository<PropertyImage, Long> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM property_images WHERE property_id = :propertyId")
    void deleteByProperty(@Param("propertyId") Long propertyId);
}
