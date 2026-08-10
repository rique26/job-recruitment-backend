package com.rique.job_api.auth.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    
    CANDIDATE("ROLE_CANDIDATE"),
    COMPANY("ROLE_COMPANY");

    private final String authority;
}