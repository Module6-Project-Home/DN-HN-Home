package com.example.md6projecthndn.service.notification;

import com.example.md6projecthndn.model.entity.notification.Notification;
import com.example.md6projecthndn.model.entity.user.User;

import java.util.List;

public interface INotificationService {
    List<Notification> findByOwnerUsernameOrderByTimestampDesc(String username);


    public void notifyOwnerOfCancellation(String guestName, String propertyName, User owner);

    public void markNotificationAsRead(Long notificationId);

}
