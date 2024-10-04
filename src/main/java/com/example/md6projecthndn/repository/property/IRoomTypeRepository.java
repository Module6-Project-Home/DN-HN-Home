package com.example.md6projecthndn.repository.property;


import com.example.md6projecthndn.model.entity.property.RoomType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IRoomTypeRepository extends CrudRepository<RoomType, Long> {
    Optional<RoomType> findByName(String name);

}
