package com.dvo.profile_service.web.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileFilter {
    private Integer pageNumber;
    private Integer pageSize;
    private Long userId;
    private Long skillId;
    private String level;
    private Boolean verified;
}
