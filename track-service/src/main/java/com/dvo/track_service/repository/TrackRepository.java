package com.dvo.track_service.repository;

import com.dvo.track_service.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TrackRepository extends JpaRepository<Track, Long>, JpaSpecificationExecutor<Track> {
}
