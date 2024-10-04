package com.example.md6projecthndn.service.property.property;


import com.example.md6projecthndn.model.entity.property.Property;
import com.example.md6projecthndn.model.entity.property.PropertyDTO;
import com.example.md6projecthndn.model.entity.property.PropertyImage;
import com.example.md6projecthndn.repository.booking.IStatusRepository;
import com.example.md6projecthndn.repository.property.IPropertyImageRepository;
import com.example.md6projecthndn.repository.property.IPropertyRepository;
import com.example.md6projecthndn.repository.property.IPropertyTypeRepository;
import com.example.md6projecthndn.repository.property.IRoomTypeRepository;
import com.example.md6projecthndn.repository.user.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
                .owner(userRepository.findByUsername(propertyDTO.getOwner())
                        .orElseThrow(() -> new RuntimeException("Owner not found")))
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
    public Optional<Property> findById(Long id) {
        return propertyRepository.findById(id);
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




    // Cập nhật thông tin bất động sản
    @Transactional
    public Property updateProperty(Long id, PropertyDTO propertyDTO) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bất động sản không tồn tại"));

        property.setName(propertyDTO.getName());
        property.setPropertyType(propertyTypeRepository.findByName(propertyDTO.getPropertyType()).orElse(null));
        property.setRoomType(roomTypeRepository.findByName(propertyDTO.getRoomType()).orElse(null));
        property.setAddress(propertyDTO.getAddress());
        property.setBedrooms(propertyDTO.getBedrooms());
        property.setBathrooms(propertyDTO.getBathrooms());
        property.setDescription(propertyDTO.getDescription());
        property.setPricePerNight(propertyDTO.getPricePerNight());
        property.setOwner(userRepository.findByUsername(propertyDTO.getOwner()).orElse(null));
        property.setStatus(statusRepository.findByName(propertyDTO.getStatus()).orElse(null));
        return propertyRepository.save(property);
    }



    // Xóa bất động sản
    @Transactional
    public void deleteProperty(Long id) {
        propertyRepository.deleteById(id);
    }
}
