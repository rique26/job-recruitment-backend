package com.rique.job_api.candidate.service;

import com.rique.job_api.candidate.dto.response.CandidateProfileResponseDto;
import com.rique.job_api.candidate.dto.response.CandidateSkillResponseDto;
import com.rique.job_api.candidate.dto.response.ExperienceResponseDto;
import com.rique.job_api.candidate.repository.CandidateRepository;
import com.rique.job_api.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;

    public CandidateProfileResponseDto getMyProfile (Long userId) {
        var candidate = candidateRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Candidato não encontrado"));

        var skills = candidate.getCandidateSkills().stream()
                .map(cs -> CandidateSkillResponseDto.builder()
                        .skillId(cs.getSkill().getId())
                        .name(cs.getSkill().getName())
                        .level(cs.getLevel())
                        .build())
                .toList();

        var experiences = candidate.getExperiences().stream()
                .map(exp -> ExperienceResponseDto.builder()
                        .id(exp.getId())
                        .company(exp.getCompany())
                        .jobTitle(exp.getJobTitle())
                        .description(exp.getDescription())
                        .startDate(exp.getStartDate())
                        .endDate(exp.getEndDate())
                        .build())
                .toList();

        return CandidateProfileResponseDto.builder()
                .id(candidate.getId())
                .name(candidate.getName())
                .cpf(candidate.getCpf())
                .phone(candidate.getPhone())
                .professionalSummary(candidate.getProfessionalSummary())
                .linkedinUrl(candidate.getLinkedinUrl())
                .skills(skills)
                .experiences(experiences)
                .build();
    }
}
