package com.ahmaide.trainingtaskmongo.services;

import com.ahmaide.trainingtaskmongo.config.TokenExtractorFilter;
import com.ahmaide.trainingtaskmongo.entites.User;
import com.ahmaide.trainingtaskmongo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService{

    private final UserRepository userRepository;

    private final TokenExtractorFilter tokenExtractorFilter;

    private final PasswordEncoder passwordEncoder;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User extractUserFromToken(){
        String username = tokenExtractorFilter.getUsername();
        return findByUsername(username);
    }

    public User save(User user) {
        return userRepository.insert(user);
    }

    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    public boolean sameUserInfo(User user1, User user2){
        return Objects.equals(user1.getId(), user2.getId()) || !user1.getUsername().equals(user2.getUsername());
    }

    public void encodePassword(User user){
        String encoded  = passwordEncoder.encode(user.getPassword());
        user.setPassword(encoded);
    }

}
