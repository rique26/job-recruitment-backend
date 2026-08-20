package com.rique.job_api.company.service;

import com.rique.job_api.company.dto.request.UpdateCompanyRequestDto;
import com.rique.job_api.company.dto.response.CompanyProfileResponseDto;
import com.rique.job_api.company.entity.CompanyEntity;
import com.rique.job_api.company.mapper.CompanyMapper;
import com.rique.job_api.company.repository.CompanyRepository;
import com.rique.job_api.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Transactional(readOnly = true)
    public CompanyProfileResponseDto getMyProfile(Long userId) {
        var company = findByUserIdOrThrow(userId);
        return companyMapper.toDto(company);
    }

    @Transactional
    public CompanyProfileResponseDto updateMyProfile(Long userId, UpdateCompanyRequestDto dto) {
        var company = findByUserIdOrThrow(userId);

        companyMapper.updateEntityFromDto(dto, company);
        return companyMapper.toDto(company);
    }

    private CompanyEntity findByUserIdOrThrow(Long userId) {
        return companyRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Empresa não encontrada"));
    }
}