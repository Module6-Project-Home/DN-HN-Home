package com.example.md6projecthndn.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDetailDTO {
    private Long id;
    private String avatar;
    private String fullName;
    private String username;
    private String phoneNumber;
    private int userStatus;
    private double totalSpent;

    public UserDetailDTO(Long id, String avatar, String fullName, String username,
                         String phoneNumber, int userStatus, double totalSpent) {
        this.id = id;
        this.avatar = avatar;
        this.fullName = fullName;
        this.username = username;
        this.phoneNumber = phoneNumber;
       this.userStatus = userStatus;
        this.totalSpent = totalSpent;
    }

}

