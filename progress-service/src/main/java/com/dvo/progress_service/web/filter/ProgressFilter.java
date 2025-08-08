package com.dvo.progress_service.web.filter;

import com.dvo.progress_service.validation.ProgressFilterValid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ProgressFilterValid
public class ProgressFilter {
    private Integer pageNumber;
    private Integer pageSize;
    private Long trackId;
    private Boolean approved;
}
