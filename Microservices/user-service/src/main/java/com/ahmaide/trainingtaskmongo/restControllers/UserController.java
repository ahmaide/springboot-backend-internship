package com.ahmaide.trainingtaskmongo.restControllers;

import com.ahmaide.trainingtaskmongo.entites.User;
import com.ahmaide.trainingtaskmongo.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.nio.file.AccessDeniedException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user-service/")
public class UserController {

    private final UserService userService;

    @GetMapping("/id")
    @ResponseStatus(HttpStatus.OK)
    public String sendUserId(){
        User user = userService.extractUserFromToken();
        return user.getId();
    }

    @GetMapping("/user")
    public User getUser() {
        return userService.extractUserFromToken();
    }

    @PostMapping("/user")
    public User addUser(@RequestBody User user){
        userService.encodePassword(user);
        return userService.save(user);
    }

    @PutMapping("/user")
    public User  updateUser(@RequestBody User user) throws AccessDeniedException {
        User currentUser = userService.extractUserFromToken();
        userService.encodePassword(user);
        if(!userService.sameUserInfo(currentUser, user)){
            throw new AccessDeniedException("You don't have permission to edit that");
        }
        return userService.save(user);
    }

    @DeleteMapping("/user")
    public String deleteUser(){
        User currentUser = userService.extractUserFromToken();
        userService.deleteById(currentUser.getId());
        return "Deleted user id - " + currentUser.getId();
    }

}