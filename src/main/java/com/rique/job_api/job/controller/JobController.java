package com.rique.job_api.job.controller;

import com.rique.job_api.auth.entity.UserEntity;
import com.rique.job_api.job.dto.request.CreateJobRequestDto;
import com.rique.job_api.job.dto.response.JobResponseDto;
import com.rique.job_api.job.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_COMPANY')")
    public JobResponseDto createJob(
            @AuthenticationPrincipal UserEntity user,
            @Valid @RequestBody CreateJobRequestDto dto) {
        return jobService.createJob(user.getId(), dto);
    }
}