package com.example.md6projecthndn.service.property.roomType;


import com.example.md6projecthndn.model.entity.property.RoomType;
import com.example.md6projecthndn.repository.property.IRoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoomTypeService implements IRoomTypeService {
    @Autowired
    private IRoomTypeRepository roomTypeRepository;

    @Override
    public Iterable<RoomType> findAll() {
        return roomTypeRepository.findAll();
    }

    @Override
    public RoomType findById(Long id) {
        return roomTypeRepository.findById(id).orElse(null);
    }

    @Override
    public void save(RoomType roomType) {
        roomTypeRepository.save(roomType);
    }

    @Override
    public void delete(Long id) {
        roomTypeRepository.deleteById(id);
    }
}
