package com.rique.job_api.candidate.service;

import com.rique.job_api.candidate.dto.request.AddSkillRequestDto;
import com.rique.job_api.candidate.dto.request.CreateExperienceRequestDto;
import com.rique.job_api.candidate.dto.request.UpdateCandidateRequestDto;
import com.rique.job_api.candidate.dto.response.CandidateProfileResponseDto;
import com.rique.job_api.candidate.entity.CandidateEntity;
import com.rique.job_api.candidate.entity.CandidateSkillEntity;
import com.rique.job_api.candidate.mapper.CandidateMapper;
import com.rique.job_api.candidate.repository.CandidateRepository;
import com.rique.job_api.candidate.repository.CandidateSkillRepository;
import com.rique.job_api.candidate.repository.ExperienceRepository;
import com.rique.job_api.exception.BadRequestException;
import com.rique.job_api.exception.NotFoundException;
import com.rique.job_api.skill.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final CandidateMapper candidateMapper;
    private final ExperienceRepository experienceRepository;
    private final CandidateSkillRepository candidateSkillRepository;
    private final SkillRepository skillRepository;

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

    @Transactional
    public CandidateProfileResponseDto addExperience(Long userId, CreateExperienceRequestDto dto) {
        var candidate = findByUserIdOrThrow(userId);

        var experience = candidateMapper.toExperienceEntity(dto);
        experience.setCandidate(candidate);

        candidate.getExperiences().add(experience);
        candidateRepository.save(candidate);

        return candidateMapper.toDto(candidate);
    }

    public void deleteExperience(Long userId, Long experienceId) {
        var candidate = findByUserIdOrThrow(userId);

        var experience = experienceRepository.findByIdAndCandidateId(experienceId, candidate.getId())
                .orElseThrow(() -> new NotFoundException("Experiência não encontrada ou não pertence a este candidato"));

        candidate.getExperiences().remove(experience);
        experienceRepository.delete(experience);
    }

    @Transactional
    public CandidateProfileResponseDto addSkill(Long userId, AddSkillRequestDto dto) {
        var candidate = findByUserIdOrThrow(userId);
        var skill = skillRepository.findById(dto.skillId())
                .orElseThrow(() -> new NotFoundException("Habilidade não encontrada"));

        if (candidateSkillRepository.existsByCandidateIdAndSkillId(candidate.getId(), skill.getId())) {
            throw new BadRequestException("Candidato já possui esta habilidade.");
        }

        var candidateSkill = CandidateSkillEntity.builder()
                .candidate(candidate)
                .skill(skill)
                .level(dto.level())
                .build();

        candidate.getCandidateSkills().add(candidateSkill);
        candidateRepository.save(candidate);

        return candidateMapper.toDto(candidate);
    }

    @Transactional
    public void removeSkill(Long userId, Long skillId) {
        var candidate = findByUserIdOrThrow(userId);

        // Valida se a skill está vinculada
        if (!candidateSkillRepository.existsByCandidateIdAndSkillId(candidate.getId(), skillId)) {
            throw new NotFoundException("Habilidade não vinculada ao perfil deste candidato");
        }

        candidate.getCandidateSkills()
                .removeIf(candidateSkill -> candidateSkill.getSkill().getId().equals(skillId));

        candidateSkillRepository.deleteByCandidateIdAndSkillId(candidate.getId(), skillId);
    }
}
