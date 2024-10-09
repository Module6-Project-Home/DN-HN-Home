package com.example.md6projecthndn.repository.user;

import com.example.md6projecthndn.model.entity.user.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserStatusRepository extends JpaRepository<UserStatus, Long> {

    Optional<UserStatus> findByStatus(UserStatus.USER_STATUS userStatus);
}
