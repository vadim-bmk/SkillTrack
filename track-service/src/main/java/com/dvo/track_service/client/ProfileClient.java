package com.dvo.track_service.client;

import com.dvo.track_service.client.dto.ProfileDto;
import com.dvo.track_service.configuration.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service", url = "${profile-service.url}", configuration = FeignClientConfig.class)
public interface ProfileClient {
    @PostMapping("api/profiles/create")
    ProfileDto create(@RequestBody ProfileDto profile);
}
