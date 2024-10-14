package com.example.md6projecthndn.service.user;


import com.example.md6projecthndn.model.dto.ROLENAME;
import com.example.md6projecthndn.model.dto.UserDTO;
import com.example.md6projecthndn.model.dto.UserDetailDTO;
import com.example.md6projecthndn.model.dto.UserPrinciple;
import com.example.md6projecthndn.model.dto.*;
import com.example.md6projecthndn.model.entity.user.Role;
import com.example.md6projecthndn.model.entity.user.User;
import com.example.md6projecthndn.model.entity.user.UserStatus;
import com.example.md6projecthndn.repository.user.IRoleRepository;
import com.example.md6projecthndn.repository.user.IUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.example.md6projecthndn.repository.user.IUserStatusRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements IUserService, UserDetailsService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository; // Repository để lấy Role từ DB
    private final IUserStatusRepository userStatusRepository; // Repository để lấy Status từ DB

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Optional<User> findUserByName(String username){
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        return UserPrinciple.build(user);
    }

    @Override
    public List<User> getUsersByRole(ROLENAME role) {
        return userRepository.findByRoles_Name(role);
    }

    @Override
    public Page<User> getUsersByRole_Name(ROLENAME rolename, PageRequest of) {
        return userRepository.findAllByRoles_Name(rolename, of);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void updateUser(User currentUser, UserProfileDTO userProfileDTO) {
        currentUser.setFullName(userProfileDTO.getFullName());
        if(Objects.equals(userProfileDTO.getAvatar(), "")){
            currentUser.setAvatar("https://firebasestorage.googleapis.com/v0/b/home-dn.appspot.com/o/images%2Favatar.jpg?alt=media&token=f43bdd14-8aa5-4364-afc7-509f6f72a172");
        } else {
            currentUser.setAvatar(userProfileDTO.getAvatar());
        }
        currentUser.setAddress(userProfileDTO.getAddress());
        userRepository.save(currentUser);
    }

    @Override
    public boolean changePassword(User user, String oldPassword, String newPassword) {

        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }

        // Mã hóa mật khẩu mới và lưu lại
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
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

        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Đặt Role mặc định là ROLE_USER
        Role defaultRole = roleRepository.findRoleByName(ROLENAME.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found.")); // Lấy Role từ DB
        user.setRoles(Set.of(defaultRole)); // Gán role cho người dùng

        // Đặt Status mặc định là ACTIVE
        UserStatus activeStatus = userStatusRepository.findByStatus(UserStatus.USER_STATUS.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Error: Status is not found."));
        user.setUserStatuses(Set.of(activeStatus)); // Gán status cho người dùng

        user.setAvatar("https://firebasestorage.googleapis.com/v0/b/home-dn.appspot.com/o/images%2Favatar.jpg?alt=media&token=f43bdd14-8aa5-4364-afc7-509f6f72a172");

        // Save the user entity
        userRepository.save(user);

        return "Đăng ký tài khoản thành công!";
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public List<User> findUsersRequestingUpgrade() {
        return userRepository.findByUpgradeRequested(true);
    }

    @Override
    public UserDetailDTO getUserDetails(Long id) {
        Object result = userRepository.getUserDetails(id);

        // Mapping Object to UserDetailDTO directly
        Object[] fields = (Object[]) result; // Vì query trả về nhiều cột nên vẫn phải ép kiểu thành Object[]

        UserDetailDTO userDetailDTO = new UserDetailDTO(
                ((Number) fields[0]).longValue(),        // id
                (String) fields[1],                      // avatar
                (String) fields[2],                      // username
                (String) fields[3],                      // full_name
                (String) fields[4],                      // phone_number
                ((Number) fields[5]).intValue(),    // upgrade_requested (boolean)
                ((Number) fields[6]).doubleValue()       // total_spent
        );

        return userDetailDTO;
    }
}
