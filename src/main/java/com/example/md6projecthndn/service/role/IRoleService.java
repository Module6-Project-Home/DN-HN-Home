package com.example.md6projecthndn.service.role;


import com.example.md6projecthndn.model.entity.user.Role;
import com.example.md6projecthndn.service.IGenerateService;

public interface IRoleService extends IGenerateService<Role> {
    Role findByName(String name);
}
