package com.example.FitnessChallengeApp_Test.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JPA Entity representing a certified fitness coach/trainer in FitSquad Hub.
 * Coaches author and lead fitness challenges.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COACHES")
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Coach name is required")
    @Size(min = 2, max = 50, message = "Coach name must be between 2 and 50 characters")
    @Column(nullable = false, length = 50)
    private String name;

    @NotBlank(message = "Specialty is required")
    @Column(nullable = false, length = 60)
    private String specialty;

    @NotBlank(message = "Certification is required")
    @Size(min = 2, max = 80, message = "Certification must be between 2 and 80 characters")
    @Column(nullable = false, length = 80)
    private String certification;

    @Min(value = 1, message = "Experience must be at least 1 year")
    @Max(value = 50, message = "Experience cannot exceed 50 years")
    @Column(name = "experience_years")
    private int experienceYears;

    @Column(name = "avatar_url")
    private String avatarUrl;
}

