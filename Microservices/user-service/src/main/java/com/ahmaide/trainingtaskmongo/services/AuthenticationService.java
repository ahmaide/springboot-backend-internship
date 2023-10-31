package com.ahmaide.trainingtaskmongo.services;

import com.ahmaide.trainingtaskmongo.auth.AuthenticationRequest;
import com.ahmaide.trainingtaskmongo.auth.AuthenticationResponse;
import com.ahmaide.trainingtaskmongo.auth.RegisterRequest;
import com.ahmaide.trainingtaskmongo.config.TokenExtractorFilter;
import com.ahmaide.trainingtaskmongo.entites.Token;
import com.ahmaide.trainingtaskmongo.entites.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final TokenExtractorFilter tokenExtractorFilter;

    public User userFromRequest(AuthenticationRequest request){
        User user;
        if(request instanceof RegisterRequest registerRequest){
            user = User.builder()
                    .username(registerRequest.getUsername())
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .age(registerRequest.getAge())
                    .enabled(registerRequest.getEnabled())
                    .build();
            userService.save(user);
        }
        else{
            user = userService.findByUsername(request.getUsername());
            if(user == null){
                throw new UsernameNotFoundException("User not found");
            }
        }
        return user;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        var user = userFromRequest(request);
        var token = tokenService.generateToken(user);
        return generateResponse(token);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userFromRequest(request);
        checkPassword(request, user);
        var token = tokenService.generateToken(user);
        return generateResponse(token);
    }

    public void revokeToken(Token token){
        token.setExpired(true);
        token.setRevoked(true);
        tokenService.save(token);
    }

    public Token getCurrentToken(){
        String tokenData = tokenExtractorFilter.getJwt();
        return tokenService.findByData(tokenData);
    }

    private AuthenticationResponse generateResponse(Token token){
        return AuthenticationResponse
                .builder()
                .token(token.getData())
                .build();
    }

    private void checkPassword(AuthenticationRequest request, User user){
        boolean isMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!isMatch){
            throw new BadCredentialsException("Invalid password");
        }
    }
}