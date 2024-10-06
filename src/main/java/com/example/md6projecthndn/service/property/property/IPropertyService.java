package com.example.md6projecthndn.service.property.property;



import com.example.md6projecthndn.model.entity.property.Property;
import com.example.md6projecthndn.model.entity.property.PropertyDTO;
import com.example.md6projecthndn.model.entity.property.PropertyType;
import com.example.md6projecthndn.model.entity.property.RoomType;
import com.example.md6projecthndn.service.IGenerateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IPropertyService extends IGenerateService<Property> {
    Property addPropertyPost(PropertyDTO propertyDTO);

    Optional<Property> findById(Long id);

    public Page<Property> searchProperties(String name,
                                           String address,
                                           Double minPrice,
                                           Double maxPrice,
                                           PropertyType propertyType,
                                           RoomType roomType,
                                           Integer minBedrooms,
                                           Integer maxBedrooms,
                                           Integer minBathrooms,
                                           Integer maxBathrooms,
                                           LocalDate checkInDate,    // Thêm tham số này
                                           LocalDate checkOutDate,   // Thêm tham số này
                                           Pageable pageable);

    List<Property> findByOwnerId(Long ownerId);

}
