package com.dvo.track_service.mapper;

import com.dvo.track_service.entity.Track;
import com.dvo.track_service.web.model.request.UpdateTrackRequest;
import com.dvo.track_service.web.model.request.UpsertTrackRequest;
import com.dvo.track_service.web.model.response.TrackResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TrackMapper {
    TrackResponse trackToResponse(Track track);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    Track upsertRequestToTrack(UpsertTrackRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRequestToTrack(UpdateTrackRequest request, @MappingTarget Track track);
}
