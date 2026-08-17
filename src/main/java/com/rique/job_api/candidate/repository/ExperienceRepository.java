package com.rique.job_api.candidate.repository;

import com.rique.job_api.candidate.entity.ExperienceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExperienceRepository extends JpaRepository<ExperienceEntity, Long> {

    Optional<ExperienceEntity> findByIdAndCandidateId(Long id, Long candidateId);
}
