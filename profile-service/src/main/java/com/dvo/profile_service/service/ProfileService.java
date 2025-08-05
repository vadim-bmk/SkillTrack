package com.dvo.profile_service.service;

import com.dvo.profile_service.entity.Profile;
import com.dvo.profile_service.web.filter.ProfileFilter;
import com.dvo.profile_service.web.model.request.UpdateProfileRequest;

import java.util.List;

public interface ProfileService {
    List<Profile> findAll();

    List<Profile> findAllByFilter(ProfileFilter filter);

    Profile findById(Long id);

    Profile create(Profile profile);

    Profile update(UpdateProfileRequest request, Long id);

    void deleteById(Long id);

    Profile setVerified(Long id, Boolean verified);

}
