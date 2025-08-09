package com.dvo.progress_service.service.impl;

import com.dvo.progress_service.client.TrackClient;
import com.dvo.progress_service.client.dto.TrackDto;
import com.dvo.progress_service.entity.Progress;
import com.dvo.progress_service.event.ProgressEvent;
import com.dvo.progress_service.exception.EntityNotFoundException;
import com.dvo.progress_service.mapper.ProgressMapper;
import com.dvo.progress_service.repository.ProgressRepository;
import com.dvo.progress_service.repository.ProgressSpecification;
import com.dvo.progress_service.service.ProgressService;
import com.dvo.progress_service.web.filter.ProgressFilter;
import com.dvo.progress_service.web.model.request.UpdateTrackRequest;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProgressServiceImpl implements ProgressService {
    private final ProgressRepository progressRepository;
    private final ProgressMapper progressMapper;
    private final TrackClient trackClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public List<Progress> findAll() {
        log.info("Call findAll in ProgressServiceImpl");

        return progressRepository.findAll();
    }

    @Override
    public List<Progress> findAllByFilter(ProgressFilter filter) {
        log.info("Call findAllByFilter in ProgressServiceImpl with filter: {}", filter);

        return progressRepository.findAll(
                ProgressSpecification.withFilter(filter),
                PageRequest.of(filter.getPageNumber(), filter.getPageSize())
        ).getContent();
    }

    @Override
    public Progress findById(Long id) {
        log.info("Call findById in ProgressServiceImpl with ID: {}", id);

        return progressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Progress with ID: {0} not found", id)));
    }

    @Override
    @Transactional
    public Progress create(Progress progress) {
        TrackDto trackDto;
        try {
            trackDto = trackClient.getTrackById(progress.getTrackId());
        } catch (FeignException.NotFound e) {
            throw new EntityNotFoundException(MessageFormat.format("Track with ID: {0} not found", progress.getTrackId()));
        }

        Progress newProgress = progressRepository.save(progress);

        ProgressEvent event = ProgressEvent.builder()
                .id(newProgress.getId())
                .trackId(newProgress.getTrackId())
                .createdAt(newProgress.getCreatedAt())
                .userId(trackDto.getUserId())
                .approved(newProgress.getApproved())
                .action("создан")
                .build();
        kafkaTemplate.send("progress-topic", event);
        log.info("Send to progress-topic in create event: {}", event);

        return newProgress;
    }

    @Override
    @Transactional
    public Progress update(UpdateTrackRequest request, Long id) {
        log.info("Call update in ProgressServiceImpl with ID: {}, request: {}", id, request);

        Progress existedProgress = progressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Progress with ID: {0} not found", id)));

        progressMapper.updateRequestToProgress(request, existedProgress);

        Long userId = 0L;
        if (existedProgress.getTrackId() != null) {
            try {
                TrackDto trackDto = trackClient.getTrackById(existedProgress.getTrackId());
                userId = trackDto.getUserId();
            } catch (FeignException.NotFound e) {
                throw new EntityNotFoundException(MessageFormat.format("Track with ID: {0} not found", existedProgress.getTrackId()));
            }
        }

        ProgressEvent event = ProgressEvent.builder()
                .id(existedProgress.getId())
                .trackId(existedProgress.getTrackId())
                .approved(existedProgress.getApproved())
                .userId(userId)
                .action("обновлен")
                .build();
        kafkaTemplate.send("progress-topic", event);
        log.info("Send to progress-topic in update event: {}", event);

        return existedProgress;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Call deleteById in ProgressServiceImpl");

        progressRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void setApproved(Long id, Boolean approved) {
        log.info("Call setApproved in ProgressServiceImpl with ID: {}, approved: {}", id, approved);

        Progress existedProgress = progressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Progress with ID: {0} not found", id)));

        existedProgress.setApproved(approved);

        TrackDto trackDto;
        try {
            trackDto = trackClient.getTrackById(existedProgress.getTrackId());
        } catch (FeignException.NotFound e) {
            throw new EntityNotFoundException(MessageFormat.format("Track with ID: {0} not found", existedProgress.getTrackId()));
        }

        ProgressEvent event = ProgressEvent.builder()
                .id(existedProgress.getId())
                .trackId(existedProgress.getTrackId())
                .approved(existedProgress.getApproved())
                .userId(trackDto.getUserId())
                .action("изменен статус")
                .build();
        kafkaTemplate.send("progress-topic", event);
        log.info("Send to progress-topic in setApproved event: {}", event);

        if (progressRepository.allApprovedByTrackId(existedProgress.getTrackId()) ) {
            log.info("All approved by track ID: {}", existedProgress.getTrackId());
            try {
                trackClient.setStatusTrack(existedProgress.getTrackId(), "DONE");
            } catch (FeignException.NotFound e) {
                throw new EntityNotFoundException(MessageFormat.format("Track with ID: {0} not found", id));
            }
        }

        progressRepository.save(existedProgress);
    }
}
