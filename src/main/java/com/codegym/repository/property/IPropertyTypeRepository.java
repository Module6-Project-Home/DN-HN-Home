package com.codegym.repository.property;

import com.codegym.model.entity.property.PropertyType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IPropertyTypeRepository extends CrudRepository<PropertyType, Long> {
    Optional<PropertyType> findByName(String name);

}
