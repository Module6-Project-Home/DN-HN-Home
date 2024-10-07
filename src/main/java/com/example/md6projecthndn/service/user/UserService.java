package com.example.md6projecthndn.service.user;


import com.example.md6projecthndn.model.entity.user.Role;
import com.example.md6projecthndn.model.entity.user.User;
import com.example.md6projecthndn.model.entity.user.UserStatus;
import com.example.md6projecthndn.model.entity.user.UserStatus.USER_STATUS;
import com.example.md6projecthndn.repository.user.IRoleRepository;
import com.example.md6projecthndn.repository.user.IUserRepository;
import com.example.md6projecthndn.repository.user.IUserStatusRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository; // Repository để lấy Role từ DB
    private final IUserStatusRepository userStatusRepository; // Repository để lấy Status từ DB

    public UserService(IUserRepository userRepository,
                       IRoleRepository roleRepository,
                       IUserStatusRepository userStatusRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userStatusRepository = userStatusRepository;
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(Long id) {

    }


    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public String registerNewUser(User user) {
        // Check if the username or email already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            return "Username is already taken.";
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email is already registered.";
        }

        // Check if password and confirmPassword match
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            return "Passwords do not match.";
        }

//        // Encode the password before saving
//        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Đặt Role mặc định là ROLE_USER
        Role defaultRole = roleRepository.findRoleByName(Role.USER_ROLE.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found.")); // Lấy Role từ DB
        user.setRoles(Set.of(defaultRole)); // Gán role cho người dùng

        // Đặt Status mặc định là ACTIVE
        UserStatus activeStatus = userStatusRepository.findByStatus(UserStatus.USER_STATUS.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Error: Status is not found."));
        user.setUserStatuses(Set.of(activeStatus)); // Gán status cho người dùng

        user.setAvatar("default-user.png");

        // Save the user entity
        userRepository.save(user);

        return "User registered successfully!";
    }

}
