package com.example.md6projecthndn.service.property.property;



import com.example.md6projecthndn.model.dto.PropertyDetailDTO;
import com.example.md6projecthndn.model.dto.PropertyTopBookingDTO;
import com.example.md6projecthndn.model.entity.property.Property;
import com.example.md6projecthndn.model.entity.property.PropertyDTO;
import com.example.md6projecthndn.model.entity.property.PropertyType;
import com.example.md6projecthndn.model.entity.property.RoomType;
import com.example.md6projecthndn.service.IGenerateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IPropertyService extends IGenerateService<Property> {
    Property addPropertyPost(PropertyDTO propertyDTO);

    Property findById(Long id);


    public Iterable<Property> searchProperties(String name,
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
                                           LocalDate checkOutDate   // Thêm tham số này
                                          );

    List<Property> findByOwnerId(Long ownerId);

    PropertyDetailDTO findPropertyById(@Param("id") Long id);

    List<PropertyTopBookingDTO> findPropertyTopBookingDTO();

    List<Property> findByOwnerUsername(String username);


    Long countByOwnerId(Long ownerId);
}
