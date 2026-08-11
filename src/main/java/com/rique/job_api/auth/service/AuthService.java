package com.rique.job_api.auth.service;

import com.rique.job_api.auth.dto.request.RegisterCandidateRequestDto;
import com.rique.job_api.auth.dto.request.RegisterCompanyRequestDto;
import com.rique.job_api.auth.entity.UserEntity;
import com.rique.job_api.auth.enums.UserRole;
import com.rique.job_api.candidate.entity.CandidateEntity;
import com.rique.job_api.candidate.repository.CandidateRepository;
import com.rique.job_api.auth.repository.UserRepository;
import com.rique.job_api.company.entity.CompanyEntity;
import com.rique.job_api.company.repository.CompanyRepository;
import com.rique.job_api.exception.BadRequestException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public void registerCandidate(RegisterCandidateRequestDto dto) throws BadRequestException {
        String sanitizedEmail = dto.email().trim().toLowerCase();
        String sanitizedCpf = dto.cpf().replaceAll("\\D", "");
        String sanitizedPhone = dto.phone().replaceAll("\\D", "");

        if (userRepository.existsByEmail(sanitizedEmail)) {
            throw new BadRequestException("Email já cadastrado");
        }

        if (candidateRepository.existsByCpf(sanitizedCpf)) {
            throw new BadRequestException("CPF já cadastrado");
        }

        if (candidateRepository.existsByPhone(sanitizedPhone)) {
            throw new BadRequestException("Telefone já cadastrado");
        }

        UserEntity user = UserEntity.builder()
                .email(sanitizedEmail)
                .password(passwordEncoder.encode(dto.password()))
                .role(UserRole.CANDIDATE)
                .build();

        user = userRepository.save(user);

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
        String sanitizedEmail = dto.email().trim().toLowerCase();
        String sanitizedCnpj = dto.cnpj().replaceAll("\\D", "");

        if (userRepository.existsByEmail(sanitizedEmail)) {
            throw new BadRequestException("Email já cadastrado");
        }

        if (companyRepository.existsByCnpj(sanitizedCnpj)) {
            throw new BadRequestException("CNPJ já cadastrado");
        }

        UserEntity user = UserEntity.builder()
                .email(sanitizedEmail)
                .password(passwordEncoder.encode(dto.password()))
                .role(UserRole.COMPANY)
                .build();

        user = userRepository.save(user);

        CompanyEntity company = CompanyEntity.builder()
                .user(user)
                .cnpj(sanitizedCnpj)
                .tradeName(dto.tradeName().trim())
                .corporateName(dto.corporateName().trim())
                .description(dto.description() != null ? dto.description().trim() : null)
                .websiteUrl(dto.websiteUrl() != null ? dto.websiteUrl().trim() : null)
                .build();

        companyRepository.save(company);
    }
}
