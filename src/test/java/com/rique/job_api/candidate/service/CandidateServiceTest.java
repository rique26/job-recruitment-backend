package com.rique.job_api.candidate.service;

import com.rique.job_api.auth.entity.UserEntity;
import com.rique.job_api.auth.enums.UserRole;
import com.rique.job_api.candidate.dto.request.UpdateCandidateRequestDto;
import com.rique.job_api.candidate.dto.response.CandidateProfileResponseDto;
import com.rique.job_api.candidate.dto.response.CandidateSkillResponseDto;
import com.rique.job_api.candidate.dto.response.ExperienceResponseDto;
import com.rique.job_api.candidate.entity.CandidateEntity;
import com.rique.job_api.candidate.entity.CandidateSkillEntity;
import com.rique.job_api.candidate.entity.ExperienceEntity;
import com.rique.job_api.candidate.mapper.CandidateMapper;
import com.rique.job_api.candidate.repository.CandidateRepository;
import com.rique.job_api.exception.BadRequestException;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CandidateServiceTest {

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private CandidateMapper candidateMapper;

    @InjectMocks
    private CandidateService candidateService;

    @Nested
    class GetMyProfile {

        @Test
        @DisplayName("Should return candidate profile with skills and experiences when user id exists")
        void shouldGetMyProfileWithSuccess() {
            // Arrange
            Long userId = 1L;

            var candidate = CandidateEntity.builder()
                    .id(1L)
                    .build();

            var skillDto = CandidateSkillResponseDto.builder()
                    .skillId(10L)
                    .name("Java")
                    .level("ADVANCED")
                    .build();

            var expDto = ExperienceResponseDto.builder()
                    .id(100L)
                    .company("Tech Corp")
                    .jobTitle("Backend Developer")
                    .description("Spring Boot development")
                    .startDate(LocalDate.of(2023, 1, 10))
                    .endDate(null)
                    .build();

            var expectedResponse = CandidateProfileResponseDto.builder()
                    .id(1L)
                    .name("Pedro Henrique")
                    .cpf("12345678901")
                    .phone("85999999999")
                    .professionalSummary("Desenvolvedor mobile e backend")
                    .linkedinUrl("https://linkedin.com/in/pedro")
                    .skills(List.of(skillDto))
                    .experiences(List.of(expDto))
                    .build();

            when(candidateRepository.findByUserId(userId)).thenReturn(Optional.of(candidate));
            when(candidateMapper.toDto(candidate)).thenReturn(expectedResponse);

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
            var returnedSkillDto = response.skills().get(0);
            assertEquals(10L, returnedSkillDto.skillId());
            assertEquals("Java", returnedSkillDto.name());
            assertEquals("ADVANCED", returnedSkillDto.level());

            // Assert Experiences
            assertNotNull(response.experiences());
            assertEquals(1, response.experiences().size());
            var returnedExpDto = response.experiences().get(0);
            assertEquals(100L, returnedExpDto.id());
            assertEquals("Tech Corp", returnedExpDto.company());
            assertEquals("Backend Developer", returnedExpDto.jobTitle());
            assertEquals("Spring Boot development", returnedExpDto.description());
            assertEquals(LocalDate.of(2023, 1, 10), returnedExpDto.startDate());
            assertNull(returnedExpDto.endDate());

            verify(candidateRepository, times(1)).findByUserId(userId);
            verify(candidateMapper, times(1)).toDto(candidate);
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

    @Nested
    class UpdateMyProfile {

        @Test
        @DisplayName("Should update candidate profile successfully with all fields when data is valid")
        void shouldUpdateMyProfileWithSuccess() {
            // Arrange
            Long userId = 1L;
            var candidate = CandidateEntity.builder()
                    .id(1L)
                    .name("Pedro Antigo")
                    .phone("85888888888")
                    .professionalSummary("Resumo antigo")
                    .linkedinUrl("https://linkedin.com/in/antigo")
                    .build();

            var updateDto = UpdateCandidateRequestDto.builder()
                    .name("Pedro Novo")
                    .phone("85999999999")
                    .professionalSummary("Resumo atualizado")
                    .linkedinUrl("https://linkedin.com/in/novo")
                    .build();

            var expectedResponse = CandidateProfileResponseDto.builder()
                    .id(1L)
                    .name("Pedro Novo")
                    .phone("85999999999")
                    .professionalSummary("Resumo atualizado")
                    .linkedinUrl("https://linkedin.com/in/novo")
                    .build();

            when(candidateRepository.findByUserId(userId)).thenReturn(Optional.of(candidate));
            when(candidateRepository.findByPhone("85999999999")).thenReturn(Optional.empty());
            when(candidateMapper.toDto(candidate)).thenReturn(expectedResponse);

            // Act
            var response = candidateService.updateMyProfile(userId, updateDto);

            // Assert
            assertNotNull(response);
            assertEquals("Pedro Novo", response.name());
            assertEquals("85999999999", response.phone());

            verify(candidateRepository, times(1)).findByUserId(userId);
            verify(candidateRepository, times(1)).findByPhone("85999999999");
            verify(candidateMapper, times(1)).updateEntityFromDto(updateDto, candidate);
            verify(candidateMapper, times(1)).toDto(candidate);
        }

        @Test
        @DisplayName("Should throw BadRequestException when phone is already in use by another user")
        void shouldThrowExceptionWhenPhoneAlreadyInUse() {
            // Arrange
            Long userId = 1L;
            var candidate = CandidateEntity.builder()
                    .id(1L)
                    .name("Pedro")
                    .phone("85888888888")
                    .build();

            var otherCandidateWithSamePhone = CandidateEntity.builder()
                    .id(2L)
                    .phone("85999999999")
                    .build();

            var updateDto = UpdateCandidateRequestDto.builder()
                    .name("Pedro")
                    .phone("85999999999")
                    .professionalSummary("Resumo")
                    .linkedinUrl("https://linkedin.com/in/pedro")
                    .build();

            when(candidateRepository.findByUserId(userId)).thenReturn(Optional.of(candidate));
            when(candidateRepository.findByPhone("85999999999")).thenReturn(Optional.of(otherCandidateWithSamePhone));

            // Act & Assert
            var exception = assertThrows(
                    BadRequestException.class,
                    () -> candidateService.updateMyProfile(userId, updateDto)
            );

            assertEquals("Este telefone já está cadastrado por outro usuário.", exception.getMessage());
            verify(candidateRepository, times(1)).findByUserId(userId);
            verify(candidateRepository, times(1)).findByPhone("85999999999");
            verify(candidateMapper, never()).updateEntityFromDto(any(), any());
            verify(candidateMapper, never()).toDto(any());
        }

        @Test
        @DisplayName("Should throw NotFoundException when updating non-existent candidate")
        void shouldThrowExceptionWhenUpdatingNonExistentCandidate() {
            // Arrange
            Long userId = 99L;
            var updateDto = UpdateCandidateRequestDto.builder()
                    .name("Pedro")
                    .phone("85999999999")
                    .professionalSummary("Resumo")
                    .linkedinUrl("https://linkedin.com/in/pedro")
                    .build();

            when(candidateRepository.findByUserId(userId)).thenReturn(Optional.empty());

            // Act & Assert
            var exception = assertThrows(
                    NotFoundException.class,
                    () -> candidateService.updateMyProfile(userId, updateDto)
            );

            assertEquals("Candidato não encontrado", exception.getMessage());
            verify(candidateRepository, times(1)).findByUserId(userId);
            verifyNoInteractions(candidateMapper);
        }

        @Test
        @DisplayName("Should update candidate profile without checking phone uniqueness when phone is unchanged")
        void shouldUpdateMyProfileWithoutPhoneCheckWhenPhoneUnchanged() {
            // Arrange
            Long userId = 1L;
            String samePhone = "85999999999";

            var candidate = CandidateEntity.builder()
                    .id(1L)
                    .phone(samePhone)
                    .build();

            var updateDto = UpdateCandidateRequestDto.builder()
                    .name("Pedro Novo")
                    .phone(samePhone) // Telefone igual ao atual
                    .build();

            var expectedResponse = CandidateProfileResponseDto.builder()
                    .id(1L)
                    .name("Pedro Novo")
                    .phone(samePhone)
                    .build();

            when(candidateRepository.findByUserId(userId)).thenReturn(Optional.of(candidate));
            when(candidateMapper.toDto(candidate)).thenReturn(expectedResponse);

            // Act
            var response = candidateService.updateMyProfile(userId, updateDto);

            // Assert
            assertNotNull(response);
            verify(candidateRepository, times(1)).findByUserId(userId);
            // Garante que nem foi ao banco buscar por telefone!
            verify(candidateRepository, never()).findByPhone(anyString());
            verify(candidateMapper, times(1)).updateEntityFromDto(updateDto, candidate);
            verify(candidateMapper, times(1)).toDto(candidate);
        }
    }
}
