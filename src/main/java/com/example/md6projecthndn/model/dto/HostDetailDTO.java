package com.example.md6projecthndn.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostDetailDTO {
    private String fullName;
    private String phoneNumber;
    private String status;
    private Long userId;
    private String userName;
    private String avatar;
    private String address;
    private double totalRevenue;

    //u.id, u.avatar, u.username, u.full_name, u.phone_number, u.address,double totalRevenue
    public HostDetailDTO(Long userId, String avatar, String userName, String fullName, String phoneNumber, String address, double totalRevenue,String status) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.userId = userId;
        this.userName = userName;
        this.avatar = avatar;
        this.address = address;
        this.totalRevenue = totalRevenue;
    }
}
