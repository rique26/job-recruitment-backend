package com.rique.job_api.company.repository;

import com.rique.job_api.company.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

    boolean existsByCnpj(String cnpj);

    Optional<CompanyEntity> findByUserId(Long userId);

    Optional<CompanyEntity> findByCnpj(String cnpj);
}