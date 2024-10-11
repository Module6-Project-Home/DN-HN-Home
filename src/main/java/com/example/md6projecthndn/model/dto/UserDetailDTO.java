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
    private String username;
    private String fullName;
    private String phoneNumber;
    private int userStatus;
    private double totalSpent;

    public UserDetailDTO(Long id, String avatar, String username, String fullName,
                         String phoneNumber, int userStatus, double totalSpent) {
        this.id = id;
        this.avatar = avatar;
        this.username = username;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
       this.userStatus = userStatus;
        this.totalSpent = totalSpent;
    }

}

