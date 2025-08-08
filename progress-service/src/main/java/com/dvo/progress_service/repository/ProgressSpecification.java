package com.dvo.progress_service.repository;

import com.dvo.progress_service.entity.Progress;
import com.dvo.progress_service.web.filter.ProgressFilter;
import org.springframework.data.jpa.domain.Specification;

public interface ProgressSpecification {
    static Specification<Progress> withFilter(ProgressFilter filter) {
        return Specification.where(byTrackId(filter.getTrackId())
                .and(byApproved(filter.getApproved()))
        );
    }

    private static Specification<Progress> byTrackId(Long trackId) {
        return ((root, query, criteriaBuilder) -> {
            if (trackId == null) return null;

            return criteriaBuilder.equal(root.get(Progress.Fields.trackId), trackId);
        });
    }

    private static Specification<Progress> byApproved(Boolean approved) {
        return ((root, query, criteriaBuilder) -> {
            if (approved == null) return null;

            return criteriaBuilder.equal(root.get(Progress.Fields.approved), approved);
        });
    }
}
