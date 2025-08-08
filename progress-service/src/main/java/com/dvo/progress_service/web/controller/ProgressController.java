package com.dvo.progress_service.web.controller;

import com.dvo.progress_service.mapper.ProgressMapper;
import com.dvo.progress_service.service.ProgressService;
import com.dvo.progress_service.web.filter.ProgressFilter;
import com.dvo.progress_service.web.model.request.UpdateTrackRequest;
import com.dvo.progress_service.web.model.request.UpsertTrackRequest;
import com.dvo.progress_service.web.model.response.ProgressResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ProgressController {
    private final ProgressService progressService;
    private final ProgressMapper progressMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all progresses by filter")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<List<ProgressResponse>> findAllByFilter(@Valid ProgressFilter filter) {
        return ResponseEntity.ok(
                progressService.findAllByFilter(filter)
                        .stream()
                        .map(progressMapper::progressToResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get progress by id")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<ProgressResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                progressMapper.progressToResponse(
                        progressService.findById(id)
                )
        );
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create progress")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<ProgressResponse> create(@RequestBody @Valid UpsertTrackRequest request) {
        return ResponseEntity.ok(
                progressMapper.progressToResponse(
                        progressService.create(
                                progressMapper.requestToProgress(request)
                        )
                )
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update progress by id")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<ProgressResponse> update(@PathVariable Long id,
                                                   @RequestBody UpdateTrackRequest request) {
        return ResponseEntity.ok(
                progressMapper.progressToResponse(
                        progressService.update(request, id)
                )
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete progress by id")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        progressService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/approved")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Set progress approved by id")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Void> setApproved(@PathVariable Long id,
                                            @RequestParam Boolean approved) {
        progressService.setApproved(id, approved);
        return ResponseEntity.ok().build();
    }
}
