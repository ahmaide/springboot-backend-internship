package com.manager.trainingtask.services;

import com.manager.trainingtask.entities.Task;



import java.util.List;

public interface TaskService {


    List<Task> findAll(int userId);

    Task findById(int id);

    Task save (Task task);

    void deleteById(int id);

    boolean validate(Task task);

    List<Task> findPaged(int index, int pageSize, int userId);

}
