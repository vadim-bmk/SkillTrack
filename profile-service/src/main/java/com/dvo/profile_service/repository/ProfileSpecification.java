package com.dvo.profile_service.repository;

import com.dvo.profile_service.entity.Profile;
import com.dvo.profile_service.web.filter.ProfileFilter;
import org.springframework.data.jpa.domain.Specification;

public interface ProfileSpecification {
    static Specification<Profile> withFilter(ProfileFilter filter) {
        return Specification.where(byUserId(filter.getUserId())
                .and(bySkillId(filter.getSkillId()))
                .and(byLevel(filter.getLevel()))
                .and(byVerified(filter.getVerified()))
        );
    }

    private static Specification<Profile> byUserId(Long userId) {
        return ((root, query, criteriaBuilder) -> {
            if (userId == null) return null;

            return criteriaBuilder.equal(root.get(Profile.Fields.userId), userId);
        });
    }

    private static Specification<Profile> bySkillId(Long skillId) {
        return ((root, query, criteriaBuilder) -> {
            if (skillId == null) return null;

            return criteriaBuilder.equal(root.get(Profile.Fields.skillId), skillId);
        });
    }

    private static Specification<Profile> byLevel(String level) {
        return ((root, query, criteriaBuilder) -> {
            if (level == null) return null;

            return criteriaBuilder.equal(root.get(Profile.Fields.level), level);
        });
    }

    private static Specification<Profile> byVerified(Boolean verified) {
        return ((root, query, criteriaBuilder) -> {
            if (verified == null) return null;

            return criteriaBuilder.equal(root.get(Profile.Fields.verified), verified);
        });
    }

}
