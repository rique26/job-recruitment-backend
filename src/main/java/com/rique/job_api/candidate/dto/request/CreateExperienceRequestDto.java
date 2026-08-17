package com.rique.job_api.candidate.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CreateExperienceRequestDto(
        @NotBlank(message = "O nome da empresa é obrigatório")
        @Size(max = 100, message = "O nome da empresa deve ter no máximo 100 caracteres")
        String company,

        @NotBlank(message = "O cargo é obrigatório")
        @Size(max = 100, message = "O cargo deve ter no máximo 100 caracteres")
        String jobTitle,

        String description,

        @NotNull(message = "A data de início é obrigatória")
        LocalDate startDate,

        LocalDate endDate
) {}
