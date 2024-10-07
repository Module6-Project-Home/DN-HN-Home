package com.example.md6projecthndn.model.dto;

import com.example.md6projecthndn.model.entity.user.Role;
import com.example.md6projecthndn.model.entity.user.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrinciple implements UserDetails {
//Chỉ để đọc và để tạo 1 lần thôi
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrinciple(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }
    //    chuyen tu user trong model -> User co kha nang phan quyen UserPrinciple,chuyeen doi tu user sang userDetail
    public static UserPrinciple build(User user) {
//        quyen de xac thuc -> GrantedAuthority
        List<GrantedAuthority> author = new ArrayList<>();
        for (Role role : user.getRoles()) {
            author.add(new SimpleGrantedAuthority(role.getName().name()));
        }
        return new UserPrinciple(user.getUsername(), user.getPassword(), author);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
