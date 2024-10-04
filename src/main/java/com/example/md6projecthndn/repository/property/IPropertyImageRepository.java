package com.example.md6projecthndn.repository.property;

import com.example.md6projecthndn.model.entity.property.PropertyImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IPropertyImageRepository extends JpaRepository<PropertyImage, Long> {


    @Query(nativeQuery = true,value = "call deleteAllImageByIdProperty(:id)")
    void deleteAllImageByIdProperty(@Param("id") Long id);
}
