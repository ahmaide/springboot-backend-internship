package com.tasks.tasksservice.services;

import com.tasks.tasksservice.config.TokenExtractorFilter;
import com.tasks.tasksservice.entities.Task;
import com.tasks.tasksservice.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TaskServiceImpTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TokenExtractorFilter tokenExtractorFilter;
    @InjectMocks
    private TaskService taskService;

    private Task task;

    @BeforeEach
    private void init(){
        task = Task.builder()
                .id(1)
                .description("First")
                .complete(true)
                .from(new Date())
                .to(new Date())
                .userId("123").build();
    }

    @Test
    void findById() throws AccessDeniedException {
        int taskId =1;
        when(taskRepository.findById(taskId)).thenReturn(Optional.ofNullable(task));
        when(tokenExtractorFilter.getUserId()).thenReturn(task.getUserId());
        Task returnedTask = taskService.findById(taskId);
        Assertions.assertThat(returnedTask).isNotNull();
        task.setUserId("456");
        assertThrows(AccessDeniedException.class, () -> {
            taskService.findById(1);
        });
    }

    @Test
    void save() throws AccessDeniedException {
        when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);
        when(tokenExtractorFilter.getUserId()).thenReturn(task.getUserId());
        Task savedTask = taskService.save(task);
        Assertions.assertThat(savedTask).isNotNull();
        Assertions.assertThat(savedTask).isEqualTo(task);
        when(tokenExtractorFilter.getUserId()).thenReturn("456");
        assertThrows(AccessDeniedException.class, () -> {
            taskService.save(task);
        });
    }

    @Test
    void deleteById() {
        when(taskRepository.findById(1)).thenReturn(Optional.ofNullable(task));
        when(tokenExtractorFilter.getUserId()).thenReturn(task.getUserId());
        int taskId = 1;
        Mockito.doNothing().when(taskRepository).deleteById(taskId);
        assertAll(() -> taskService.deleteById(1));

        when(tokenExtractorFilter.getUserId()).thenReturn("456");
        assertThrows(AccessDeniedException.class, () -> {
            taskService.deleteById(1);
        });
    }

    @Test
    void validate() {
        task.setTo(new Date(task.getFrom().getTime() + 10000));
        List<Task> userTasks= new ArrayList<>();
        userTasks.add(task);
        when(taskRepository.findByUserId("123")).thenReturn(userTasks);
        Task [] testedTasks = new Task[6];
        for(int i=0; i<6 ; i++)
            testedTasks[i]= Task.builder()
                    .id(task.getId()+i)
                    .description("Task")
                    .complete(true)
                    .from(new Date(task.getFrom().getTime() - 12000 +(5000*i) ))
                    .to(new Date(task.getFrom().getTime() - 8000 +(5000*i)))
                    .userId("123").build();

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
        when(tokenExtractorFilter.getUserId()).thenReturn(task.getUserId());
        when(taskRepository.findByUserId("123", pageable)).thenReturn(tasks);
        List<Task> returnedTasks = taskService.findPaged(0, 1);
        Assertions.assertThat(returnedTasks).isNotNull();
        assertNotEquals(returnedTasks.size(),0);
        assertEquals(tasks.size(), returnedTasks.size());
        assertEquals(tasks.get(0), returnedTasks.get(0));
    }

    @Test
    void correctTimes(){
        Date newDate = new Date();
        task.setFrom(new Date(newDate.getTime() + 1000));
        task.setTo(new Date(newDate.getTime() + 11000));
        boolean accepted = taskService.correctTimes(task);
        task.setFrom(new Date(newDate.getTime() + 21000));
        boolean rejected = taskService.correctTimes(task);
        assertEquals(accepted, true);
        assertEquals(rejected, false);
    }
}