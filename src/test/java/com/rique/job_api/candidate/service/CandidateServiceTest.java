package com.rique.job_api.candidate.service;

import com.rique.job_api.auth.entity.UserEntity;
import com.rique.job_api.auth.enums.UserRole;
import com.rique.job_api.candidate.entity.CandidateEntity;
import com.rique.job_api.candidate.entity.CandidateSkillEntity;
import com.rique.job_api.candidate.entity.ExperienceEntity;
import com.rique.job_api.candidate.repository.CandidateRepository;
import com.rique.job_api.exception.NotFoundException;
import com.rique.job_api.skill.entity.SkillEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CandidateServiceTest {

    @Mock
    private CandidateRepository candidateRepository;

    @InjectMocks
    private CandidateService candidateService;

    @Nested
    class GetMyProfile {

        @Test
        @DisplayName("Should return candidate profile with skills and experiences when user id exists")
        void shouldGetMyProfileWithSuccess() {
            // Arrange
            Long userId = 1L;

            var user = UserEntity.builder()
                    .id(userId)
                    .email("candidato@email.com")
                    .role(UserRole.CANDIDATE)
                    .build();

            var skill = SkillEntity.builder()
                    .id(10L)
                    .name("Java")
                    .build();

            var candidateSkill = CandidateSkillEntity.builder()
                    .skill(skill)
                    .level("ADVANCED")
                    .build();

            var experience = ExperienceEntity.builder()
                    .id(100L)
                    .company("Tech Corp")
                    .jobTitle("Backend Developer")
                    .description("Spring Boot development")
                    .startDate(LocalDate.of(2023, 1, 10))
                    .endDate(null)
                    .build();

            var candidate = CandidateEntity.builder()
                    .id(1L)
                    .user(user)
                    .name("Pedro Henrique")
                    .cpf("12345678901")
                    .phone("85999999999")
                    .professionalSummary("Desenvolvedor mobile e backend")
                    .linkedinUrl("https://linkedin.com/in/pedro")
                    .candidateSkills(new HashSet<>(Set.of(candidateSkill)))
                    .experiences(new HashSet<>(Set.of(experience)))
                    .createdAt(LocalDateTime.now())
                    .build();

            when(candidateRepository.findByUserId(userId)).thenReturn(Optional.of(candidate));

            // Act
            var response = candidateService.getMyProfile(userId);

            // Assert
            assertNotNull(response);
            assertEquals(1L, response.id());
            assertEquals("Pedro Henrique", response.name());
            assertEquals("12345678901", response.cpf());
            assertEquals("85999999999", response.phone());
            assertEquals("Desenvolvedor mobile e backend", response.professionalSummary());
            assertEquals("https://linkedin.com/in/pedro", response.linkedinUrl());

            // Assert Skills
            assertNotNull(response.skills());
            assertEquals(1, response.skills().size());
            var skillDto = response.skills().get(0);
            assertEquals(10L, skillDto.skillId());
            assertEquals("Java", skillDto.name());
            assertEquals("ADVANCED", skillDto.level());

            // Assert Experiences
            assertNotNull(response.experiences());
            assertEquals(1, response.experiences().size());
            var expDto = response.experiences().get(0);
            assertEquals(100L, expDto.id());
            assertEquals("Tech Corp", expDto.company());
            assertEquals("Backend Developer", expDto.jobTitle());
            assertEquals("Spring Boot development", expDto.description());
            assertEquals(LocalDate.of(2023, 1, 10), expDto.startDate());
            assertNull(expDto.endDate());

            verify(candidateRepository, times(1)).findByUserId(userId);
        }

        @Test
        @DisplayName("Should throw NotFoundException when candidate is not found for user id")
        void shouldThrowExceptionWhenCandidateNotFound() {
            // Arrange
            Long userId = 99L;

            when(candidateRepository.findByUserId(userId)).thenReturn(Optional.empty());

            // Act & Assert
            var exception = assertThrows(
                    NotFoundException.class,
                    () -> candidateService.getMyProfile(userId)
            );

            assertEquals("Candidato não encontrado", exception.getMessage());
            verify(candidateRepository, times(1)).findByUserId(userId);
        }
    }
}
