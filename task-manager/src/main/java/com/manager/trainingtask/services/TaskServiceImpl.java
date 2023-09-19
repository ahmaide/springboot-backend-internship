package com.manager.trainingtask.services;

import com.manager.trainingtask.entities.Task;
import com.manager.trainingtask.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.manager.trainingtask.config.TokenExtractorFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> findAll(int userId) {
        return taskRepository.findByUserId(userId);
    }

    @Override
    public Task findById(int id) {
        Optional<Task> result = taskRepository.findById(id);
        Task temp = null;
        if (result.isPresent())
            temp = result.get();
        else{
            throw new RuntimeException("Did not find task id - " + id);
        }

        return temp;
    }

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public void deleteById(int id) {
        taskRepository.deleteById(id);
    }

    @Override
    public boolean validate(Task task){
        List<Task> userTasks = taskRepository.findByUserId(task.getUserId());
        for(Task t:userTasks){
            if(t.getId()!=task.getId() &&
                    ((task.getFrom().getTime() >= t.getFrom().getTime() && task.getFrom().getTime() <= t.getTo().getTime()) ||
                            (task.getTo().getTime() >= t.getFrom().getTime() && task.getTo().getTime() < t.getTo().getTime() ))
            )
                return false;
        }
        return true;
    }

    @Override
    public List<Task> findPaged(int index, int pageSize, int userId){
        Pageable sortedByDate = PageRequest.of(index, pageSize, Sort.by("from").descending());
        return taskRepository.findByUserId(userId, sortedByDate);
    }
}
