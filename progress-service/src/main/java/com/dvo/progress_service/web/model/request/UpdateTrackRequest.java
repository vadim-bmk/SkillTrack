package com.dvo.progress_service.web.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTrackRequest {
    private Long trackId;
    private String comment;
    private URL resource;
}
