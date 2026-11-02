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
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * JPA Entity representing a fitness challenge in FitSquad Hub.
 * Includes Jakarta Persistence (JPA) mappings, a @ManyToOne relationship to Coach,
 * and Jakarta Bean Validation constraints for form input validation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CHALLENGES")
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    @NotBlank(message = "Challenge title is required")
    @Size(min = 3, max = 60, message = "Title must be between 3 and 60 characters")
    @Column(nullable = false, length = 60)
    private String title;

    @NotBlank(message = "Please select a category")
    @Column(nullable = false, length = 50)
    private String category;

    @NotBlank(message = "Description cannot be empty")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    @Column(nullable = false, length = 500)
    private String description;

    @NotBlank(message = "Target goal is required")
    @Size(min = 5, max = 100, message = "Target goal must be between 5 and 100 characters")
    @Column(name = "target_goal", nullable = false, length = 100)
    private String targetGoal;

    @Min(value = 1, message = "Duration must be at least 1 day")
    @Max(value = 365, message = "Duration cannot exceed 365 days")
    @Column(name = "duration_days")
    private int durationDays;

    @Min(value = 50, message = "Reward points must be at least 50 XP")
    @Max(value = 5000, message = "Reward points cannot exceed 5,000 XP")
    @Column(name = "reward_points")
    private int rewardPoints;

    @NotBlank(message = "Please select a difficulty level")
    @Column(nullable = false, length = 30)
    private String difficulty;

    @Column(name = "image_url", length = 300)
    private String imageUrl;

    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    private Coach coach;

    /**
     * Convenience constructor supporting instantiation without explicitly supplying createdAt or coach.
     */
    public Challenge(Long id, String title, String category, String description, String targetGoal,
                     int durationDays, int rewardPoints, String difficulty, String imageUrl, boolean active) {
        this.id = id;
        this.createdAt = new Date();
        this.title = title;
        this.category = category;
        this.description = description;
        this.targetGoal = targetGoal;
        this.durationDays = durationDays;
        this.rewardPoints = rewardPoints;
        this.difficulty = difficulty;
        this.imageUrl = imageUrl;
        this.active = active;
    }
}
