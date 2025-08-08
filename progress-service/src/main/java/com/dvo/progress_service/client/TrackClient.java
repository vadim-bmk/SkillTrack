package com.dvo.progress_service.client;

import com.dvo.progress_service.client.dto.TrackDto;
import com.dvo.progress_service.configuration.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "track-service", url = "${track-service.url}", configuration = FeignClientConfig.class)
public interface TrackClient {
    @GetMapping("/api/tracks/{id}")
    TrackDto getTrackById(@PathVariable Long id);

    @PutMapping("/api/tracks/{id}/status")
    void setStatusTrack(@PathVariable Long id, @RequestParam String status);
}
