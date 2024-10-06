package com.example.md6projecthndn.service.user;

import com.example.md6projecthndn.model.User;
import com.example.md6projecthndn.service.GenerateService;
import org.springframework.security.core.userdetails.UserDetails;

public interface IUserService extends GenerateService<User> {
//    UserDetails loadUserByUsername(String username);

    User findByUsername(String username);

    String registerNewUser(User user);
}
