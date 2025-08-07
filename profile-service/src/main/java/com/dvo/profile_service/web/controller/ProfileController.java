package com.dvo.profile_service.web.controller;

import com.dvo.profile_service.mapper.ProfileMapper;
import com.dvo.profile_service.service.ProfileService;
import com.dvo.profile_service.web.filter.ProfileFilter;
import com.dvo.profile_service.web.model.request.UpdateProfileRequest;
import com.dvo.profile_service.web.model.request.UpsertProfileRequest;
import com.dvo.profile_service.web.model.response.ProfileResponse;
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
@RequiredArgsConstructor
@RequestMapping("/api/profiles")
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {
    private final ProfileService profileService;
    private final ProfileMapper profileMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all profiles by filter")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<List<ProfileResponse>> findAllByFilter(@Valid ProfileFilter filter) {
        return ResponseEntity.ok(
                profileService.findAllByFilter(filter)
                        .stream()
                        .map(profileMapper::profileToResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get profile by id")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<ProfileResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                profileMapper.profileToResponse(
                        profileService.findById(id)
                )
        );
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create profile")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<ProfileResponse> create(@RequestBody @Valid UpsertProfileRequest request) {
        return ResponseEntity.ok(
                profileMapper.profileToResponse(
                        profileService.create(
                                profileMapper.requestToProfile(request)
                        )
                )
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update profile by id")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<ProfileResponse> update(@PathVariable Long id,
                                                  @RequestBody @Valid UpdateProfileRequest request) {
        return ResponseEntity.ok(
                profileMapper.profileToResponse(
                        profileService.update(request, id)
                )
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete profile by id")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        profileService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/verified")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Set profile verified by id")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<ProfileResponse> setVerified(@PathVariable Long id,
                                                       @RequestParam Boolean verified) {
        return ResponseEntity.ok(
                profileMapper.profileToResponse(
                        profileService.setVerified(id, verified)
                )
        );
    }
}
