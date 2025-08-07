package com.dvo.track_service.service;

import com.dvo.track_service.entity.Track;
import com.dvo.track_service.web.filter.TrackFilter;
import com.dvo.track_service.web.model.request.UpdateTrackRequest;

import java.util.List;

public interface TrackService {
    List<Track> findAll();

    List<Track> findAllByFilter(TrackFilter filter);

    Track findById(Long id);

    Track create(Track track);

    Track update(Long id, UpdateTrackRequest request);

    void deleteById(Long id);

    void setStatus(Long id, String status);

}
