package com.example.md6projecthndn.model.entity.booking;

public enum BookingStatusEnum {
    PENDING("Chờ nhận phòng"),
    CHECKIN("Đang ở"),
    CHECKOUT("Đã trả phòng"),
    CANCELED("Đã hủy");

    private final String description;

    BookingStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
