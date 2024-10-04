package com.example.md6projecthndn.service.property.propertyImage;



import com.example.md6projecthndn.model.entity.property.PropertyImage;
import com.example.md6projecthndn.service.IGenerateService;

import java.util.Set;

public interface IPropertyImageService extends IGenerateService<PropertyImage> {
    void saveAll(Set<PropertyImage> propertyImages);

    void deleteAllImageByIdProperty( Long id);
}
