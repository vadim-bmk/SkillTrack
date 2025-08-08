package com.dvo.progress_service.repository;

import com.dvo.progress_service.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProgressRepository extends JpaRepository<Progress, Long>, JpaSpecificationExecutor<Progress> {
    Boolean existsByTrackIdAndApproved(Long trackId, Boolean approved);

    @Query("SELECT CASE WHEN COUNT(p) = 0 THEN true ELSE false END FROM Progress p WHERE p.trackId = :trackId AND (p.approved = false OR p.approved IS NULL)")
    boolean allApprovedByTrackId(@Param("trackId") Long trackId);
}
