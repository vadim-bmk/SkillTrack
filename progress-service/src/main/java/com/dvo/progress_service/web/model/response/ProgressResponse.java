package com.dvo.progress_service.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgressResponse {
    private Long id;
    private Long trackId;
    private String comment;
    private URL resource;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean approved;
}
