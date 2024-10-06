package com.example.md6projecthndn.service.user;

import com.example.md6projecthndn.model.User;
//import com.example.md6projecthndn.model.dto.UserPrinciple;
import com.example.md6projecthndn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

//    @Lazy
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) {
//        User user = userRepository.findByUsername(username);
//        return UserPrinciple.build(user);
//    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public String registerNewUser(User user) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            return "Username is already taken.";
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email is already registered.";
        }

        // Check if passwords match
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            return "Passwords do not match.";
        }

//        // Encode the password before saving
//        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save user to the database
        userRepository.save(user);

        return "User registered successfully.";
    }
}
