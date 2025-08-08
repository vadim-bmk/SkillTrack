package com.dvo.progress_service.service;

import com.dvo.progress_service.entity.Progress;
import com.dvo.progress_service.web.filter.ProgressFilter;
import com.dvo.progress_service.web.model.request.UpdateTrackRequest;

import java.util.List;

public interface ProgressService {
    List<Progress> findAll();
    List<Progress> findAllByFilter(ProgressFilter filter);
    Progress findById(Long id);
    Progress create(Progress progress);
    Progress update(UpdateTrackRequest request, Long id);
    void deleteById(Long id);
    void setApproved(Long id, Boolean approved);
}
