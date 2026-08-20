package com.rique.job_api.company.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateCompanyRequestDto(
        @NotBlank(message = "A razão social é obrigatória")
        @Size(max = 150, message = "A razão social deve ter no máximo 150 caracteres")
        String socialReason,

        @Size(max = 255, message = "O website deve ter no máximo 255 caracteres")
        String website,

        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
        String description,

        @Size(max = 50, message = "O porte da empresa deve ter no máximo 50 caracteres")
        String size
) {
}