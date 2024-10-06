package com.example.md6projecthndn.service.property.propertyImage;


import com.example.md6projecthndn.model.entity.property.PropertyImage;
import com.example.md6projecthndn.repository.property.IPropertyImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class PropertyImageService implements IPropertyImageService {
    @Autowired
    private IPropertyImageRepository propertyImageRepository;

    @Override
    public Iterable<PropertyImage> findAll() {
        return propertyImageRepository.findAll();
    }

    @Override
    public Optional<PropertyImage> findById(Long id) {
        return propertyImageRepository.findById(id);
    }

    @Override
    public void save(PropertyImage propertyImage) {
        propertyImageRepository.save(propertyImage);
    }

    @Override
    public void delete(Long id) {
        propertyImageRepository.deleteById(id);

    }


    @Override
    public void saveAll(Set<PropertyImage> propertyImages) {
        // Dùng repository để lưu tất cả PropertyImage vào database
        propertyImageRepository.saveAll(propertyImages);
    }


    @Override
    public void deleteAllImageByIdProperty(Long id) {
        propertyImageRepository.deleteAllImageByIdProperty(id);
    }





}
