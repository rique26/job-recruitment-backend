package com.rique.job_api.company.entity;

import com.rique.job_api.auth.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @Column(nullable = false, unique = true, length = 14)
    private String cnpj;

    @Column(name = "trade_name", nullable = false, length = 100)
    private String tradeName;

    @Column(name = "corporate_name", nullable = false, length = 150)
    private String corporateName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "website_url", length = 255)
    private String websiteUrl;

    @Column(length = 50)
    private String size;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}