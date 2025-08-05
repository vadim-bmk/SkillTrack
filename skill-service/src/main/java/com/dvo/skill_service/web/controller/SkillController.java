package com.dvo.skill_service.web.controller;

import com.dvo.skill_service.entity.Skill;
import com.dvo.skill_service.mapper.SkillMapper;
import com.dvo.skill_service.service.SkillService;
import com.dvo.skill_service.web.model.filter.SkillFilter;
import com.dvo.skill_service.web.model.request.UpdateSkillRequest;
import com.dvo.skill_service.web.model.request.UpsertSkillRequest;
import com.dvo.skill_service.web.model.response.SkillResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class SkillController {
    private final SkillService skillService;
    private final SkillMapper skillMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all skills by filter")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<List<SkillResponse>> findAllByFilter(@Valid SkillFilter filter) {
        List<Skill> skills = skillService.findAllByFilter(filter);

        return ResponseEntity.ok(skills
                .stream()
                .map(skillMapper::skillToResponse)
                .toList()
        );
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get skill by ID")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<SkillResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                skillMapper.skillToResponse(
                        skillService.findById(id)
                )
        );
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create skill")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<SkillResponse> create(@RequestBody @Valid UpsertSkillRequest request) {
        return ResponseEntity.ok(
                skillMapper.skillToResponse(
                        skillService.save(
                                skillMapper.requestToSkill(request)
                        )
                )
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update skill by ID")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<SkillResponse> update(@PathVariable Long id,
                                                @RequestBody UpdateSkillRequest request) {
        return ResponseEntity.ok(
                skillMapper.skillToResponse(
                        skillService.update(request, id)
                )
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete skill by ID")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        skillService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
