package com.rique.job_api.company.controller;

import com.rique.job_api.auth.entity.UserEntity;
import com.rique.job_api.company.dto.request.UpdateCompanyRequestDto;
import com.rique.job_api.company.dto.response.CompanyProfileResponseDto;
import com.rique.job_api.company.service.CompanyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/companies")
@RequiredArgsConstructor
@Tag(name = "Company")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_COMPANY')")
    public CompanyProfileResponseDto getMyProfile(@AuthenticationPrincipal UserEntity user) {
        return companyService.getMyProfile(user.getId());
    }

    @PutMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_COMPANY')")
    public CompanyProfileResponseDto updateMyProfile(
            @AuthenticationPrincipal UserEntity user,
            @Valid @RequestBody UpdateCompanyRequestDto dto) {
        return companyService.updateMyProfile(user.getId(), dto);
    }
}