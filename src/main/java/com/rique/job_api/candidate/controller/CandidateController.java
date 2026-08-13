package com.rique.job_api.candidate.controller;

import com.rique.job_api.auth.entity.UserEntity;
import com.rique.job_api.candidate.dto.request.UpdateCandidateRequestDto;
import com.rique.job_api.candidate.dto.response.CandidateProfileResponseDto;
import com.rique.job_api.candidate.service.CandidateService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_CANDIDATE')")
    public CandidateProfileResponseDto getMyProfile(@AuthenticationPrincipal UserEntity user) {
        return candidateService.getMyProfile(user.getId());
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('ROLE_CANDIDATE')")
    public CandidateProfileResponseDto updateMyProfile(
            @AuthenticationPrincipal UserEntity user,
            @Valid @RequestBody UpdateCandidateRequestDto dto) {
        return candidateService.updateMyProfile(user.getId(), dto);
    }

}
