package com.manager.trainingtask.services;

import com.manager.trainingtask.entities.Task;
import com.manager.trainingtask.entities.User;
import com.manager.trainingtask.repositories.TaskRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;

    private User user;

    @BeforeEach
    private void init(){
        user = User.builder()
                .username("claude")
                .id(1)
                .age(27)
                .email("claude@speed.com")
                .password("123")
                .enabled(1)
                .build();

        task = Task.builder()
                .id(1)
                .description("First")
                .complete(true)
                .from(new Date())
                .to(new Date())
                .userId(user.getId()).build();
    }

    @Test
    void findAll() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        tasks.add(Task.builder()
                .id(2)
                .description("Second")
                .complete(true)
                .from(new Date())
                .to(new Date())
                .userId(user.getId()).build());
        when(taskRepository.findByUserId(user.getId())).thenReturn(tasks);
        List<Task> returnedTasks = taskService.findAll(user.getId());
        Assertions.assertThat(returnedTasks).isNotNull();
        assertNotEquals(returnedTasks.size(),0);
        assertEquals(tasks.size(), returnedTasks.size());
        assertEquals(tasks.get(0), returnedTasks.get(0));
        assertEquals(tasks.get(1), returnedTasks.get(1));
    }

    @Test
    void findById() {
        int taskId =1;
        when(taskRepository.findById(taskId)).thenReturn(Optional.ofNullable(task));
        Task returnedTask = taskService.findById(taskId);
        Assertions.assertThat(returnedTask).isNotNull();
        task=null;
        assertThrows(RuntimeException.class, () -> {
            taskService.findById(2);
        });
    }

    @Test
    void save() {
        when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);
        Task savedTask = taskService.save(task);
        Assertions.assertThat(savedTask).isNotNull();
        Assertions.assertThat(savedTask).isEqualTo(task);
    }

    @Test
    void deleteById() {
        int taskId = 1;
        Mockito.doNothing().when(taskRepository).deleteById(taskId);
        assertAll(() -> taskService.deleteById(1));
    }

    @Test
    void validate() {
        task.setTo(new Date(task.getFrom().getTime() + 10000));
        List<Task> userTasks= new ArrayList<>();
        userTasks.add(task);
        when(taskRepository.findByUserId(user.getId())).thenReturn(userTasks);
        Task [] testedTasks = new Task[6];
        for(int i=0; i<6 ; i++)
            testedTasks[i]= Task.builder()
                    .id(task.getId()+i)
                    .description("Task")
                    .complete(true)
                    .from(new Date(task.getFrom().getTime() - 12000 +(5000*i) ))
                    .to(new Date(task.getFrom().getTime() - 8000 +(5000*i)))
                    .userId(user.getId()).build();

        testedTasks[0].setFrom(testedTasks[3].getFrom());
        testedTasks[0].setTo(testedTasks[3].getTo());
        boolean [] validations = {true, true, false, false, false, true};
        for(int i=0; i<6 ; i++){
            boolean returnedValidation = taskService.validate(testedTasks[i]);
            assertEquals(returnedValidation, validations[i]);
        }

    }

    @Test
    void findPaged() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        Pageable pageable = PageRequest.of(0, 1, Sort.by("from").descending());
        when(taskRepository.findByUserId(1, pageable)).thenReturn(tasks);
        List<Task> returnedTasks = taskService.findPaged(0, 1, user.getId());
        Assertions.assertThat(returnedTasks).isNotNull();
        assertNotEquals(returnedTasks.size(),0);
        assertEquals(tasks.size(), returnedTasks.size());
        assertEquals(tasks.get(0), returnedTasks.get(0));
    }
}