package com.example.md6projecthndn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserProfileDTO {
    private String username;
    private String fullName;
    private String avatar;
    private String address;
    private String phoneNumber;
}
