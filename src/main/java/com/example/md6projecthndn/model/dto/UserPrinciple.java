//package com.example.md6projecthndn.model.dto;
//
//import com.example.md6projecthndn.model.Role;
//import com.example.md6projecthndn.model.User;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.io.Serial;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//public class UserPrinciple implements UserDetails {
//
//    @Serial
//    private static final long serialVersionUID = 1L;
//
//    private String username;
//    private String password;
//    private Collection<? extends GrantedAuthority> authorities;
//
//    public UserPrinciple(String username, String password, Collection<? extends GrantedAuthority> authorities) {
//        this.username = username;
//        this.password = password;
//        this.authorities = authorities;
//    }
//
//    public static UserPrinciple build(User user) {
//        List<GrantedAuthority> author = new ArrayList<>();
//        for (Role role : user.getRoles()) {
//            author.add(new SimpleGrantedAuthority(role.getName().name()));
//        }
//        return new UserPrinciple(user.getUsername(), user.getPassword(), author);
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
