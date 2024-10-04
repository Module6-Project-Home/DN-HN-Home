package com.codegym.repository.booking;

import com.codegym.model.entity.booking.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IStatusRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByName(String name); // Tìm Status theo tên
}
