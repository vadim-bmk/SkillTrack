package com.dvo.user_service.repository;

import com.dvo.user_service.entity.User;
import com.dvo.user_service.web.model.filter.UserFilter;
import org.springframework.data.jpa.domain.Specification;

public interface UserSpecification {

    static Specification<User> withFilter(UserFilter filter) {
        return Specification.where(byUsername(filter.getUsername())
                .and(byEmail(filter.getEmail()))
                .and(byPosition(filter.getPosition()))
        );
    }

    private static Specification<User> byUsername(String username) {
        return ((root, query, criteriaBuilder) -> {
            if (username == null) return null;

            return criteriaBuilder.equal(root.get(User.Fields.username), username);
        });
    }

    private static Specification<User> byEmail(String email) {
        return ((root, query, criteriaBuilder) -> {
            if (email == null) return null;

            return criteriaBuilder.equal(root.get(User.Fields.email), email);
        });
    }

    private static Specification<User> byPosition(String position) {
        return ((root, query, criteriaBuilder) -> {
            if (position == null) return null;

            return criteriaBuilder.like(root.get(User.Fields.position), "%" + position + "%");
        });
    }
}
