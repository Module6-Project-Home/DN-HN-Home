package com.codegym.service.property.roomType;

import com.codegym.model.entity.property.RoomType;
import com.codegym.repository.property.IRoomTypeRepository;
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
    public Optional<RoomType> findById(Long id) {
        return roomTypeRepository.findById(id);
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
