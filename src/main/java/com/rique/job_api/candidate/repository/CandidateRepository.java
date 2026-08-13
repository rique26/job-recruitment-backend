package com.rique.job_api.candidate.repository;

import com.rique.job_api.auth.entity.UserEntity;
import com.rique.job_api.candidate.entity.CandidateEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<CandidateEntity, Long> {

    boolean existsByCpf(String cpf);
    boolean existsByPhone(String phone);

    @Query("SELECT DISTINCT c FROM CandidateEntity c " +
            "LEFT JOIN FETCH c.experiences " +
            "LEFT JOIN FETCH c.candidateSkills cs " +
            "LEFT JOIN FETCH cs.skill " +
            "WHERE c.user.id = :userId")
    Optional<CandidateEntity> findByUserId(@Param("userId") Long userId);

    Optional<CandidateEntity> findByCpf(String cpf);

    Optional<CandidateEntity> findByPhone(String phone);

    Long user(UserEntity user);
}