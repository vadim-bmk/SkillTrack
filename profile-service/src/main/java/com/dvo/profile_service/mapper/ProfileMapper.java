package com.dvo.profile_service.mapper;

import com.dvo.profile_service.entity.Profile;
import com.dvo.profile_service.web.model.request.UpdateProfileRequest;
import com.dvo.profile_service.web.model.request.UpsertProfileRequest;
import com.dvo.profile_service.web.model.response.ProfileResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProfileMapper {
    ProfileResponse profileToResponse(Profile profile);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "verified", ignore = true)
    Profile requestToProfile(UpsertProfileRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "verified", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRequestToProfile(UpdateProfileRequest request, @MappingTarget Profile profile);
}
