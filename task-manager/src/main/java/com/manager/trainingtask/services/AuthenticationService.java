package com.manager.trainingtask.services;

import com.manager.trainingtask.auth.AuthenticationRequest;
import com.manager.trainingtask.auth.AuthenticationResponse;
import com.manager.trainingtask.auth.RegisterRequest;
import com.manager.trainingtask.entities.Tokens;
import com.manager.trainingtask.entities.User;
import com.manager.trainingtask.repositories.TokenRepository;
import com.manager.trainingtask.repositories.UserRepository;
import com.manager.trainingtask.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .age(request.getAge())
                .enabled(request.getEnabled())
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.buildToken(new HashMap<>(), user);
        var token = Tokens.builder()
                .userId(user.getId())
                .data(jwtToken)
                .type("BEARER")
                .expired(false)
                .revoked(false).build();
        tokenRepository.save(token);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws AccessDeniedException {
        User u = userRepository.findByUsername(request.getUsername());
        if(u == null){
            throw new UsernameNotFoundException("User not found");
        }
        boolean isMatch = passwordEncoder.matches(request.getPassword(), u.getPassword());
        if(!isMatch){
            throw new BadCredentialsException("Invalid password");
        }
        var user = userRepository.findByUsername(request.getUsername());
        var jwtToken = jwtService.buildToken(new HashMap<>(), user);
        var token = Tokens.builder()
                .userId(user.getId())
                .data(jwtToken)
                .type("BEARER")
                .expired(false)
                .revoked(false).build();
        tokenRepository.save(token);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public void revokeAllUserTokens(User user){
        var validTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if(validTokens.isEmpty())
            return;
        validTokens.forEach( t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validTokens);
    }

    public void revokeToken(Tokens token){
            token.setExpired(true);
            token.setRevoked(true);
        tokenRepository.save(token);
    }
}