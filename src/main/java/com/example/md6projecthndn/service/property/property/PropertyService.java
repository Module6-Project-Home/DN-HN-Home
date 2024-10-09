package com.example.md6projecthndn.service.property.property;


import com.example.md6projecthndn.model.dto.PropertyDetailDTO;
import com.example.md6projecthndn.model.dto.PropertyTopBookingDTO;
import com.example.md6projecthndn.model.dto.ReviewDTO;
import com.example.md6projecthndn.model.dto.PropertyImageDTO;
import com.example.md6projecthndn.model.entity.property.*;
import com.example.md6projecthndn.repository.booking.IStatusRepository;
import com.example.md6projecthndn.repository.property.IPropertyImageRepository;
import com.example.md6projecthndn.repository.property.IPropertyRepository;
import com.example.md6projecthndn.repository.property.IPropertyTypeRepository;
import com.example.md6projecthndn.repository.property.IRoomTypeRepository;
import com.example.md6projecthndn.repository.user.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PropertyService implements IPropertyService {

    @Autowired
    private IPropertyRepository propertyRepository;

    @Autowired
    private IPropertyImageRepository imageRepository;

    @Autowired
    private IPropertyTypeRepository propertyTypeRepository; // Thêm repository cho PropertyType

    @Autowired
    private IRoomTypeRepository roomTypeRepository; // Thêm repository cho RoomType

    @Autowired
    private IUserRepository userRepository; // Thêm repository cho PropertyType

    @Autowired
    private IStatusRepository statusRepository; // Thêm repository cho RoomType


    public Property addPropertyPost(PropertyDTO propertyDTO) {
        // Tạo mới Property từ DTO trước
        Property property = Property.builder()
                .name(propertyDTO.getName())
                .propertyType(propertyTypeRepository.findByName(propertyDTO.getPropertyType())
                        .orElseThrow(() -> new RuntimeException("Property type not found")))
                .roomType(roomTypeRepository.findByName(propertyDTO.getRoomType())
                        .orElseThrow(() -> new RuntimeException("Room type not found")))
                .address(propertyDTO.getAddress())
                .bedrooms(propertyDTO.getBedrooms())
                .bathrooms(propertyDTO.getBathrooms())
                .description(propertyDTO.getDescription())
                .pricePerNight(propertyDTO.getPricePerNight())
                .status(statusRepository.findByName(propertyDTO.getStatus())
                        .orElseThrow(() -> new RuntimeException("Status not found")))
                .owner(userRepository.findByUsername(propertyDTO.getOwner()))
                .build();

        // Lưu Property vào database trước
        property = propertyRepository.save(property);

        // Sau khi Property đã có ID, lưu các hình ảnh với liên kết Property
        Set<PropertyImage> images = new HashSet<>();

        if (propertyDTO.getImageUrls() != null) {
            for (String imageUrl : propertyDTO.getImageUrls()) {
                PropertyImage image = PropertyImage.builder()
                        .imageUrl(imageUrl)
                        .property(property)  // Liên kết Property với từng hình ảnh
                        .build();
                images.add(image);
            }

            // Lưu tất cả hình ảnh
            imageRepository.saveAll(images);
        } else {
            throw new RuntimeException("Image URLs are required");
        }

        // Gán danh sách ảnh đã lưu vào Property
        property.setImages(images);

        // Trả về Property đã được lưu cùng hình ảnh
        return property;
    }


    @Override
    public Property findById(Long id) {
        return propertyRepository.findById(id).orElse(null);
    }






    @Override
    public Iterable<Property> findAll() {
        return propertyRepository.findAll();
    }

    @Override
    public void save(Property property) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
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
                                           LocalDate checkInDate,
                                           LocalDate checkOutDate) {
        return propertyRepository.searchProperties(
                name, address, minPrice, maxPrice, propertyType, roomType,
                minBedrooms, maxBedrooms, minBathrooms, maxBathrooms,
                checkInDate, checkOutDate);
    }

    @Override
    public List<Property> findByOwnerId(Long ownerId) {
        return propertyRepository.findByOwnerId(ownerId);
    }

    @Override
    public PropertyDetailDTO findPropertyById(Long id) {
        Optional<Property> propertyOptional = propertyRepository.findById(id);

        if (propertyOptional.isPresent()) {
            Property property = propertyOptional.get();
            PropertyDetailDTO propertyDTO = new PropertyDetailDTO();

            propertyDTO.setId(property.getId());
            propertyDTO.setName(property.getName());
            propertyDTO.setAddress(property.getAddress());
            propertyDTO.setBathrooms(property.getBathrooms());
            propertyDTO.setBedrooms(property.getBedrooms());
            propertyDTO.setDescription(property.getDescription());
            propertyDTO.setPricePerNight(property.getPricePerNight());

            Set<PropertyImageDTO> imageDTOs = property.getImages().stream().map(image -> {
                PropertyImageDTO imageDTO = new PropertyImageDTO();
                imageDTO.setId(image.getId());
                imageDTO.setImageUrl(image.getImageUrl());
                return imageDTO;
            }).collect(Collectors.toSet());
            propertyDTO.setImages(imageDTOs);

            List<ReviewDTO> reviews = property.getReviews().stream().map(review -> {
                ReviewDTO reviewDTO = new ReviewDTO();
                reviewDTO.setId(review.getId());
                reviewDTO.setRating(review.getRating());
                reviewDTO.setComment(review.getComment());
                reviewDTO.setGuest(review.getGuest().getUsername());
                return reviewDTO;
            }).collect(Collectors.toList());
            propertyDTO.setReviews(reviews);

            return propertyDTO;
        }

        return null;
    }

    @Override
    public List<PropertyTopBookingDTO> findPropertyTopBookingDTO() {
        List<Object[]> results = propertyRepository.findPropertyTopBookingDTO();
        List<PropertyTopBookingDTO> properties = new ArrayList<>();

        for (Object[] result : results) {
            PropertyTopBookingDTO dto = new PropertyTopBookingDTO(
                    ((Number) result[0]).longValue(), // id
                    (String) result[1],               // name
                    ((Number) result[2]).doubleValue(),// pricePerNight
                    (String) result[3],               // address
                    (String) result[4]                // imageUrl
            );
            properties.add(dto);
        }

        return properties;
    }

}
