package com.manager.trainingtask.repositories;

import com.manager.trainingtask.entities.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByUserId(int userId);

    List<Task> findByUserId(int UserId, Pageable pageable);
}
