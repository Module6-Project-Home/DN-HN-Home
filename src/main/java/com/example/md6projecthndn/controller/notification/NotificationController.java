package com.example.md6projecthndn.controller.notification;

import com.example.md6projecthndn.model.entity.notification.Notification;
import com.example.md6projecthndn.model.entity.notification.NotificationDTO;
import com.example.md6projecthndn.service.notification.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    @Autowired
    private INotificationService notificationService;

    @GetMapping()
    public ResponseEntity<List<NotificationDTO>> getOwnerNotifications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername(); // Lấy username từ UserDetails
        }

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<Notification> notifications = notificationService.findByOwnerUsernameOrderByTimestampDesc(username);
        List<NotificationDTO> notificationDTOs = notifications.stream()
                .map(notification -> new NotificationDTO(
                        notification.getId(),
                        notification.getMessage(),
                        notification.getTimestamp(),
                        notification.getIsRead()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(notificationDTOs);
    }


    @PostMapping("/markAsRead/{id}")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername(); // Lấy username từ UserDetails
        }

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        notificationService.markNotificationAsRead(id);
        return ResponseEntity.ok().build();
    }


}
