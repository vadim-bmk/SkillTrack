package com.dvo.track_service.repository;

import com.dvo.track_service.entity.Track;
import com.dvo.track_service.web.filter.TrackFilter;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public interface TrackSpecification {
    static Specification<Track> withFilter(TrackFilter filter) {
        return Specification.where(byUserId(filter.getUserId())
                .and(bySkillId(filter.getSkillId()))
                .and(byTargetLevel(filter.getTargetLevel()))
                .and(byDeadline(filter.getMinDeadline(), filter.getMaxDeadline()))
                .and(byStatus(filter.getStatus()))
        );
    }

    private static Specification<Track> byUserId(Long userId) {
        return ((root, query, criteriaBuilder) -> {
            if (userId == null) return null;

            return criteriaBuilder.equal(root.get(Track.Fields.userId), userId);
        });
    }

    private static Specification<Track> bySkillId(Long skillId) {
        return ((root, query, criteriaBuilder) -> {
            if (skillId == null) return null;

            return criteriaBuilder.equal(root.get(Track.Fields.skillId), skillId);
        });
    }

    private static Specification<Track> byTargetLevel(String targetLevel) {
        return ((root, query, criteriaBuilder) -> {
            if (targetLevel == null) return null;

            return criteriaBuilder.equal(root.get(Track.Fields.targetLevel), targetLevel);
        });
    }

    private static Specification<Track> byDeadline(LocalDate minDeadline, LocalDate maxDeadline) {
        return ((root, query, criteriaBuilder) -> {
            if (minDeadline == null && maxDeadline == null) return null;

            if (maxDeadline == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(Track.Fields.deadline), minDeadline);
            }

            if (minDeadline == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get(Track.Fields.deadline), maxDeadline);
            }

            return criteriaBuilder.between(root.get(Track.Fields.deadline), minDeadline, maxDeadline);
        });
    }

    private static Specification<Track> byStatus(String status) {
        return ((root, query, criteriaBuilder) -> {
            if (status == null) return null;

            return criteriaBuilder.equal(root.get(Track.Fields.status), status);
        });
    }
}
