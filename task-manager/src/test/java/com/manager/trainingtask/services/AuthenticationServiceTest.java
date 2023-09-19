package com.manager.trainingtask.services;

import com.manager.trainingtask.auth.AuthenticationRequest;
import com.manager.trainingtask.auth.AuthenticationResponse;
import com.manager.trainingtask.auth.RegisterRequest;
import com.manager.trainingtask.entities.Tokens;
import com.manager.trainingtask.entities.User;
import com.manager.trainingtask.repositories.TokenRepository;
import com.manager.trainingtask.repositories.UserRepository;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
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

    private Tokens token;

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

        token = Tokens.builder()
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
        when(tokenRepository.save(Mockito.any(Tokens.class))).thenReturn(token);
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
        when(tokenRepository.save(Mockito.any(Tokens.class))).thenReturn(token);
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
    void revokeAllUserTokens() {
        List<Tokens> userTokens= new ArrayList<>();
        userTokens.add(token);
        when(tokenRepository.findAllValidTokensByUser(user.getId())).thenReturn(userTokens);
        when(tokenRepository.saveAll(Mockito.any(List.class))).thenReturn(userTokens);
        authenticationService.revokeAllUserTokens(user);
        Assertions.assertEquals(userTokens.get(0).isExpired(), true);
        Assertions.assertEquals(userTokens.get(0).isRevoked(), true);
    }

    @Test
    void revokeToken() {
        when(tokenRepository.save(Mockito.any(Tokens.class))).thenReturn(token);
        authenticationService.revokeToken(token);
        Assertions.assertEquals(token.isRevoked(), true);
        Assertions.assertEquals(token.isExpired(), true);
    }
}