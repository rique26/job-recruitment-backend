package com.rique.job_api.candidate.mapper;

import com.rique.job_api.candidate.dto.request.UpdateCandidateRequestDto;
import com.rique.job_api.candidate.dto.response.CandidateProfileResponseDto;
import com.rique.job_api.candidate.dto.response.CandidateSkillResponseDto;
import com.rique.job_api.candidate.dto.response.ExperienceResponseDto;
import com.rique.job_api.candidate.entity.CandidateEntity;
import com.rique.job_api.candidate.entity.CandidateSkillEntity;
import com.rique.job_api.candidate.entity.ExperienceEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CandidateMapper {

    // Converte a CandidateEntity para CandidateProfileResponseDto
    CandidateProfileResponseDto toDto(CandidateEntity entity);

    // Mapeamentos específicos para as listas aninhadas (Skills e Experiences)
    @Mapping(target = "skillId", source = "skill.id")
    @Mapping(target = "name", source = "skill.name")
    CandidateSkillResponseDto toSkillDto(CandidateSkillEntity entity);

    ExperienceResponseDto toExperienceDto(ExperienceEntity entity);

    // Atualiza a entidade existente com os dados do UpdateCandidateRequestDto
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cpf", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(UpdateCandidateRequestDto dto, @MappingTarget CandidateEntity candidate);

}
