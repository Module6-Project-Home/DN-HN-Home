package com.example.md6projecthndn.controller.property;


import com.example.md6projecthndn.model.entity.property.RoomType;
import com.example.md6projecthndn.service.property.roomType.IRoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/room-types") // Đường dẫn gốc cho controller
public class RoomTypeController {
    @Autowired
    private IRoomTypeService roomTypeService;

    @GetMapping
    public ResponseEntity<Iterable<RoomType>> getAllRoomTypes() {
        Iterable<RoomType> propertyTypes = roomTypeService.findAll();
        return ResponseEntity.ok(propertyTypes);
    }
}
