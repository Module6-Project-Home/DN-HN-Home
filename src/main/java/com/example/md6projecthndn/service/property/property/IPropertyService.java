package com.example.md6projecthndn.service.property.property;



import com.example.md6projecthndn.model.entity.property.Property;
import com.example.md6projecthndn.model.entity.property.PropertyDTO;
import com.example.md6projecthndn.service.IGenerateService;

import java.util.Optional;

public interface IPropertyService extends IGenerateService<Property> {
    Property addPropertyPost(PropertyDTO propertyDTO);

    Optional<Property> findById(Long id);



}
