package com.rique.job_api.candidate.controller;

import com.rique.job_api.auth.entity.UserEntity;
import com.rique.job_api.candidate.dto.response.CandidateProfileResponseDto;
import com.rique.job_api.candidate.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_CANDIDATE')")
    public CandidateProfileResponseDto getMyProfile() {
        var user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return candidateService.getMyProfile(user.getId());
    }
}
