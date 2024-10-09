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


    // Constructors, getters, and setters

    public UserDTO(Long userId, String fullName, String phoneNumber, String status) {
        this.userId = userId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }



}
