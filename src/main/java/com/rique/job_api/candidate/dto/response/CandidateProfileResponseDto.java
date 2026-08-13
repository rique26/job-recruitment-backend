package com.rique.job_api.candidate.dto.response;

import lombok.Builder;
import java.util.List;

@Builder
public record CandidateProfileResponseDto(
        Long id,
        String name,
        String cpf,
        String phone,
        String professionalSummary,
        String linkedinUrl,
        List<CandidateSkillResponseDto> skills,
        List<ExperienceResponseDto> experiences
) {}

