package com.dvo.skill_service.repository;

import com.dvo.skill_service.entity.Area;
import com.dvo.skill_service.entity.Skill;
import com.dvo.skill_service.web.model.filter.SkillFilter;
import org.springframework.data.jpa.domain.Specification;

public interface SkillSpecification {

    static Specification<Skill> withFilter(SkillFilter filter) {
        return Specification.where(byName(filter.getName())
                .and(byArea(filter.getArea()))
                .and(byLevel(filter.getLevel())));
    }

    private static Specification<Skill> byName(String name) {
        return ((root, query, criteriaBuilder) -> {
            if (name == null) return null;

            return criteriaBuilder.like(root.get(Skill.Fields.name), "%" + name + "%");
        });
    }

    private static Specification<Skill> byArea(String area) {
        return ((root, query, criteriaBuilder) -> {
            if (area == null) return null;

            return criteriaBuilder.equal(root.get(Skill.Fields.area), Area.valueOf(area));
        });
    }

    private static Specification<Skill> byLevel(String level) {
        return ((root, query, criteriaBuilder) -> {
            if (level == null) return null;

            return criteriaBuilder.like(root.get(Skill.Fields.level), "%" + level + "%");
        });
    }
}
