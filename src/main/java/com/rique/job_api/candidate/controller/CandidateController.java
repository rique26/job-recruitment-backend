package com.rique.job_api.candidate.controller;

import com.rique.job_api.auth.entity.UserEntity;
import com.rique.job_api.candidate.dto.request.CreateExperienceRequestDto;
import com.rique.job_api.candidate.dto.request.UpdateCandidateRequestDto;
import com.rique.job_api.candidate.dto.response.CandidateProfileResponseDto;
import com.rique.job_api.candidate.service.CandidateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/candidates")
@RequiredArgsConstructor
@Tag(name = "Candidatos", description = "Candidate")
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

    @PostMapping("/me/experiences")
    @PreAuthorize("hasRole('ROLE_CANDIDATE')")
    public CandidateProfileResponseDto addExperience(
            @AuthenticationPrincipal UserEntity user,
            @Valid @RequestBody CreateExperienceRequestDto dto) {
        return candidateService.addExperience(user.getId(), dto);
    }

    @DeleteMapping("/me/experiences/{id}")
    @PreAuthorize("hasRole('ROLE_CANDIDATE')")
    public void deleteExperience(
            @AuthenticationPrincipal UserEntity user,
            @PathVariable Long id) {
        candidateService.deleteExperience(user.getId(), id);
    }


}
