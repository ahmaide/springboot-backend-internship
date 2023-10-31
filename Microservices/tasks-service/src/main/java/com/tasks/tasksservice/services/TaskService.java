package com.tasks.tasksservice.services;

import com.tasks.tasksservice.config.TokenExtractorFilter;
import com.tasks.tasksservice.entities.Task;
import com.tasks.tasksservice.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final TokenExtractorFilter tokenExtractorFilter;

    public Task findById(int taskId) throws AccessDeniedException {
        Optional<Task> result = taskRepository.findById(taskId);
        Task task = result.get();
        if (!checkTaskAccess(task))
            throw new AccessDeniedException("You don't have access to this task");
        return task;
    }

    public Task save(Task task) throws AccessDeniedException {
        if(!checkTaskAccess(task))
            throw new AccessDeniedException("You don't have access to this task");
        return taskRepository.save(task);
    }

    public void deleteById(int id) throws AccessDeniedException {
        Optional<Task> result = taskRepository.findById(id);
        Task task = result.get();
        if(!checkTaskAccess(task))
            throw new AccessDeniedException("You don't have access to this task");
        taskRepository.deleteById(id);
    }

    public boolean correctTimes(Task task){
        return task.getFrom().getTime() < task.getTo().getTime();
    }

    public boolean validate(Task task){
        List<Task> userTasks = taskRepository.findByUserId(task.getUserId());
        for(Task userTask:userTasks){
            if( !sameTask(task, userTask) && (startDuring(task, userTask) || startDuring(userTask, task)) )
                return false;
        }
        return true;
    }

    public List<Task> findPaged(int index, int pageSize){
        Pageable sortedByDate = PageRequest.of(index, pageSize, Sort.by("from").descending());
        return taskRepository.findByUserId(tokenExtractorFilter.getUserId(), sortedByDate);
    }

    private boolean sameTask(Task task1, Task task2){
        return task1.getId() == task2.getId();
    }

    private boolean startDuring(Task t1, Task t2){
        return t1.getFrom().getTime() >= t2.getFrom().getTime() &&
                t1.getFrom().getTime() <= t2.getTo().getTime();
    }

    private boolean checkTaskAccess(Task task){
        return task.getUserId().equals(tokenExtractorFilter.getUserId());
    }
}