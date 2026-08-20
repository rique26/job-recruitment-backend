package com.rique.job_api.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterCompanyRequestDto(
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String password,

        @NotBlank(message = "O CNPJ é obrigatório")
        @Pattern(
                regexp = "^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$",
                message = "O CNPJ deve estar no formato XX.XXX.XXX/XXXX-XX"
        )
        String cnpj,

        @NotBlank(message = "O nome fantasia é obrigatório")
        @Size(max = 100)
        String tradeName,

        @NotBlank(message = "A razão social é obrigatória")
        @Size(max = 150)
        String corporateName,

        String description,

        @Size(max = 255)
        String websiteUrl,

        @Size(max = 50, message = "O porte da empresa deve ter no máximo 50 caracteres")
        String size
) {
}