package com.rique.job_api.job.dto.request;

import com.rique.job_api.job.enums.ContractType;
import com.rique.job_api.job.enums.JobStatus;
import com.rique.job_api.job.enums.WorkMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CreateJobRequestDto(
        @NotBlank(message = "O título é obrigatório")
        @Size(max = 120, message = "O título deve ter no máximo 120 caracteres")
        String title,

        @NotBlank(message = "A descrição é obrigatória")
        String description,

        String requirements,

        BigDecimal salary,

        @NotNull(message = "A modalidade de trabalho é obrigatória")
        WorkMode workMode,

        @NotNull(message = "O tipo de contrato é obrigatório")
        ContractType contractType,

        JobStatus status
) {
}