package com.manager.trainingtask.restControllers;

import com.manager.trainingtask.services.AuthenticationService;
import com.manager.trainingtask.config.TokenExtractorFilter;
import com.manager.trainingtask.entities.User;
import com.manager.trainingtask.services.UserService;
import jakarta.persistence.EntityManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
public class UserController {

    private UserService userService;

    private PasswordEncoder passwordEncoder;
    final EntityManager entityManager;
    private TokenExtractorFilter tokenExtractorFilter;

    private AuthenticationService authenticationService;

    public UserController(UserService userService, TokenExtractorFilter tokenExtractorFilter,
                          EntityManager entityManager, AuthenticationService authenticationService) {
        this.userService = userService;
        this.tokenExtractorFilter = tokenExtractorFilter;
        this.entityManager = entityManager;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/user")
    public User getUser() {
        String username = tokenExtractorFilter.getUsername();
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Username is not found - " + username);
        }
        return user;
    }

    @PostMapping("/user")
    public User addUser(@RequestBody User user){
        String encoded  = passwordEncoder.encode(user.getPassword());
        user.setPassword(encoded);
        User newUser = userService.save(user);
        return newUser;
    }

    @PutMapping("/user")
    public User  updateUser(@RequestBody User user) throws AccessDeniedException {
        String username = tokenExtractorFilter.getUsername();
        User currentUser = userService.findByUsername(username);
        String encoded = passwordEncoder.encode(user.getPassword());
        user.setPassword(encoded);
        if(currentUser.getId()!=user.getId() || !username.equals(user.getUsername())){
            throw new AccessDeniedException("You don't have permission to edit that");
        }
        User savedUser = userService.save(user);
        return savedUser;
    }

    @DeleteMapping("/user")
    public String deleteUser(){
        String username = tokenExtractorFilter.getUsername();
        User currentUser = userService.findByUsername(username);
        userService.deleteById(currentUser.getId());
        return "Deleted user id - " + currentUser.getId();


    }

}