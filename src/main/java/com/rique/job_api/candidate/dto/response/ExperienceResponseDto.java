package com.rique.job_api.candidate.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ExperienceResponseDto(
        Long id,
        String company,
        String jobTitle,
        String description,
        LocalDate startDate,
        LocalDate endDate
) {}
