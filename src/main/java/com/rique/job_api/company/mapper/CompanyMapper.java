package com.rique.job_api.company.mapper;

import com.rique.job_api.company.dto.request.UpdateCompanyRequestDto;
import com.rique.job_api.company.dto.response.CompanyProfileResponseDto;
import com.rique.job_api.company.entity.CompanyEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    @Mapping(target = "name", source = "tradeName")
    @Mapping(target = "socialReason", source = "corporateName")
    @Mapping(target = "website", source = "websiteUrl")
    CompanyProfileResponseDto toDto(CompanyEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "corporateName", source = "socialReason") // Mapeia socialReason do DTO para corporateName da Entity
    @Mapping(target = "websiteUrl", source = "website")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cnpj", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(UpdateCompanyRequestDto dto, @MappingTarget CompanyEntity company);
}