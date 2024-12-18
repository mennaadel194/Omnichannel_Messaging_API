package com.omnichannel.messagingplatform.config;


import com.omnichannel.messagingplatform.repository.UserRepository;
import com.omnichannel.messagingplatform.security.CustomUserDetailsService;
import com.omnichannel.messagingplatform.security.JwtAuthenticationFilter;
import com.omnichannel.messagingplatform.security.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    private final JwtUtil jwtUtil;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserRepository userRepository;

    public SecurityConfig(JwtUtil jwtUtil, JwtAuthenticationFilter jwtAuthenticationFilter, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userRepository = userRepository;
    }


    // Configuring AuthenticationManager Bean
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }
    // Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // You can also use another encoder, but BCrypt is common
    }

    // Security Filter Chain Configuration (Spring Security 6.x)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers("/api/register", "/api/login").permitAll()  // Allow registration and login endpoints
                .requestMatchers(HttpMethod.GET, "/api/messages").authenticated()  // Secure GET messages endpoint
                .requestMatchers(HttpMethod.POST, "/api/messages").authenticated()  // Secure POST messages endpoint
                .anyRequest().authenticated()  // Other requests should be authenticated
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)  // Add the JWT filter
                .csrf().disable();  // Explicitly disable CSRF protection for REST APIs

        return http.build();
    }

    // Define UserDetailsService Bean for user-based authentication (optional)
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(userRepository);
    }

    // WebSecurityCustomizer allows to customize the default security configuration for resources and static content
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/static/**", "/public/**");  // Exclude static resources if needed
    }
}
