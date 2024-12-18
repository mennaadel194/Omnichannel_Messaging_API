package com.omnichannel.messagingplatform.controller;

import com.omnichannel.messagingplatform.dto.LoginRequest;
import com.omnichannel.messagingplatform.model.User;
import com.omnichannel.messagingplatform.repository.UserRepository;
import com.omnichannel.messagingplatform.security.JwtUtil;
import com.omnichannel.messagingplatform.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;
    @Mock
    private JwtUtil tokenProvider;

    private User user;
    private LoginRequest loginRequest;


    @BeforeEach
    public void setUp() {
        user = new User(1L, "john_doe", "john@example.com", "password123");
        loginRequest = new LoginRequest("john_doe", passwordEncoder.encode("password123"));

    }

    @Test
    public void testRegisterUser() {
        when(passwordEncoder.encode(user.getPassword())).thenReturn("hashedPassword");

        User savedUser = new User(1L, "john_doe", "john@example.com", "hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        Boolean registeredUser = userService.registerUser(user);

        assertNotNull(registeredUser);
        assertEquals(true, registeredUser);
        assertNotEquals(false, registeredUser);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testLoginUser() throws Exception {
        when(userService.authenticateUser(loginRequest)).thenReturn("jwt-token-here");
        when(passwordEncoder.matches(user.getPassword(), user.getPassword())).thenReturn(true);
        when(tokenProvider.generateToken(user)).thenReturn("jwt-token-here");

        String token = String.valueOf(userController.loginUser(loginRequest));

        assertEquals("jwt-token-here", token);
        verify(userService, times(1)).authenticateUser(loginRequest);
    }

}
