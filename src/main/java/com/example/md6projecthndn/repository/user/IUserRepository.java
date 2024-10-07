package com.example.md6projecthndn.repository.user;


import com.example.md6projecthndn.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    User findUserById (Long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
