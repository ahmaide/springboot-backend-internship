package com.ahmaide.trainingtaskmongo.services;

import com.ahmaide.trainingtaskmongo.auth.RegisterRequest;
import com.ahmaide.trainingtaskmongo.auth.AuthenticationResponse;
import com.ahmaide.trainingtaskmongo.auth.AuthenticationRequest;
import com.ahmaide.trainingtaskmongo.entites.Token;
import com.ahmaide.trainingtaskmongo.entites.User;
import com.ahmaide.trainingtaskmongo.repositories.TokenRepository;
import com.ahmaide.trainingtaskmongo.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User user;

    private Token token;

    @BeforeEach
    private void init(){
        user = User.builder()
                .username("claude")
                .id("abc")
                .age(27)
                .email("claude@speed.com")
                .password("123")
                .enabled(1)
                .build();

        token = Token.builder()
                .userId(user.getId())
                .data("ABCD")
                .type("BEARER")
                .expired(false)
                .revoked(false).build();
    }

    @Test
    void register() {
        RegisterRequest request = RegisterRequest.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .age(user.getAge())
                .enabled(user.getEnabled())
                .build();
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        when(tokenRepository.save(Mockito.any(Token.class))).thenReturn(token);
        when(passwordEncoder.encode(Mockito.any(String.class))).thenReturn(user.getPassword());
        when(jwtService.buildToken(Mockito.any(HashMap.class), Mockito.any(User.class))).thenReturn(token.getData());
        AuthenticationResponse response = authenticationService.register(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getToken(), token.getData());
    }

    @Test
    void authenticateTrue() throws AccessDeniedException {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(user);
        when(passwordEncoder.matches(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(true);
        when(jwtService.buildToken(Mockito.any(HashMap.class), Mockito.any(User.class))).thenReturn(token.getData());
        when(tokenRepository.save(Mockito.any(Token.class))).thenReturn(token);
        AuthenticationResponse response = authenticationService.authenticate(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getToken(), token.getData());
    }

    @Test
    void authenticateFalse() throws AccessDeniedException {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(user);
        when(passwordEncoder.matches(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(false);
        assertThrows(BadCredentialsException.class, () -> {
            authenticationService.authenticate(request);
        });
    }

    @Test
    void authenticateNoUser() throws AccessDeniedException {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> {
            authenticationService.authenticate(request);
        });
    }


    @Test
    void revokeToken() {
        when(tokenRepository.save(Mockito.any(Token.class))).thenReturn(token);
        authenticationService.revokeToken(token);
        Assertions.assertEquals(token.isRevoked(), true);
        Assertions.assertEquals(token.isExpired(), true);
    }
}