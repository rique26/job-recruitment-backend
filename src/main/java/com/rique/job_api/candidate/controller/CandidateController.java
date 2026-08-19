package com.rique.job_api.candidate.controller;

import com.rique.job_api.auth.entity.UserEntity;
import com.rique.job_api.candidate.dto.request.AddSkillRequestDto;
import com.rique.job_api.candidate.dto.request.CreateExperienceRequestDto;
import com.rique.job_api.candidate.dto.request.UpdateCandidateRequestDto;
import com.rique.job_api.candidate.dto.response.CandidateProfileResponseDto;
import com.rique.job_api.candidate.service.CandidateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/candidates")
@RequiredArgsConstructor
@Tag(name = "Candidate")
public class CandidateController {

    private final CandidateService candidateService;

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_CANDIDATE')")
    public CandidateProfileResponseDto getMyProfile(@AuthenticationPrincipal UserEntity user) {
        return candidateService.getMyProfile(user.getId());
    }

    @PutMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_CANDIDATE')")
    public CandidateProfileResponseDto updateMyProfile(
            @AuthenticationPrincipal UserEntity user,
            @Valid @RequestBody UpdateCandidateRequestDto dto) {
        return candidateService.updateMyProfile(user.getId(), dto);
    }

    @PostMapping("/me/experiences")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_CANDIDATE')")
    public CandidateProfileResponseDto addExperience(
            @AuthenticationPrincipal UserEntity user,
            @Valid @RequestBody CreateExperienceRequestDto dto) {
        return candidateService.addExperience(user.getId(), dto);
    }

    @DeleteMapping("/me/experiences/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_CANDIDATE')")
    public void deleteExperience(
            @AuthenticationPrincipal UserEntity user,
            @PathVariable Long id) {
        candidateService.deleteExperience(user.getId(), id);
    }

    @PostMapping("/me/skills")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_CANDIDATE')")
    public CandidateProfileResponseDto addSkill(
            @AuthenticationPrincipal UserEntity user,
            @Valid @RequestBody AddSkillRequestDto dto) {
        return candidateService.addSkill(user.getId(), dto);
    }
}
