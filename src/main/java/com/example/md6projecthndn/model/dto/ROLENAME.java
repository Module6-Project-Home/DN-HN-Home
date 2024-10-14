package com.example.md6projecthndn.model.dto;

public enum ROLENAME {
    ROLE_ADMIN,
    ROLE_USER,
    ROLE_HOST;

    @Override
    public String toString() {
        return this.name(); // Or any specific string representation you want
    }
}
