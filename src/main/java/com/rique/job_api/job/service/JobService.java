package com.rique.job_api.job.service;

import com.rique.job_api.company.entity.CompanyEntity;
import com.rique.job_api.company.repository.CompanyRepository;
import com.rique.job_api.job.dto.request.CreateJobRequestDto;
import com.rique.job_api.job.dto.response.JobResponseDto;
import com.rique.job_api.job.entity.JobEntity;
import com.rique.job_api.job.mapper.JobMapper;
import com.rique.job_api.job.repository.JobRepository;
import com.rique.job_api.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final JobMapper jobMapper;

    @Transactional
    public JobResponseDto createJob(Long userId, CreateJobRequestDto dto) {
        CompanyEntity company = companyRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Empresa não encontrada para o usuário logado"));

        JobEntity job = jobMapper.toEntity(dto);
        job.setCompany(company);

        JobEntity savedJob = jobRepository.save(job);
        return jobMapper.toDto(savedJob);
    }
}