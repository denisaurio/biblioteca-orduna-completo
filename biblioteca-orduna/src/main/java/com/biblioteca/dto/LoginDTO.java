package com.biblioteca.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/** RF05 - Body de POST /api/auth/login */
public record LoginDTO(
        @NotBlank @Email String email,
        @NotBlank String password
) {}
