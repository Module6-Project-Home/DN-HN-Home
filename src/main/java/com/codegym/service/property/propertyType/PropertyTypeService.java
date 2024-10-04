package com.codegym.service.property.propertyType;


import com.codegym.model.entity.property.PropertyType;
import com.codegym.repository.property.IPropertyTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PropertyTypeService implements IPropertyTypeService {
    @Autowired
    private IPropertyTypeRepository propertyTypeRepository;

    @Override
    public Iterable<PropertyType> findAll() {
        return propertyTypeRepository.findAll();
    }

    @Override
    public Optional<PropertyType> findById(Long id) {
        return propertyTypeRepository.findById(id);
    }

    @Override
    public void save(PropertyType propertyType) {
        propertyTypeRepository.save(propertyType);
    }

    @Override
    public void delete(Long id) {
        propertyTypeRepository.deleteById(id);
    }
}
