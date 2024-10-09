package com.example.md6projecthndn.service.user;



import com.example.md6projecthndn.model.dto.ROLENAME;
import com.example.md6projecthndn.model.dto.UserDTO;
import com.example.md6projecthndn.model.entity.user.User;
import com.example.md6projecthndn.service.IGenerateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface IUserService extends IGenerateService<User> {
//    Optional<User> findByUsername(String username);

    User findByUsername(String username);

    UserDetails loadUserByUsername(String username);

    List<User> getUsersByRole(ROLENAME role);

    String registerNewUser(User user);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
    Page<User> getUsersByRole_Name(ROLENAME rolename, PageRequest of);
}
