package com.rique.job_api.auth.controller;

import com.rique.job_api.auth.dto.request.RegisterCompanyRequestDto;
import com.rique.job_api.auth.service.AuthService;
import com.rique.job_api.auth.dto.request.RegisterCandidateRequestDto;
import com.rique.job_api.exception.BadRequestException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/candidate")
    public ResponseEntity<Void> registerCandidate(@Valid @RequestBody RegisterCandidateRequestDto dto) throws BadRequestException {
        authService.registerCandidate(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/register/company")
    public ResponseEntity<Void> registerCompany(@Valid @RequestBody RegisterCompanyRequestDto dto) throws BadRequestException {
        authService.registerCompany(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}