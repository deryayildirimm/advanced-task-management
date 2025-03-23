package com.example.advancedtaskmanagement.user;

import com.example.advancedtaskmanagement.security.AuthRequest;
import com.example.advancedtaskmanagement.security.CreateUserRequest;
import com.example.advancedtaskmanagement.security.JwtService;
import com.example.advancedtaskmanagement.security.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService service;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;


    public UserController(UserService service, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.service = service;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Hello World!";
    }

    @PostMapping("/addNewUser")
    public User addUser(@RequestBody CreateUserRequest request) {
        return service.createUser(request);
    }

    @PostMapping("/generateToken")
    public String generateToken(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        if (authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal(); // 👈 burada User nesnesini alıyoruz
            return jwtService.generateToken(user);             // 👈 generateToken(user) şeklinde çağırıyoruz
        }

        throw new UsernameNotFoundException("invalid username {} " + request.username());
    }


    @GetMapping("/user")
    public String getUserString() {
        return "This is USER!";
    }

    @GetMapping("/admin")
    public String getAdminString() {
        return "This is ADMIN!";
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {

        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {

        return null;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        return null;
    }
}
