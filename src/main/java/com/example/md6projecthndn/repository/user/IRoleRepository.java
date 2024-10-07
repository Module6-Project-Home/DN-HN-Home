package com.example.md6projecthndn.repository.user;


import com.example.md6projecthndn.model.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);

    Optional<Role> findRoleByName(Role.USER_ROLE name);
}
