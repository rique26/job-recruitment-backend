package com.rique.job_api.auth.service;

import com.rique.job_api.auth.dto.request.RegisterCandidateRequestDto;
import com.rique.job_api.auth.entity.UserEntity;
import com.rique.job_api.auth.enums.UserRole;
import com.rique.job_api.candidate.entity.CandidateEntity;
import com.rique.job_api.candidate.repository.CandidateRepository;
import com.rique.job_api.auth.repository.UserRepository;
import com.rique.job_api.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;

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
}
