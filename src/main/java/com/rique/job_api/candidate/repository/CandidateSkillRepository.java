package com.rique.job_api.candidate.repository;

import com.rique.job_api.candidate.entity.CandidateSkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateSkillRepository extends JpaRepository<CandidateSkillEntity, Long> {
    void deleteByCandidateIdAndSkillId(Long candidateId, Long skillId);
    boolean existsByCandidateIdAndSkillId(Long candidateId, Long skillId);
}