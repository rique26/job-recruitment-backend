package com.rique.job_api.candidate.dto.response;

import lombok.Builder;

@Builder
public record CandidateSkillResponseDto(
        Long skillId,
        String name,
        String level
) {}
