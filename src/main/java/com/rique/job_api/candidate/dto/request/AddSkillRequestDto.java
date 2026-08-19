package com.rique.job_api.candidate.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AddSkillRequestDto(
        @NotNull(message = "O ID da habilidade é obrigatório")
        Long skillId,

        @NotBlank(message = "O nível da habilidade é obrigatório")
        @Size(max = 20, message = "O nível deve ter no máximo 20 caracteres")
        String level
) {
}