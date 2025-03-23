package com.example.advancedtaskmanagement.security;

public record AuthRequest(
        String username,
        String password
) {
}
