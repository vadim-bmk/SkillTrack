package com.dvo.track_service.web.filter;

import com.dvo.track_service.entity.TrackStatus;
import com.dvo.track_service.validation.TrackFilterValid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TrackFilterValid
public class TrackFilter {
    private Integer pageNumber;
    private Integer pageSize;
    private Long userId;
    private Long skillId;
    private String targetLevel;
    private LocalDate maxDeadline;
    private LocalDate minDeadline;
    private String status;
}
