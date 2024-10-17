package com.example.md6projecthndn.repository.notification;

import com.example.md6projecthndn.model.entity.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface INotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByOwnerUsernameOrderByTimestampDesc(String username);
}