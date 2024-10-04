package com.codegym.service.property.propertyImage;

import com.codegym.model.entity.property.PropertyImage;
import com.codegym.service.IGenerateService;

import java.util.Set;

public interface IPropertyImageService extends IGenerateService<PropertyImage> {
    void saveAll(Set<PropertyImage> propertyImages);

    void deleteAllImageByIdProperty( Long id);
}
