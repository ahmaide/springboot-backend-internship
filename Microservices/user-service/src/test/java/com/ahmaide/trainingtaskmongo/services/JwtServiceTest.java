package com.ahmaide.trainingtaskmongo.services;

import com.ahmaide.trainingtaskmongo.entites.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private Key signInKey;
    private User user;

    private String token;

    @BeforeEach
    private void init() {
        String secretKey = "6242434F4D446F427858667578555146324F6974524454796D39695249694131";
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        signInKey = Keys.hmacShaKeyFor(keyBytes);
        user = User.builder()
                .username("claude")
                .id("abc")
                .age(27)
                .email("claude@speed.com")
                .password("123")
                .enabled(1)
                .build();
        token = Jwts
                .builder()
                .setClaims(new HashMap<>())
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 *60 *24))
                .signWith(signInKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Test
    void extractUsername() {
        String returnedUsername = jwtService.extractUsername(token);
        Assertions.assertThat(returnedUsername).isNotNull();
        assertEquals(returnedUsername, user.getUsername());
    }

    @Test
    void extractClaim() {
        String returnedUsername = jwtService.extractClaim(token, Claims::getSubject);
        Assertions.assertThat(returnedUsername).isNotNull();
        assertEquals(returnedUsername, user.getUsername());
    }

    @Test
    void buildToekn(){
        String builtToken = jwtService.buildToken(new HashMap<>(), user);
        Assertions.assertThat(builtToken).isNotNull();

        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(signInKey)
                .build()
                .parseClaimsJws(builtToken)
                .getBody();

        String returnedUsername = claims.getSubject();
        assertEquals(returnedUsername, user.getUsername());
    }

    @Test
    void isTokenValid() {
        User wrongUser = User.builder()
                .username("tommy")
                .id("abc")
                .age(31)
                .email("tommy@gmail.com")
                .password("123")
                .enabled(1)
                .build();

        boolean [] returnedValidations = new boolean[2];
        returnedValidations[0] = jwtService.isTokenValid(token, user);
        returnedValidations[1] = jwtService.isTokenValid(token, wrongUser);

        assertEquals(returnedValidations[0], true);
        assertEquals(returnedValidations[1], false);
    }

    @Test
    void extractExpiration(){
        Date expiration = new Date(System.currentTimeMillis() + 1000 *60 *24);
        String newToken = Jwts
                .builder()
                .setClaims(new HashMap<>())
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(signInKey, SignatureAlgorithm.HS256)
                .compact();
        Date returnedExpiration = jwtService.extractExpiration(newToken);
        assertEquals(returnedExpiration.getTime()/1000, expiration.getTime()/1000);
    }

    @Test
    void extractAllClaims(){
        Date startDate = new Date(System.currentTimeMillis());
        Date endDate = new Date(System.currentTimeMillis() + 1000 *60 *24);
        String newToken = Jwts
                .builder()
                .setClaims(new HashMap<>())
                .setSubject(user.getUsername())
                .setIssuedAt(startDate)
                .setExpiration(endDate)
                .signWith(signInKey, SignatureAlgorithm.HS256)
                .compact();

        Claims extractedClaims = jwtService.extractAllClaims(newToken);
        String returnedUsername = extractedClaims.getSubject();
        Date returnedStart = extractedClaims.getIssuedAt();
        Date returnedEnd = extractedClaims.getExpiration();
        assertEquals(returnedUsername, user.getUsername());
        assertEquals(returnedStart.getTime()/1000, startDate.getTime()/1000);
        assertEquals(returnedEnd.getTime()/1000, endDate.getTime()/1000);
    }

    @Test
    void getSignInKey() {
        try (MockedStatic<Keys> mockedKeys = Mockito.mockStatic(Keys.class)) {
            mockedKeys.when(() -> Keys.hmacShaKeyFor(any(byte[].class))).thenReturn((SecretKey) signInKey);
        }
        Key returnedKey = jwtService.getSignInKey();
        assertEquals(returnedKey, signInKey);
    }
}