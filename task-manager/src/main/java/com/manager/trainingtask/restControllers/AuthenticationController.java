package com.manager.trainingtask.restControllers;

import com.manager.trainingtask.auth.AuthenticationRequest;
import com.manager.trainingtask.auth.AuthenticationResponse;
import com.manager.trainingtask.auth.RegisterRequest;
import com.manager.trainingtask.config.TokenExtractorFilter;
import com.manager.trainingtask.entities.Tokens;
import com.manager.trainingtask.entities.User;
import com.manager.trainingtask.repositories.TokenRepository;
import com.manager.trainingtask.services.AuthenticationService;
import com.manager.trainingtask.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    private final TokenExtractorFilter tokenExtractorFilter;

    private final UserService userService;

    private final TokenRepository tokenRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) throws AccessDeniedException {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/logout/user")
    public String logout(){
        String tokenData = tokenExtractorFilter.getJwt();
        System.out.println("token: " + tokenData);
        Tokens token = tokenRepository.findByData(tokenData);
        service.revokeToken(token);
        return "Logged out!";
    }
    @PostMapping("/logout/all")
    public String logoutAll(){
        String username = tokenExtractorFilter.getUsername();
        User currentUser = userService.findByUsername(username);
        service.revokeAllUserTokens(currentUser);
        return "All user (" + currentUser.getId() + ") tokens are logged out ";
    }


}