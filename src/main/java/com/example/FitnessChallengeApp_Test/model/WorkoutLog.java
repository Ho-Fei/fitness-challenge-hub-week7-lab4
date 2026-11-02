package com.example.FitnessChallengeApp_Test.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * JPA Entity representing an athlete's logged workout session.
 * Links to a specific Challenge via a @ManyToOne relationship and enforces
 * Jakarta Bean Validation rules on form submissions.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "WORKOUT_LOGS")
public class WorkoutLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    @NotBlank(message = "Athlete name is required")
    @Size(min = 2, max = 40, message = "Athlete name must be between 2 and 40 characters")
    @Column(name = "athlete_name", nullable = false, length = 40)
    private String athleteName;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @Column(name = "challenge_title", length = 60)
    private String challengeTitle;

    @NotNull(message = "Workout duration is required")
    @Min(value = 5, message = "Workout duration must be at least 5 minutes")
    @Max(value = 360, message = "Workout duration cannot exceed 360 minutes")
    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @NotNull(message = "Calories burned is required")
    @Min(value = 10, message = "Calories burned must be at least 10 kcal")
    @Max(value = 4000, message = "Calories burned cannot exceed 4,000 kcal")
    @Column(name = "calories_burned", nullable = false)
    private Integer caloriesBurned;

    @NotBlank(message = "Please select an intensity level")
    @Column(name = "intensity_level", nullable = false, length = 30)
    private String intensityLevel;

    @NotBlank(message = "Workout date is required")
    @Column(name = "workout_date", nullable = false, length = 20)
    private String workoutDate;

    @Size(max = 250, message = "Workout notes cannot exceed 250 characters")
    @Column(length = 250)
    private String notes;
}

