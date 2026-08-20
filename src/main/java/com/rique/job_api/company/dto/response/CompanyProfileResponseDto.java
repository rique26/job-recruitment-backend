package com.rique.job_api.company.dto.response;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record CompanyProfileResponseDto(
        Long id,
        String name,
        String cnpj,
        String socialReason,
        String website,
        String description,
        String size,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}