package com.rique.job_api.job.dto.response;

import com.rique.job_api.job.enums.ContractType;
import com.rique.job_api.job.enums.JobStatus;
import com.rique.job_api.job.enums.WorkMode;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record JobResponseDto(
        Long id,
        String title,
        String description,
        String requirements,
        BigDecimal salary,
        WorkMode workMode,
        ContractType contractType,
        JobStatus status,
        LocalDateTime createdAt
) {
}