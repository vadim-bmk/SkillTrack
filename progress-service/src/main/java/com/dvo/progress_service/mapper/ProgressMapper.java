package com.dvo.progress_service.mapper;

import com.dvo.progress_service.entity.Progress;
import com.dvo.progress_service.web.model.request.UpdateTrackRequest;
import com.dvo.progress_service.web.model.request.UpsertTrackRequest;
import com.dvo.progress_service.web.model.response.ProgressResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProgressMapper {
    ProgressResponse progressToResponse(Progress progress);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "approved", ignore = true)
    Progress requestToProgress(UpsertTrackRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "approved", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRequestToProgress(UpdateTrackRequest request, @MappingTarget Progress progress);
}
