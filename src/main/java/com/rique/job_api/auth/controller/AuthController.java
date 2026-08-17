package com.rique.job_api.auth.controller;

import com.rique.job_api.auth.dto.request.LoginRequestDto;
import com.rique.job_api.auth.dto.request.RegisterCandidateRequestDto;
import com.rique.job_api.auth.dto.request.RegisterCompanyRequestDto;
import com.rique.job_api.auth.dto.response.TokenResponseDto;
import com.rique.job_api.auth.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDto login (@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return authService.login(loginRequestDto);
    }

    @PostMapping("/register/candidate")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerCandidate(@Valid @RequestBody RegisterCandidateRequestDto dto) {
        authService.registerCandidate(dto);
    }

    @PostMapping("/register/company")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerCompany(@Valid @RequestBody RegisterCompanyRequestDto dto) {
        authService.registerCompany(dto);
    }
}