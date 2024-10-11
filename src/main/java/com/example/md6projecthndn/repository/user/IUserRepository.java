package com.example.md6projecthndn.repository.user;


import com.example.md6projecthndn.model.dto.ROLENAME;
import com.example.md6projecthndn.model.dto.UserDetailDTO;
import com.example.md6projecthndn.model.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
//    Optional<User> findByUsername(String username);


    User findByUsername(String username);


    List<User> findByRoles_Name(ROLENAME role); // Lấy danh sách người dùng theo role

    User findUserById (Long id);

    User findByEmail(String email);


    Page<User> findAllByRoles_Name(ROLENAME rolename, PageRequest of);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findByUpgradeRequested(boolean upgradeRequested);

    @Query(nativeQuery = true, value =  "SELECT u.id, u.avatar,  u.username, u.full_name, u.phone_number, uus.user_statuses_id as user_status, SUM(p.price_per_night * DATEDIFF(b.check_out_date, b.check_in_date)) AS total_spent FROM users u JOIN bookings b ON u.id = b.guest_id JOIN properties p ON b.property_id = p.id join users_user_statuses uus on u.id = uus.user_id WHERE u.id = :userId GROUP BY u.id, u.avatar, u.username, u.full_name, u.phone_number, uus.user_statuses_id;")
    Object getUserDetails(@Param("userId") Long userId);
}
