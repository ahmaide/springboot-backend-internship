package com.manager.trainingtask.restControllers;

import com.manager.trainingtask.config.TokenExtractorFilter;
import com.manager.trainingtask.entities.Task;
import com.manager.trainingtask.entities.User;
import com.manager.trainingtask.services.TaskService;
import com.manager.trainingtask.services.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RestController
public class TaskController {

    private TaskService taskService;

    private UserService userService;

    private TokenExtractorFilter tokenExtractorFilter;

    public TaskController(TaskService taskService, UserService userService,
                          TokenExtractorFilter tokenExtractorFilter) {
        this.taskService = taskService;
        this.userService = userService;
        this.tokenExtractorFilter = tokenExtractorFilter;
    }

    @GetMapping("/tasks")
    public List<Task> findPaged(@RequestParam int index,
                                @RequestParam(name = "size") int pageSize){
        String username = tokenExtractorFilter.getUsername();
        User currentUser = userService.findByUsername(username);
        return taskService.findPaged(index, pageSize, currentUser.getId());
    }

    @GetMapping("/tasks/{taskId}")
    public Task getTask(@PathVariable int taskId) throws AccessDeniedException {
        String username = tokenExtractorFilter.getUsername();
        User currentUser = userService.findByUsername(username);
        Task task = taskService.findById(taskId);
        if (task == null) {
            throw new RuntimeException("Task id is not found - " + taskId);
        }
        if(task.getUserId()!=currentUser.getId())
            throw new AccessDeniedException("You don't have access to this task");
        return task;
    }

    @PostMapping("/tasks")
    public Task addTask(@RequestBody Task task){
        if(task.getFrom().getTime() > task.getTo().getTime())
            throw new RuntimeException("The date for the task to end is invalid please enter a valid date");
        String username = tokenExtractorFilter.getUsername();
        User currentUser = userService.findByUsername(username);
        task.setId(0);
        task.setUserId(currentUser.getId());
        if(!taskService.validate(task))
            throw new RuntimeException("This time is already been reserved");
        Task savedTask = taskService.save(task);
        return savedTask;
    }

    @PutMapping("/tasks")
    public Task  updateTask(@RequestBody Task task) throws AccessDeniedException {
        String username = tokenExtractorFilter.getUsername();
        User currentUser = userService.findByUsername(username);
        if(taskService.findById(task.getId()).getUserId()!=currentUser.getId())
            throw new AccessDeniedException("You don't have access to this task");
        if(taskService.findById(task.getId()).getUserId()!=task.getUserId())
            throw new AccessDeniedException("task user can't be changed");
        if(task.getFrom().getTime() <= task.getTo().getTime())
            throw new RuntimeException("The date for the task to end is invalid please enter a valid date");
        if( !taskService.validate(task))
            throw new RuntimeException("This time is already been reserved");
        Task savedTask = taskService.save(task);
        return savedTask;
    }

    @DeleteMapping("/tasks/{taskId}")
    public String deleteTask(@PathVariable int taskId) throws AccessDeniedException {
        String username = tokenExtractorFilter.getUsername();
        User currentUser = userService.findByUsername(username);
        Task task = taskService.findById(taskId);
        if (task == null) {
            throw new RuntimeException("Task id is not found - " + taskId);
        }
        if(task.getUserId()==currentUser.getId()) {
            taskService.deleteById(taskId);
            return "Deleted task id - " + taskId;
        }
        else{
            throw new AccessDeniedException("You don't have access to this task");
        }
    }
}
