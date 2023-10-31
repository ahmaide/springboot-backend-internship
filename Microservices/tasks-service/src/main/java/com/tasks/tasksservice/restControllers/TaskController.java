package com.tasks.tasksservice.restControllers;

import com.tasks.tasksservice.entities.Task;
import com.tasks.tasksservice.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task-service/")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/tasks")
    public List<Task> findPaged(@RequestParam int index,
                                @RequestParam(name = "size") int pageSize){
        return taskService.findPaged(index, pageSize);
    }

    @GetMapping("/tasks/{taskId}")
    public Task getTask(@PathVariable int taskId) throws AccessDeniedException {
        Task task = taskService.findById(taskId);
        if (task == null)
            throw new RuntimeException("Task id is not found - " + taskId);
        return task;
    }

    @PostMapping("/tasks")
    public Task addTask(@RequestBody Task task) throws AccessDeniedException {
        if(!taskService.correctTimes(task))
            throw new RuntimeException("The date for the task to end is invalid please enter a valid date");
        task.setId(0);
        if(!taskService.validate(task))
            throw new RuntimeException("This time is already been reserved");
        return taskService.save(task);
    }

    @PutMapping("/tasks")
    public Task  updateTask(@RequestBody Task task) throws AccessDeniedException {
        if(!Objects.equals(taskService.findById(task.getId()).getUserId(), task.getUserId()))
            throw new AccessDeniedException("task user can't be changed");
        if(!taskService.correctTimes(task))
            throw new RuntimeException("The date for the task to end is invalid please enter a valid date");
        if(!taskService.validate(task))
            throw new RuntimeException("This time is already been reserved");
        return taskService.save(task);
    }

    @DeleteMapping("/tasks/{taskId}")
    public String deleteTask(@PathVariable int taskId) throws AccessDeniedException {
        Task task = taskService.findById(taskId);
        if (task == null) {
            throw new RuntimeException("Task id is not found - " + taskId);
        }
        taskService.deleteById(taskId);
        return "Deleted task id - " + taskId;
    }

}
