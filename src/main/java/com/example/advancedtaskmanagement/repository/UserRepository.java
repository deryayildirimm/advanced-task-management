package com.example.advancedtaskmanagement.repository;

import com.example.advancedtaskmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String userName);

}
