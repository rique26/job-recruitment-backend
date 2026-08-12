package com.rique.job_api.auth.service;

import com.rique.job_api.auth.dto.request.LoginRequestDto;
import com.rique.job_api.auth.dto.request.RegisterCandidateRequestDto;
import com.rique.job_api.auth.dto.request.RegisterCompanyRequestDto;
import com.rique.job_api.auth.dto.response.TokenResponseDto;
import com.rique.job_api.auth.entity.UserEntity;
import com.rique.job_api.auth.enums.UserRole;
import com.rique.job_api.auth.repository.UserRepository;
import com.rique.job_api.candidate.entity.CandidateEntity;
import com.rique.job_api.candidate.repository.CandidateRepository;
import com.rique.job_api.company.entity.CompanyEntity;
import com.rique.job_api.company.repository.CompanyRepository;
import com.rique.job_api.config.TokenProvider;
import com.rique.job_api.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    @Captor
    private ArgumentCaptor<UserEntity> userEntityArgumentCaptor;

    @Captor
    private ArgumentCaptor<CandidateEntity> candidateEntityArgumentCaptor;

    @Captor
    private ArgumentCaptor<CompanyEntity> companyEntityArgumentCaptor;

    @Nested
    class Login {

        @Test
        @DisplayName("Should authenticate and return token when valid credentials are provided")
        void shouldLoginWithSuccess() {
            // Arrange
            var input = new LoginRequestDto("usuario@email.com", "123456");

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(tokenProvider.generateToken(authentication)).thenReturn("fake-jwt-token");

            // Act
            TokenResponseDto response = authService.login(input);

            // Assert
            assertNotNull(response);
            assertEquals("fake-jwt-token", response.token());
            verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(tokenProvider, times(1)).generateToken(authentication);
        }

        @Test
        @DisplayName("Should throw BadRequestException when invalid credentials are provided")
        void shouldThrowExceptionWhenBadCredentials() {
            // Arrange
            var input = new LoginRequestDto("usuario@email.com", "senhaerrada");

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Bad credentials"));

            // Act & Assert
            var exception = assertThrows(
                    BadRequestException.class,
                    () -> authService.login(input)
            );

            assertEquals("E-mail ou senha inválidos", exception.getMessage());
            verify(tokenProvider, never()).generateToken(any());
        }
    }

    @Nested
    class RegisterCandidate {

        @Test
        @DisplayName("Should register candidate with success when valid request is provided")
        void shouldRegisterCandidateWithSuccess() {
            // Arrange
            var input = new RegisterCandidateRequestDto(
                    "candidato@email.com",
                    "123456",
                    "Candidato Teste",
                    "123.456.789-00",
                    "(85) 98888-8888"
            );

            when(userRepository.existsByEmail("candidato@email.com")).thenReturn(false);
            when(candidateRepository.existsByCpf("12345678900")).thenReturn(false);
            when(candidateRepository.existsByPhone("85988888888")).thenReturn(false);
            when(passwordEncoder.encode("123456")).thenReturn("encoded_password");

            when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
                UserEntity user = invocation.getArgument(0);
                return UserEntity.builder()
                        .id(1L)
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .role(user.getRole())
                        .build();
            });

            // Act & Assert
            assertDoesNotThrow(() -> authService.registerCandidate(input));

            // Verify User save
            verify(userRepository, times(1)).save(userEntityArgumentCaptor.capture());
            var capturedUser = userEntityArgumentCaptor.getValue();
            assertEquals("candidato@email.com", capturedUser.getEmail());
            assertEquals("encoded_password", capturedUser.getPassword());
            assertEquals(UserRole.CANDIDATE, capturedUser.getRole());

            // Verify Candidate save (com CPF e Telefone sanitizados)
            verify(candidateRepository, times(1)).save(candidateEntityArgumentCaptor.capture());
            var capturedCandidate = candidateEntityArgumentCaptor.getValue();
            assertEquals("Candidato Teste", capturedCandidate.getName());
            assertEquals("12345678900", capturedCandidate.getCpf());
            assertEquals("85988888888", capturedCandidate.getPhone());
        }

        @Test
        @DisplayName("Should throw BadRequestException when email already exists for candidate")
        void shouldThrowExceptionWhenEmailExists() {
            var input = new RegisterCandidateRequestDto(
                    "existente@email.com",
                    "123456",
                    "Candidato",
                    "123.456.789-00",
                    "85988888888"
            );

            when(userRepository.existsByEmail(anyString())).thenReturn(true);

            var exception = assertThrows(BadRequestException.class, () -> authService.registerCandidate(input));
            assertEquals("Email já cadastrado", exception.getMessage());
            verify(candidateRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw BadRequestException when CPF already exists")
        void shouldThrowExceptionWhenCpfExists() {
            var input = new RegisterCandidateRequestDto(
                    "novo@email.com",
                    "123456",
                    "Candidato",
                    "123.456.789-00",
                    "85988888888"
            );

            when(userRepository.existsByEmail("novo@email.com")).thenReturn(false);
            when(candidateRepository.existsByCpf("12345678900")).thenReturn(true);

            var exception = assertThrows(BadRequestException.class, () -> authService.registerCandidate(input));
            assertEquals("CPF já cadastrado", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw BadRequestException when Phone already exists")
        void shouldThrowExceptionWhenPhoneExists() {
            var input = new RegisterCandidateRequestDto(
                    "novo@email.com",
                    "123456",
                    "Candidato",
                    "123.456.789-00",
                    "85988888888"
            );

            when(userRepository.existsByEmail("novo@email.com")).thenReturn(false);
            when(candidateRepository.existsByCpf("12345678900")).thenReturn(false);
            when(candidateRepository.existsByPhone("85988888888")).thenReturn(true);

            var exception = assertThrows(BadRequestException.class, () -> authService.registerCandidate(input));
            assertEquals("Telefone já cadastrado", exception.getMessage());
        }
    }

    @Nested
    class RegisterCompany {

        @Test
        @DisplayName("Should register company with success when valid request is provided")
        void shouldRegisterCompanyWithSuccess() {
            // Arrange
            var input = new RegisterCompanyRequestDto(
                    "empresa@email.com",
                    "123456",
                    "12.345.678/0001-90",
                    "Tech Ltda",
                    "Tech Corporation S.A.",
                    "Empresa de tecnologia",
                    "https://tech.com"
            );

            when(userRepository.existsByEmail("empresa@email.com")).thenReturn(false);
            when(companyRepository.existsByCnpj("12345678000190")).thenReturn(false);
            when(passwordEncoder.encode("123456")).thenReturn("encoded_password");

            // Simula o salvamento do usuário retornando a entidade com ID
            when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
                UserEntity user = invocation.getArgument(0);
                return UserEntity.builder()
                        .id(1L)
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .role(user.getRole())
                        .build();
            });

            // Act & Assert
            assertDoesNotThrow(() -> authService.registerCompany(input));

            // Verify User save
            verify(userRepository, times(1)).save(userEntityArgumentCaptor.capture());
            var capturedUser = userEntityArgumentCaptor.getValue();
            assertEquals("empresa@email.com", capturedUser.getEmail());
            assertEquals("encoded_password", capturedUser.getPassword());
            assertEquals(UserRole.COMPANY, capturedUser.getRole());

            // Verify Company save (com CNPJ sanitizado)
            verify(companyRepository, times(1)).save(companyEntityArgumentCaptor.capture());
            var capturedCompany = companyEntityArgumentCaptor.getValue();
            assertEquals("12345678000190", capturedCompany.getCnpj());
            assertEquals("Tech Ltda", capturedCompany.getTradeName());
            assertEquals("Tech Corporation S.A.", capturedCompany.getCorporateName());
        }

        @Test
        @DisplayName("Should throw BadRequestException when email already exists")
        void shouldThrowExceptionWhenEmailAlreadyExists() {
            // Arrange
            var input = new RegisterCompanyRequestDto(
                    "existente@email.com",
                    "123456",
                    "12.345.678/0001-90",
                    "Tech Ltda",
                    "Tech Corporation S.A.",
                    null,
                    null
            );

            when(userRepository.existsByEmail("existente@email.com")).thenReturn(true);

            // Act & Assert
            var exception = assertThrows(
                    BadRequestException.class,
                    () -> authService.registerCompany(input)
            );

            assertEquals("Email já cadastrado", exception.getMessage());

            verify(userRepository, times(1)).existsByEmail("existente@email.com");
            verify(companyRepository, never()).existsByCnpj(any());
            verify(userRepository, never()).save(any());
            verify(companyRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw BadRequestException when CNPJ already exists")
        void shouldThrowExceptionWhenCnpjAlreadyExists() {
            // Arrange
            var input = new RegisterCompanyRequestDto(
                    "novo@email.com",
                    "123456",
                    "12.345.678/0001-90",
                    "Tech Ltda",
                    "Tech Corporation S.A.",
                    null,
                    null
            );

            when(userRepository.existsByEmail("novo@email.com")).thenReturn(false);
            when(companyRepository.existsByCnpj("12345678000190")).thenReturn(true);

            // Act & Assert
            var exception = assertThrows(
                    BadRequestException.class,
                    () -> authService.registerCompany(input)
            );

            assertEquals("CNPJ já cadastrado", exception.getMessage());

            verify(userRepository, times(1)).existsByEmail("novo@email.com");
            verify(companyRepository, times(1)).existsByCnpj("12345678000190");
            verify(userRepository, never()).save(any());
            verify(companyRepository, never()).save(any());
        }
    }
}