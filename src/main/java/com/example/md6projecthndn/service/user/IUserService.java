package com.example.md6projecthndn.service.user;



import com.example.md6projecthndn.model.dto.*;
import com.example.md6projecthndn.model.entity.property.Property;
import com.example.md6projecthndn.model.entity.user.User;
import com.example.md6projecthndn.service.IGenerateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface IUserService extends IGenerateService<User> {
    Optional<User> findUserByName(String username);

    User findByUsername(String username);

    UserDetails loadUserByUsername(String username);

    List<User> getUsersByRole(ROLENAME role);

    String registerNewUser(User user);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    List<User> findUsersRequestingUpgrade();
    Page<User> getUsersByRole_Name(ROLENAME rolename, PageRequest of);

    void updateUser(User currentUser, UserProfileDTO userProfileDTO);

    boolean changePassword(User user, String oldPassword, String newPassword);

    User findByEmail(String email);

    UserDetailDTO getUserDetails(Long id);

}
