package com.example.md6projecthndn.repository.property;

import com.example.md6projecthndn.model.entity.property.PropertyType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IPropertyTypeRepository extends CrudRepository<PropertyType, Long> {
    Optional<PropertyType> findByName(String name);

}
