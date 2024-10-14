package com.example.md6projecthndn.controller.user;


import com.example.md6projecthndn.model.dto.ROLENAME;
import com.example.md6projecthndn.model.dto.UserPrinciple;
import com.example.md6projecthndn.model.entity.user.Role;
import com.example.md6projecthndn.model.entity.user.User;
import com.example.md6projecthndn.model.entity.user.UserStatus;
import com.example.md6projecthndn.repository.user.IUserStatusRepository;
import com.example.md6projecthndn.service.jwt.JwtService;
import com.example.md6projecthndn.service.role.RoleService;
import com.example.md6projecthndn.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class LoginGG {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Autowired // Use @Autowired for dependency injection
    private RoleService roleService;
    @Autowired
    private IUserStatusRepository userStatusRepository; // Repository để lấy Status từ DB

    @Autowired
    private JwtService jwtService;

    @Value("${client-id}")
    private String clientId;

    @Value("${client-secret}")
    private String clientSecret;

    public LoginGG(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/v1/SSO/google")
    public ResponseEntity<String> googleLogin() {
        try {
            String redirectUri = "http://localhost:8080/auth/google/callback";
            String scope = "profile email";

            String encodedScope = URLEncoder.encode(scope, StandardCharsets.UTF_8.toString());
            String redirectUrl = "https://accounts.google.com/o/oauth2/auth?" +
                    "client_id=" + clientId +
                    "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8.toString()) +
                    "&response_type=code" +
                    "&scope=" + encodedScope;

            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirectUrl)).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi: " + e.getMessage());
        }
    }

    @GetMapping("/google/callback")
    public ResponseEntity<String> googleCallback(@RequestParam String code) {
        try {
            String accessToken = getAccessToken(code);
            Map<String, Object> userAttributes = getUserInfo(accessToken);

            // Lưu thông tin người dùng vào cơ sở dữ liệu
            String email = (String) userAttributes.get("email");
            String fullName = (String) userAttributes.get("name");
            String avatar = (String) userAttributes.get("picture");

            // Kiểm tra xem người dùng đã tồn tại chưa
            User user = userService.findByEmail(email);
            if (user == null) {
                user = new User();
                user.setUsername(email);
                user.setEmail(email);
                user.setFullName(fullName);
                user.setAvatar(avatar);
                // Mã hóa mật khẩu mặc định (nếu cần thiết, có thể tạo mật khẩu ngẫu nhiên)

                Role userRole = roleService.findByName(ROLENAME.ROLE_USER);
                if (userRole != null) {
                    user.setRoles(new HashSet<>(Collections.singleton(userRole))); // Set roles as Set
                }


                // Đặt Status mặc định là ACTIVE
                UserStatus activeStatus = userStatusRepository.findByStatus(UserStatus.USER_STATUS.ACTIVE)
                        .orElseThrow(() -> new RuntimeException("Error: Status is not found."));
                user.setUserStatuses(Set.of(activeStatus)); // Gán status cho người dùng


                user.setPassword(passwordEncoder.encode("defaultPassword"));
                userService.save(user);
            }

            // Get roles as a collection of GrantedAuthority
            List<GrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName().toString()))
                    .collect(Collectors.toList());

            // Add the ROLE_USER from ROLENAME enum
            authorities.add(new SimpleGrantedAuthority(ROLENAME.ROLE_USER.name()));

            // Create an instance of UserPrinciple (custom UserDetails implementation)
            UserPrinciple userPrinciple = new UserPrinciple(user.getEmail(), user.getPassword(), authorities);

            // Create authentication token
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userPrinciple, null, authorities);

            // Generate token
            String token = jwtService.generateTokenLogin(authentication);

            // Determine redirect URL
            String redirectUrl = determineRedirectUrl(user.getRoles()) + "?token=" + token;
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirectUrl)).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
        }
    }

    private String determineRedirectUrl(Set<Role> roles) {
        if (roles.stream().anyMatch(role -> role.getName().equals(ROLENAME.ROLE_HOST))) {
            return "http://localhost:3000/host/listMyHome"; // Adjust the URL to your frontend
        } else if (roles.stream().anyMatch(role -> role.getName().equals(ROLENAME.ROLE_ADMIN))) {
            return "http://localhost:3000/admin/dashboard"; // Adjust the URL to your frontend
        } else {
            return "http://localhost:3000/home"; // Adjust the URL to your frontend
        }
    }


    private String getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("code", code);
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("redirect_uri", "http://localhost:8080/auth/google/callback");

        requestBody.add("grant_type", "authorization_code");

        ResponseEntity<Map> response = restTemplate.postForEntity("https://oauth2.googleapis.com/token", requestBody, Map.class);
        return (String) response.getBody().get("access_token");
    }

    private Map<String, Object> getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> userInfoResponse = restTemplate.exchange("https://www.googleapis.com/oauth2/v3/userinfo", HttpMethod.GET, entity, Map.class);
        return userInfoResponse.getBody();
    }
}
