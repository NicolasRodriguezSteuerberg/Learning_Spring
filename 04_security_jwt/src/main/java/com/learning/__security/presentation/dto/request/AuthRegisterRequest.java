package com.learning.__security.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record AuthRegisterRequest(
        @NotBlank String username,
        @NotBlank String password,
        @Valid AuthRoleRequest roleRequest

) {

}