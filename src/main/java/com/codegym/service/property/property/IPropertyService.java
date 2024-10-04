package com.codegym.service.property.property;

import com.codegym.model.entity.property.Property;
import com.codegym.model.entity.property.PropertyDTO;
import com.codegym.service.IGenerateService;

import java.util.Optional;

public interface IPropertyService extends IGenerateService<Property> {
    Property addPropertyPost(PropertyDTO propertyDTO);

    Optional<Property> findById(Long id);



}
