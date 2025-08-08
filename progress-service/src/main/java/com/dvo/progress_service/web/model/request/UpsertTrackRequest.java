package com.dvo.progress_service.web.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertTrackRequest {
    @NotNull(message = "Track ID is required")
    private Long trackId;
    @NotNull(message = "User ID is required")
    private String comment;
    @NotNull(message = "Resource is required")
    private URL resource;
}
