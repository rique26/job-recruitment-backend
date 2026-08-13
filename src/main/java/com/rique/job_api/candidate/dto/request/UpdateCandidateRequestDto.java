package com.rique.job_api.candidate.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateCandidateRequestDto (
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String name,

        @NotBlank(message = "O telefone é obrigatório")
        @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
        String phone,

        @Size(max = 1000, message = "O resumo profissional deve ter no máximo 1000 caracteres")
        String professionalSummary,

        @Size(max = 255, message = "A URL do LinkedIn deve ter no máximo 255 caracteres")
        String linkedinUrl
) {}
