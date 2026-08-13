package com.rique.job_api.candidate.service;

import com.rique.job_api.candidate.dto.request.UpdateCandidateRequestDto;
import com.rique.job_api.candidate.dto.response.CandidateProfileResponseDto;
import com.rique.job_api.candidate.entity.CandidateEntity;
import com.rique.job_api.candidate.mapper.CandidateMapper;
import com.rique.job_api.candidate.repository.CandidateRepository;
import com.rique.job_api.exception.BadRequestException;
import com.rique.job_api.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final CandidateMapper candidateMapper;

    @Transactional(readOnly = true)
    public CandidateProfileResponseDto getMyProfile(Long userId) {
        var candidate = findByUserIdOrThrow(userId);
        return candidateMapper.toDto(candidate);
    }

    @Transactional
    public CandidateProfileResponseDto updateMyProfile(Long userId, UpdateCandidateRequestDto dto) {
        var candidate = findByUserIdOrThrow(userId);

        if (dto.phone() != null && !dto.phone().equals(candidate.getPhone())) {
            candidateRepository.findByPhone(dto.phone())
                    .filter(existing -> !existing.getId().equals(candidate.getId()))
                    .ifPresent(existing -> {
                        throw new BadRequestException("Este telefone já está cadastrado por outro usuário.");
                    });
        }

        candidateMapper.updateEntityFromDto(dto, candidate); // O MapStruct aplica as alterações do DTO no objeto 'candidate'
        return candidateMapper.toDto(candidate);   // O Hibernate atualiza no BD ao fechar a transação.
    }

    private CandidateEntity findByUserIdOrThrow(Long userId) {
        return candidateRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Candidato não encontrado"));
    }
}
