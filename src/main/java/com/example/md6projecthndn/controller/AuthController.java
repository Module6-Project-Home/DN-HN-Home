//package com.example.md6projecthndn.controller;
//
//import com.example.md6projecthndn.configuration.jwt.JwtResponse;
//import com.example.md6projecthndn.model.User;
//import com.example.md6projecthndn.service.jwt.JwtService;
//import com.example.md6projecthndn.service.user.IUserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class AuthController {
//    private final AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JwtService jwtService;
//
//    @Autowired
//    private IUserService userService;
//
//    public AuthController(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody User user) {
//
//        Authentication authentication
//                = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String jwt = jwtService.generateTokenLogin(authentication);
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        User currentUser = userService.findByUsername(user.getUsername());
//        return ResponseEntity.ok(new JwtResponse(currentUser.getId(), jwt, userDetails.getUsername(), userDetails.getUsername(), userDetails.getAuthorities()));
//    }
//
//}
