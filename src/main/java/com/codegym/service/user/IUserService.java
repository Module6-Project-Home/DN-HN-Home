package com.codegym.service.user;

import com.codegym.model.entity.user.User;
import com.codegym.service.IGenerateService;

import java.util.Optional;

public interface IUserService extends IGenerateService<User> {
    Optional<User> findByUsername(String username);

    User findUserById (Long id);

}
