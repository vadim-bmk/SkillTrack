package com.dvo.track_service.service.impl;

import com.dvo.track_service.client.ProfileClient;
import com.dvo.track_service.client.SkillClient;
import com.dvo.track_service.client.UserClient;
import com.dvo.track_service.client.dto.ProfileDto;
import com.dvo.track_service.client.dto.SkillDto;
import com.dvo.track_service.client.dto.UserDto;
import com.dvo.track_service.entity.Track;
import com.dvo.track_service.entity.TrackStatus;
import com.dvo.track_service.event.TrackEvent;
import com.dvo.track_service.exception.EntityNotFoundException;
import com.dvo.track_service.mapper.TrackMapper;
import com.dvo.track_service.repository.TrackRepository;
import com.dvo.track_service.repository.TrackSpecification;
import com.dvo.track_service.service.TrackService;
import com.dvo.track_service.web.filter.TrackFilter;
import com.dvo.track_service.web.model.request.UpdateTrackRequest;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class TrackServiceImpl implements TrackService {
    private final TrackRepository trackRepository;
    private final TrackMapper trackMapper;
    private final UserClient userClient;
    private final SkillClient skillClient;
    private final ProfileClient profileClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public List<Track> findAll() {
        log.info("Call findAll in TrackServiceImpl");

        return trackRepository.findAll();
    }

    @Override
    public List<Track> findAllByFilter(TrackFilter filter) {
        log.info("Call findAllByFilter in TrackServiceImpl with filter: {}", filter);

        return trackRepository.findAll(
                TrackSpecification.withFilter(filter),
                PageRequest.of(filter.getPageNumber(), filter.getPageSize())
        ).getContent();
    }

    @Override
    public Track findById(Long id) {
        log.info("Call findById in TrackServiceImpl with ID: {}", id);

        return trackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Track with ID: {0} not found", id)));
    }

    @Override
    @Transactional
    public Track create(Track track) {
        log.info("Call create in TrackServiceImpl with track: {}", track);

        try {
            UserDto userDto = userClient.getUserById(track.getUserId());
        } catch (FeignException.NotFound e) {
            throw new EntityNotFoundException(MessageFormat.format("User with ID: {0} not found", track.getUserId()));
        }

        try {
            SkillDto skillDto = skillClient.getSkillById(track.getSkillId());

            if (!skillDto.getLevels().contains(track.getTargetLevel())) {
                throw new EntityNotFoundException(MessageFormat.format("Skill levels: {0} not contains level: {1}", skillDto.getLevels(), track.getTargetLevel()));
            }
        } catch (FeignException.NotFound e) {
            throw new EntityNotFoundException(MessageFormat.format("Skill with ID: {0} not found", track.getSkillId()));
        }

        Track newTrack = trackRepository.save(track);

        TrackEvent event = TrackEvent.builder()
                .id(newTrack.getId())
                .userId(newTrack.getUserId())
                .skillId(newTrack.getSkillId())
                .targetLevel(newTrack.getTargetLevel())
                .deadline(newTrack.getDeadline())
                .status(newTrack.getStatus().toString())
                .createdAt(newTrack.getCreatedAt())
                .updatedAt(newTrack.getUpdatedAt())
                .action("создан")
                .build();

        kafkaTemplate.send("track-topic", event);
        log.info("kafka send track-topic with create event: {}", event);

        return newTrack;
    }

    @Override
    @Transactional
    public Track update(Long id, UpdateTrackRequest request) {
        log.info("Call update in TrackServiceImpl for ID: {}, with request: {}", id, request);

        Track existedTrack = trackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Track with ID: {0} not found", id)));

        trackMapper.updateRequestToTrack(request, existedTrack);
        existedTrack.setUpdatedAt(LocalDateTime.now());

        try {
            UserDto userDto = userClient.getUserById(existedTrack.getUserId());
        } catch (FeignException.NotFound e) {
            throw new EntityNotFoundException(MessageFormat.format("User with ID: {0} not found", existedTrack.getUserId()));
        }

        try {
            SkillDto skillDto = skillClient.getSkillById(existedTrack.getSkillId());

            if (!skillDto.getLevels().contains(existedTrack.getTargetLevel())) {
                throw new EntityNotFoundException(MessageFormat.format("Skill levels: {0} not contains level: {1}", skillDto.getLevels(), existedTrack.getTargetLevel()));
            }
        } catch (FeignException.NotFound e) {
            throw new EntityNotFoundException(MessageFormat.format("Skill with ID: {0} not found", existedTrack.getSkillId()));
        }

        TrackEvent event = TrackEvent.builder()
                .id(existedTrack.getId())
                .userId(existedTrack.getUserId())
                .skillId(existedTrack.getSkillId())
                .targetLevel(existedTrack.getTargetLevel())
                .deadline(existedTrack.getDeadline())
                .status(existedTrack.getStatus().toString())
                .createdAt(existedTrack.getCreatedAt())
                .updatedAt(existedTrack.getUpdatedAt())
                .action("изменен")
                .build();

        kafkaTemplate.send("track-topic", event);
        log.info("kafka send track-topic with update event: {}", event);

        return trackRepository.save(existedTrack);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Call deleteById in TrackServiceImpl with ID: {}", id);

        trackRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void setStatus(Long id, String status) {
        log.info("Call setStatus in TrackServiceImpl with ID: {}, status: {}", id, status);

        Track existedTrack = trackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Track with ID: {0} not found", id)));

        existedTrack.setStatus(TrackStatus.valueOf(status));

        trackRepository.save(existedTrack);

        TrackEvent event = TrackEvent.builder()
                .id(existedTrack.getId())
                .userId(existedTrack.getUserId())
                .skillId(existedTrack.getSkillId())
                .targetLevel(existedTrack.getTargetLevel())
                .deadline(existedTrack.getDeadline())
                .status(existedTrack.getStatus().toString())
                .createdAt(existedTrack.getCreatedAt())
                .updatedAt(existedTrack.getUpdatedAt())
                .action("обновлен статус")
                .build();

        kafkaTemplate.send("track-topic", event);
        log.info("kafka send track-topic with update event status: {}", event);

        if (existedTrack.getStatus().toString().equals("DONE")) {
            ProfileDto profileDto = ProfileDto.builder()
                    .userId(existedTrack.getUserId())
                    .skillId(existedTrack.getSkillId())
                    .level(existedTrack.getTargetLevel())
                    .verified(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            profileClient.create(profileDto);
        }
    }
}
