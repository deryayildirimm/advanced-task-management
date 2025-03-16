package com.example.advancedtaskmanagement;


import com.example.advancedtaskmanagement.dto.CreateUserRequest;
import com.example.advancedtaskmanagement.model.Role;
import com.example.advancedtaskmanagement.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Set;


@SpringBootApplication
public class AdvancedTaskManagementApplication implements CommandLineRunner {

    private final UserService userService;

    public AdvancedTaskManagementApplication(UserService userService) {
        this.userService = userService;
    }

    public static void main(String[] args) {
        SpringApplication.run(AdvancedTaskManagementApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        createDummyData();
    }

    private void createDummyData() {

        if (userService.countUsers() > 0) {
            System.out.println("📌 Dummy veriler zaten mevcut, ekleme yapılmadı.");
            return;
        }

        CreateUserRequest request = CreateUserRequest.builder()
                .name("Emin")
                .username("emin")
                .password("pass")
                .roles(Set.of(Role.ROLE_USER))
                .build();
        userService.createUser(request);

        CreateUserRequest request2 = CreateUserRequest.builder()
                .name("FSK")
                .username("fsk")
                .password("pass")
                .roles(Set.of(Role.ROLE_FSK))
                .build();
        userService.createUser(request2);


        CreateUserRequest request3 = CreateUserRequest.builder()
                .name("No Name")
                .username("noname")
                .password("pass")
                .roles(Set.of(Role.ROLE_ADMIN))
                .build();
        userService.createUser(request3);
    }
    }

