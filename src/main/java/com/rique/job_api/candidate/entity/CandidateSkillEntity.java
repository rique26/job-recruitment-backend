package com.rique.job_api.candidate.entity;

import com.rique.job_api.skill.entity.SkillEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "candidate_skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateSkillEntity {

    @EmbeddedId
    private CandidateSkillId id = new CandidateSkillId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("candidateId")
    @JoinColumn(name = "candidate_id")
    private CandidateEntity candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("skillId")
    @JoinColumn(name = "skill_id")
    private SkillEntity skill;

    @Column(nullable = false, length = 20)
    private String level;
}