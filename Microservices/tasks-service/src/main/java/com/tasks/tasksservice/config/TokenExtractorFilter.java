package com.tasks.tasksservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenExtractorFilter extends OncePerRequestFilter {

    private static final String HEADER = "Authorization";

    private static final String BEARER_HEADER = "Bearer ";

    private static final String USER_SERVICE_URL = "http://localhost:8080/api/user-service/id";

    private final WebClient webClient;

    @Getter
    private String userId;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader(HEADER);
        if (checkHeader(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = authHeader.substring(7);

        try {
            userId = getIdFromUserService(jwt);
        }
        catch (WebClientResponseException e){
            System.out.println("Token Expired");
        }

        filterChain.doFilter(request, response);
    }

    private boolean checkHeader(String header){
        return (header == null || !header.startsWith(BEARER_HEADER));
    }

    private String getIdFromUserService(String jwt){
        return webClient.get()
                .uri(USER_SERVICE_URL)
                .header(HEADER, BEARER_HEADER + jwt)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}