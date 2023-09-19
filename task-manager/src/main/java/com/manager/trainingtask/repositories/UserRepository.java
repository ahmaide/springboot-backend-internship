package com.manager.trainingtask.repositories;

import com.manager.trainingtask.entities.Task;
import com.manager.trainingtask.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findById(int userId);

    User findByUsername(String username);
    void deleteById(int Id);
}