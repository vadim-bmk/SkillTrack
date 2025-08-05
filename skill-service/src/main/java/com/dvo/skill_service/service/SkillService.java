package com.dvo.skill_service.service;

import com.dvo.skill_service.entity.Skill;
import com.dvo.skill_service.web.model.filter.SkillFilter;
import com.dvo.skill_service.web.model.request.UpdateSkillRequest;

import java.util.List;

public interface SkillService {
    List<Skill> findAll();

    List<Skill> findAllByFilter(SkillFilter filter);

    Skill findById(Long id);

    Skill save(Skill skill);

    Skill update(UpdateSkillRequest request, Long id);

    void deleteById(Long id);
}
