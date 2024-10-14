package com.example.md6projecthndn.model.dto;

import com.example.md6projecthndn.model.entity.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {
    public UserProfileDTO toDTO(User user) {
        return new UserProfileDTO(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getAvatar(),
                user.getAddress(),
                user.getPhoneNumber()
        );
    }
}
