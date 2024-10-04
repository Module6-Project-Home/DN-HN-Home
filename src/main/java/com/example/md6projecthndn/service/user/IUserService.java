package com.example.md6projecthndn.service.user;



import com.example.md6projecthndn.model.entity.user.User;
import com.example.md6projecthndn.service.IGenerateService;

import java.util.Optional;

public interface IUserService extends IGenerateService<User> {
    Optional<User> findByUsername(String username);

    User findUserById (Long id);

}
