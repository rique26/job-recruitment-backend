package com.rique.job_api.auth.service;

import com.rique.job_api.auth.dto.request.LoginRequestDto;
import com.rique.job_api.auth.dto.request.RegisterCandidateRequestDto;
import com.rique.job_api.auth.dto.request.RegisterCompanyRequestDto;
import com.rique.job_api.auth.dto.response.TokenResponseDto;
import com.rique.job_api.auth.entity.UserEntity;
import com.rique.job_api.auth.enums.UserRole;
import com.rique.job_api.auth.repository.UserRepository;
import com.rique.job_api.candidate.entity.CandidateEntity;
import com.rique.job_api.candidate.repository.CandidateRepository;
import com.rique.job_api.common.util.SanitizationUtil;
import com.rique.job_api.company.entity.CompanyEntity;
import com.rique.job_api.company.repository.CompanyRepository;
import com.rique.job_api.config.TokenProvider;
import com.rique.job_api.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final CompanyRepository companyRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    @Value("${jwt.expiration}")
    private long expirationTime;

    public TokenResponseDto login(LoginRequestDto dto) throws BadRequestException {
        try {
            String sanitizedEmail = SanitizationUtil.email(dto.email());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(sanitizedEmail, dto.password())
            );

            String token = tokenProvider.generateToken(authentication);

            return TokenResponseDto.builder()
                    .token(token)
                    .type("Bearer")
                    .expiration(expirationTime)
                    .build();
        } catch (BadCredentialsException e) {
            throw new BadRequestException("E-mail ou senha inválidos");
        }
    }

    @Transactional
    public void registerCandidate(RegisterCandidateRequestDto dto) throws BadRequestException {
        String sanitizedEmail = SanitizationUtil.email(dto.email());
        String sanitizedCpf = SanitizationUtil.removeNonDigits(dto.cpf());
        String sanitizedPhone = SanitizationUtil.removeNonDigits(dto.phone());

        validateEmailUniqueness(sanitizedEmail);

        if (candidateRepository.existsByCpf(sanitizedCpf)) {
            throw new BadRequestException("CPF já cadastrado");
        }

        if (candidateRepository.existsByPhone(sanitizedPhone)) {
            throw new BadRequestException("Telefone já cadastrado");
        }

        UserEntity user = saveUser(sanitizedEmail, dto.password(), UserRole.CANDIDATE);

        CandidateEntity candidate = CandidateEntity.builder()
                .user(user)
                .name(dto.name().trim())
                .cpf(sanitizedCpf)
                .phone(sanitizedPhone)
                .build();

        candidateRepository.save(candidate);
    }

    @Transactional
    public void registerCompany(RegisterCompanyRequestDto dto) throws BadRequestException {
        String sanitizedEmail = SanitizationUtil.email(dto.email());
        String sanitizedCnpj = SanitizationUtil.removeNonDigits(dto.cnpj());

        validateEmailUniqueness(sanitizedEmail);

        if (companyRepository.existsByCnpj(sanitizedCnpj)) {
            throw new BadRequestException("CNPJ já cadastrado");
        }

        UserEntity user = saveUser(sanitizedEmail, dto.password(), UserRole.COMPANY);

        CompanyEntity company = CompanyEntity.builder()
                .user(user)
                .cnpj(sanitizedCnpj)
                .tradeName(dto.tradeName().trim())
                .corporateName(dto.corporateName().trim())
                .description(SanitizationUtil.optionalText(dto.description()))
                .websiteUrl(SanitizationUtil.optionalText(dto.websiteUrl()))
                .build();

        companyRepository.save(company);
    }

    private void validateEmailUniqueness(String email) throws BadRequestException {
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email já cadastrado");
        }
    }

    private UserEntity saveUser(String email, String rawPassword, UserRole role) {
        UserEntity user = UserEntity.builder()
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .role(role)
                .build();

        return userRepository.save(user);
    }


}
