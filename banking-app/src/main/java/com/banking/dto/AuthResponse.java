package com.banking.dto;

import com.banking.entity.Role;

public record AuthResponse(String token, Role role, String fullName) {
}
