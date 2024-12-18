package com.omnichannel.messagingplatform.service;

import com.omnichannel.messagingplatform.config.AppConfig;
import com.omnichannel.messagingplatform.dto.LoginRequest;
import com.omnichannel.messagingplatform.model.User;
import com.omnichannel.messagingplatform.repository.UserRepository;
import com.omnichannel.messagingplatform.security.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AppConfig appConfig;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, AppConfig appConfig, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.appConfig = appConfig;
        this.jwtUtil = jwtUtil;
    }

    public boolean registerUser(User user) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            return false; // Username already taken
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            return false; // Email already taken
        }

        // Hash the user's password
        String hashedPassword = appConfig.passwordEncoder().encode(user.getPassword());
        user.setPassword(hashedPassword);

        // Save the user to the database
        userRepository.save(user);

        return true; // Registration successful
    }

    public String authenticateUser(LoginRequest loginRequest) throws Exception {
        // Fetch user from database by username
        Optional<User> user = userRepository.findByUsername(loginRequest.getUsername());

        // If user is not found or password does not match, throw exception
        if (user.isEmpty() ||!appConfig.passwordEncoder().matches(loginRequest.getPassword(), user.get().getPassword())) {
                throw new Exception("Invalid username or password");
        }
         return jwtUtil.generateToken(user.get());
    }
}

