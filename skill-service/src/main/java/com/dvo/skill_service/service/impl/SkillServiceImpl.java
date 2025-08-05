package com.dvo.skill_service.service.impl;

import com.dvo.skill_service.entity.Skill;
import com.dvo.skill_service.exception.EntityNotFoundException;
import com.dvo.skill_service.mapper.SkillMapper;
import com.dvo.skill_service.repository.SkillRepository;
import com.dvo.skill_service.repository.SkillSpecification;
import com.dvo.skill_service.service.SkillService;
import com.dvo.skill_service.web.model.filter.SkillFilter;
import com.dvo.skill_service.web.model.request.UpdateSkillRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SkillServiceImpl implements SkillService {
    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;

    @Override
    public List<Skill> findAll() {
        log.info("Call findAll in SkillServiceImpl");

        return skillRepository.findAll();
    }

    @Override
    public List<Skill> findAllByFilter(SkillFilter filter) {
        log.info("Call findAllByFilter in SkillServiceImpl with filter: {}", filter);

        return skillRepository.findAll(
                SkillSpecification.withFilter(filter),
                PageRequest.of(filter.getPageNumber(), filter.getPageSize())
        ).getContent();
    }

    @Override
    public Skill findById(Long id) {
        log.info("Call findById in SkillServiceImpl with ID: {}", id);

        return skillRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Skill with ID: {0} not found", id)));
    }

    @Override
    @Transactional
    public Skill save(Skill skill) {
        log.info("Call save in SkillServiceImpl with skill: {}", skill);

        return skillRepository.save(skill);
    }

    @Override
    @Transactional
    public Skill update(UpdateSkillRequest request, Long id) {
        log.info("Call update in SkillServiceImpl with request: {}, ID: {}", request, id);

        Skill existedSkill = skillRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Skill with ID: {0} not found", id)));

        skillMapper.updateRequestToSkill(request, existedSkill);

        return skillRepository.save(existedSkill);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Call deleteById in SkillServiceImpl with ID: {}", id);

        skillRepository.deleteById(id);
    }
}
