package com.dvo.user_service.web.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {
    private String username;

    @Email
    private String email;

    @Size(min = 5, message = "Password must be at least 5 characters long")
    private String password;

    private String position;
    private String roleType;
    private String telegramId;
}
