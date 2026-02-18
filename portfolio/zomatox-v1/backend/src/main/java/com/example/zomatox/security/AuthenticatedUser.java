package com.example.zomatox.security;

import com.example.zomatox.entity.enums.UserRole;

public record AuthenticatedUser(Long id, String email, UserRole role) {}
