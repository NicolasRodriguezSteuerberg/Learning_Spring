package com.learning.__security.presentation.dto.request;

import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public record AuthRoleRequest(
        @Size(max = 3, message = "User cant have more than 3 roles")
        List<String> roleListName
) {
}
