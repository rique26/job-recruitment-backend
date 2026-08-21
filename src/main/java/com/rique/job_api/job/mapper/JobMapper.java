package com.rique.job_api.job.mapper;

import com.rique.job_api.job.dto.request.CreateJobRequestDto;
import com.rique.job_api.job.dto.response.JobResponseDto;
import com.rique.job_api.job.entity.JobEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JobMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    JobEntity toEntity(CreateJobRequestDto dto);

    JobResponseDto toDto(JobEntity entity);
}