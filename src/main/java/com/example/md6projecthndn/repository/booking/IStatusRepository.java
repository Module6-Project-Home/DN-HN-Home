package com.example.md6projecthndn.repository.booking;

import com.example.md6projecthndn.model.entity.property.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IStatusRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByName(String name); // Tìm Status theo tên
}
