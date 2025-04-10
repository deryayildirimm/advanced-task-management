package com.example.advancedtaskmanagement.config;

import com.example.advancedtaskmanagement.security.JwtAuthFilter;
import com.example.advancedtaskmanagement.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;


    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserService userService, PasswordEncoder passwordEncoder) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(x ->
                        x.requestMatchers("/" ,"/api/auth/**","/auth/welcome/**", "v1/projects/**" , "/auth/addNewUser/**", "/auth/generateToken/**" , "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                )
                .authorizeHttpRequests(x ->
                        x.requestMatchers("/auth/user").authenticated()
                                .requestMatchers("/auth/admin").hasRole("ADMIN")
                                .requestMatchers("/v1/projects/**").hasAnyRole("PROJECT_MANAGER", "PROJECT_GROUP_MANAGER")
                                .requestMatchers("/v1/tasks/**").hasAnyRole("PROJECT_MANAGER", "PROJECT_GROUP_MANAGER", "TEAM_MEMBER")
                                .requestMatchers("/v1/users/**").hasRole("PROJECT_GROUP_MANAGER")
                                .requestMatchers("/v1/task-comments/**").hasAnyRole("PROJECT_MANAGER", "TEAM_MEMBER")
                                .requestMatchers("/v1/task-progress/**").hasAnyRole("PROJECT_MANAGER", "TEAM_MEMBER")
                                .requestMatchers("/v1/task-attachments/**").hasAnyRole("PROJECT_MANAGER", "TEAM_MEMBER")

                )
                .sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();

    }

}
