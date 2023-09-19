package com.manager.trainingtask.services;

import com.manager.trainingtask.entities.User;
import com.manager.trainingtask.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

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
    }

    @Test
    void findByUsername() {
        String username = "claude";
        when(userRepository.findByUsername(username)).thenReturn(user);
        User returnedUser = userService.findByUsername(username);
        Assertions.assertThat(returnedUser).isNotNull();
        Assertions.assertThat(returnedUser).isEqualTo(user);
    }

    @Test
    void save() {
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        User savedUser = userService.save(user);
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser).isEqualTo(user);
    }

    @Test
    void deleteById() {
        int userId = 1;
        Mockito.doNothing().when(userRepository).deleteById(userId);
        assertAll(() -> userService.deleteById(1));
    }
}