package com.ahmaide.trainingtaskmongo.restControllers;

import com.ahmaide.trainingtaskmongo.auth.AuthenticationRequest;
import com.ahmaide.trainingtaskmongo.auth.AuthenticationResponse;
import com.ahmaide.trainingtaskmongo.auth.RegisterRequest;
import com.ahmaide.trainingtaskmongo.entites.Token;
import com.ahmaide.trainingtaskmongo.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-service")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) throws AccessDeniedException {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/logout/user")
    public String logout(){
        Token token = authenticationService.getCurrentToken();
        authenticationService.revokeToken(token);
        return "Logged out!";
    }

}