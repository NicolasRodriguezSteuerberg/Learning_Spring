package com.learning.__security.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(
        @NotBlank String username,
        @NotBlank String password
) {
}
