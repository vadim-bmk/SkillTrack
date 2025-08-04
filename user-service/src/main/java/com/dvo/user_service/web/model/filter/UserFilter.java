package com.dvo.user_service.web.model.filter;

import com.dvo.user_service.validation.UserFilterValid;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@UserFilterValid
public class UserFilter {
    private Integer pageNumber;
    private Integer pageSize;
    private String username;
    private String email;
    private String position;
}
