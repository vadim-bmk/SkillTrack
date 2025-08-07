package com.dvo.track_service.client;

import com.dvo.track_service.client.dto.SkillDto;
import com.dvo.track_service.configuration.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "skill-service", url = "${skill-service.url}", configuration = FeignClientConfig.class)
public interface SkillClient {
    @GetMapping("/api/skills/{id}")
    SkillDto getSkillById(@PathVariable Long id);
}
