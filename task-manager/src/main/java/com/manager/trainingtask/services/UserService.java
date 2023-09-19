package com.manager.trainingtask.services;

import com.manager.trainingtask.entities.User;

import java.util.List;

public interface UserService {

    User findByUsername(String username);
    User save (User user);

    void deleteById(int id);
}