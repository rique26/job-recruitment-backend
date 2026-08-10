package com.rique.job_api.candidate.repository;

import com.rique.job_api.candidate.entity.CandidateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<CandidateEntity, Long> {

    boolean existsByCpf(String cpf);
    boolean existsByPhone(String phone);

    Optional<CandidateEntity> findByUserId(Long userId);

    Optional<CandidateEntity> findByCpf(String cpf);

    Optional<CandidateEntity> findByPhone(String phone);
}