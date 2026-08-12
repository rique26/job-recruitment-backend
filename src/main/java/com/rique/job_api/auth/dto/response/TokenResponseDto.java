package com.rique.job_api.auth.dto.response;

import lombok.Builder;

@Builder
public record TokenResponseDto(
        String token,
        String type,
        Long expiration
) { }