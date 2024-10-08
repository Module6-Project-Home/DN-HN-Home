package com.example.md6projecthndn.controller;


import com.example.md6projecthndn.model.entity.property.PropertyType;
import com.example.md6projecthndn.service.property.propertyType.IPropertyTypeService;
import com.example.md6projecthndn.service.property.propertyType.PropertyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api/property-types") // Đường dẫn gốc cho controller
public class PropertyTypeController {
    @Autowired
    private IPropertyTypeService propertyTypeService;

    @GetMapping
    public ResponseEntity<Iterable<PropertyType>> getAllPropertyTypes() {
        Iterable<PropertyType> propertyTypes = propertyTypeService.findAll();
        return ResponseEntity.ok(propertyTypes);
    }

}
