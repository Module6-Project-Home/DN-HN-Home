package com.example.md6projecthndn.service.user.status;

import com.example.md6projecthndn.model.entity.user.UserStatus;

import java.util.Optional;

public interface IUserStatusService {
    UserStatus save(UserStatus userStatus);

    Optional<UserStatus> findByStatus(UserStatus.USER_STATUS userStatus);
}
