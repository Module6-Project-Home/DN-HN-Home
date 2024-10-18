package com.example.md6projecthndn.service.notification;


import com.example.md6projecthndn.model.entity.notification.Notification;
import com.example.md6projecthndn.model.entity.user.User;
import com.example.md6projecthndn.repository.notification.INotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService implements INotificationService {
    @Autowired
    private INotificationRepository notificationRepository;

    @Override
    public List<Notification> findByOwnerUsernameOrderByTimestampDesc(String username) {
        return notificationRepository.findByOwnerUsernameOrderByTimestampDesc(username);
    }

    @Override
    public void notifyOwnerOfCancellation(String guestName, String propertyName, User owner) {
        Notification notification = new Notification();
        notification.setOwner(owner);
        notification.setMessage(guestName + " đã hủy thuê " + propertyName + " vào ngày " + LocalDate.now());
        notification.setTimestamp(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    @Override
    public void markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElse(null);
        if (notification != null && !notification.getIsRead()) {
            notification.setIsRead(true);
            notificationRepository.save(notification);
        }
    }

    @Override
    public void notifyOwnerOfBooking(String guestName, String propertyName, User owner) {
        Notification notification = new Notification();
        notification.setOwner(owner);
        notification.setMessage(guestName + " đã đặt thuê " + propertyName + " vào ngày " + LocalDate.now());
        notification.setTimestamp(LocalDateTime.now());
        notificationRepository.save(notification);
    }
}
