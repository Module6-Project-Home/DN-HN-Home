package com.example.md6projecthndn.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String fullName;
    private String phoneNumber;
    private String status;
    private Long userId;
    private String userName;
    private String avatar;
    private String address;
    private boolean upgradeRequested;


    // Constructors, getters, and setters

    public UserDTO(String fullName, String phoneNumber, String status, Long userId, String userName,String avatar,String address, boolean upgradeRequested) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.userId = userId;
        this.userName = userName;
        this.avatar = avatar;
        this.address = address;
        this.upgradeRequested = upgradeRequested;
    }



}
