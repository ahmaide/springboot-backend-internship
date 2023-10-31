package com.tasks.tasksservice.repositories;

import com.tasks.tasksservice.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findByUserId(String userId);

    List<Task> findByUserId(String userId, Pageable pageable);

}
