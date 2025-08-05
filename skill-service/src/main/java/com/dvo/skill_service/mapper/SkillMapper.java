package com.dvo.skill_service.mapper;

import com.dvo.skill_service.entity.Skill;
import com.dvo.skill_service.web.model.request.UpdateSkillRequest;
import com.dvo.skill_service.web.model.request.UpsertSkillRequest;
import com.dvo.skill_service.web.model.response.SkillResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface SkillMapper {
    SkillResponse skillToResponse(Skill skill);

    @Mapping(target = "id", ignore = true)
    Skill requestToSkill(UpsertSkillRequest request);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRequestToSkill(UpdateSkillRequest request, @MappingTarget Skill skill);
}
