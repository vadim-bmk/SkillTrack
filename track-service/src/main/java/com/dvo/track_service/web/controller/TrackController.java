package com.dvo.track_service.web.controller;

import com.dvo.track_service.mapper.TrackMapper;
import com.dvo.track_service.service.TrackService;
import com.dvo.track_service.web.filter.TrackFilter;
import com.dvo.track_service.web.model.request.UpdateTrackRequest;
import com.dvo.track_service.web.model.request.UpsertTrackRequest;
import com.dvo.track_service.web.model.response.TrackResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracks")
@RequiredArgsConstructor
public class TrackController {
    private final TrackService trackService;
    private final TrackMapper trackMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all tracks by filter")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<List<TrackResponse>> findAllByFilter(TrackFilter filter) {
        return ResponseEntity.ok(
                trackService.findAllByFilter(filter)
                        .stream()
                        .map(trackMapper::trackToResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get track by id")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<TrackResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                trackMapper.trackToResponse(
                        trackService.findById(id)
                )
        );
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create track")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<TrackResponse> create(@RequestBody @Valid UpsertTrackRequest request) {
        return ResponseEntity.ok(
                trackMapper.trackToResponse(
                        trackService.create(
                                trackMapper.upsertRequestToTrack(request)
                        )
                )
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update track by id")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<TrackResponse> update(@PathVariable Long id,
                                                @RequestBody @Valid UpdateTrackRequest request) {
        return ResponseEntity.ok(
                trackMapper.trackToResponse(
                        trackService.update(id, request)
                )
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete track by id")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        trackService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Set track status by id")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Void> setStatusById(@PathVariable Long id, @RequestParam String status) {
        trackService.setStatus(id, status);
        return ResponseEntity.ok().build();
    }

}
