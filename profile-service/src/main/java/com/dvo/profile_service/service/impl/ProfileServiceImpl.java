package com.dvo.profile_service.service.impl;

import com.dvo.profile_service.client.SkillClient;
import com.dvo.profile_service.client.UserClient;
import com.dvo.profile_service.client.dto.SkillDto;
import com.dvo.profile_service.client.dto.UserDto;
import com.dvo.profile_service.entity.Profile;
import com.dvo.profile_service.event.ProfileEvent;
import com.dvo.profile_service.exception.EntityNotFoundException;
import com.dvo.profile_service.mapper.ProfileMapper;
import com.dvo.profile_service.repository.ProfileRepository;
import com.dvo.profile_service.repository.ProfileSpecification;
import com.dvo.profile_service.service.ProfileService;
import com.dvo.profile_service.web.filter.ProfileFilter;
import com.dvo.profile_service.web.model.request.UpdateProfileRequest;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final UserClient userClient;
    private final SkillClient skillClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public List<Profile> findAll() {
        log.info("Call findAll in ProfileServiceImpl");
        return profileRepository.findAll();
    }

    @Override
    public List<Profile> findAllByFilter(ProfileFilter filter) {
        log.info("Call findAllByFilter in ProfileServiceImpl with filter: {}", filter);

        return profileRepository.findAll(
                ProfileSpecification.withFilter(filter),
                PageRequest.of(filter.getPageNumber(), filter.getPageSize())
        ).getContent();
    }

    @Override
    public Profile findById(Long id) {
        log.info("Call findById in ProfileServiceImpl with ID: {}", id);

        return profileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Profile with ID: {0} not found", id)));
    }

    @Override
    @Transactional
    public Profile create(Profile profile) {
        log.info("Call create in ProfileServiceImpl with profile: {}", profile);

        try {
            UserDto userDto = userClient.getUserById(profile.getUserId());
        } catch (FeignException.NotFound e) {
            throw new EntityNotFoundException(MessageFormat.format("User with ID: {0} not found", profile.getUserId()));
        }

        SkillDto skillDto;
        try {
            skillDto = skillClient.getSkillById(profile.getSkillId());

            if (!skillDto.getLevels().contains(profile.getLevel())) {
                throw new EntityNotFoundException(MessageFormat.format("Skill levels: {0} not contains level: {1}", skillDto.getLevels(), profile.getLevel()));
            }
        } catch (FeignException.NotFound e) {
            throw new EntityNotFoundException(MessageFormat.format("Skill with ID: {0} not found", profile.getSkillId()));
        }

        Profile newProfile = profileRepository.save(profile);

        ProfileEvent event = ProfileEvent.builder()
                .id(newProfile.getId())
                .skillId(newProfile.getSkillId())
                .skillName(skillDto.getName())
                .userId(newProfile.getUserId())
                .verified(newProfile.getVerified())
                .action("создан")
                .build();
        kafkaTemplate.send("profile-topic", event);
        log.info("Send to profile-topic in create event: {}", event);

        return newProfile;
    }

    @Override
    @Transactional
    public Profile update(UpdateProfileRequest request, Long id) {
        log.info("Call update in ProfileServiceImpl with request: {}, for ID: {}", request, id);

        Profile existedProfile = profileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Profile with ID: {0} not found", id)));

        profileMapper.updateRequestToProfile(request, existedProfile);
        existedProfile.setUpdatedAt(LocalDateTime.now());

        try {
            UserDto userDto = userClient.getUserById(existedProfile.getUserId());
        } catch (FeignException.NotFound e) {
            throw new EntityNotFoundException(MessageFormat.format("User with ID: {0} not found", existedProfile.getUserId()));
        }

        SkillDto skillDto;
        try {
            skillDto = skillClient.getSkillById(existedProfile.getSkillId());

            if (!skillDto.getLevels().contains(request.getLevel()) && request.getLevel() != null) {
                throw new EntityNotFoundException(MessageFormat.format("Skill levels: {0} not contains level: {1}", skillDto.getLevels(), request.getLevel()));
            }
        } catch (FeignException.NotFound e) {
            throw new EntityNotFoundException(MessageFormat.format("Skill with ID: {0} not found", existedProfile.getSkillId()));
        }

        ProfileEvent event = ProfileEvent.builder()
                .id(existedProfile.getId())
                .skillId(existedProfile.getSkillId())
                .skillName(skillDto.getName())
                .userId(existedProfile.getUserId())
                .verified(existedProfile.getVerified())
                .action("обновлен")
                .build();
        kafkaTemplate.send("profile-topic", event);
        log.info("Send to profile-topic in update event: {}", event);

        return profileRepository.save(existedProfile);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Call deleteById in ProfileServiceImpl with ID: {}", id);

        profileRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Profile setVerified(Long id, Boolean verified) {
        log.info("Call setVerified in ProfileServiceImpl for ID: {}, verified: {}", id, verified);

        Profile existedProfile = profileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Profile with ID: {0} not found", id)));

        existedProfile.setVerified(verified);


        SkillDto skillDto;
        try {
            skillDto = skillClient.getSkillById(existedProfile.getSkillId());

            if (!skillDto.getLevels().contains(existedProfile.getLevel()) && existedProfile.getLevel() != null) {
                throw new EntityNotFoundException(MessageFormat.format("Skill levels: {0} not contains level: {1}", skillDto.getLevels(), existedProfile.getLevel()));
            }
        } catch (FeignException.NotFound e) {
            throw new EntityNotFoundException(MessageFormat.format("Skill with ID: {0} not found", existedProfile.getSkillId()));
        }

        ProfileEvent event = ProfileEvent.builder()
                .id(existedProfile.getId())
                .skillId(existedProfile.getSkillId())
                .skillName(skillDto.getName())
                .userId(existedProfile.getUserId())
                .verified(existedProfile.getVerified())
                .action("изменено подтверждение")
                .build();
        kafkaTemplate.send("profile-topic", event);
        log.info("Send to profile-topic in setVerified event: {}", event);

        return profileRepository.save(existedProfile);
    }
}
