package com.example.md6projecthndn.service.user.status;

import com.example.md6projecthndn.model.entity.user.UserStatus;
import com.example.md6projecthndn.repository.user.IUserStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserStatusService implements IUserStatusService{
    @Autowired
    IUserStatusRepository userStatusRepository;
    @Override
    public UserStatus save(UserStatus userStatus) {
        return userStatusRepository.save(userStatus);
    }

    @Override
    public Optional<UserStatus> findByStatus(UserStatus.USER_STATUS userStatus) {
        return userStatusRepository.findByStatus(userStatus);
    }
}
