package com.example.md6projecthndn.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostDetailDTO {
    private Long id;
    private String avatar;
    private String username;
    private String fullName;
    private String phoneNumber;
    private String address;
    private Double totalRevenue;
    private Integer propertyCount;
    private String status;

    //u.id, u.avatar, u.username, u.full_name, u.phone_number, u.address,double totalRevenue
    public HostDetailDTO(Long id, String avatar, String username, String fullName, String phoneNumber, String address, Double totalRevenue, Integer propertyCount, String status) {
        this.id = id;
        this.avatar = avatar;
        this.username = username;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.totalRevenue = totalRevenue;
        this.propertyCount = propertyCount;
        this.status = status;
    }
}
