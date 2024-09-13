package com.personal.project.api.controller.dto.user;

import jakarta.validation.constraints.NotBlank;

public record RequestUserLoginDTO (
        @NotBlank(message = "Login should not be empty or null.") String login,
        @NotBlank(message = "Password should not be empty or null.") String password){
}
