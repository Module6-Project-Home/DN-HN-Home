package com.codegym.service.role;

import com.codegym.model.entity.user.Role;
import com.codegym.service.IGenerateService;

public interface IRoleService extends IGenerateService<Role> {
    Role findByName(String name);
}
