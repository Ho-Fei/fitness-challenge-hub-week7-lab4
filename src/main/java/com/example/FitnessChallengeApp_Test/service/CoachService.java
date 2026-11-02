package com.example.FitnessChallengeApp_Test.service;

import com.example.FitnessChallengeApp_Test.model.Coach;
import com.example.FitnessChallengeApp_Test.repository.CoachRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service managing JPA persistence operations for FitSquad Coaches.
 */
@Service
public class CoachService {

    private final CoachRepository coachRepository;

    public CoachService(CoachRepository coachRepository) {
        this.coachRepository = coachRepository;
    }

    public List<Coach> getAllCoaches() {
        return coachRepository.findAll();
    }

    public Coach getCoachById(Long id) {
        if (id == null) {
            return null;
        }
        return coachRepository.findById(id).orElse(null);
    }

    public Coach addCoach(Coach coach) {
        if (coach.getAvatarUrl() == null || coach.getAvatarUrl().trim().isEmpty()) {
            coach.setAvatarUrl("https://images.unsplash.com/photo-1567013127542-490d757e51fc?auto=format&fit=crop&w=400&q=80");
        }
        return coachRepository.save(coach);
    }

    public List<String> getSpecialties() {
        return List.of(
                "Strength & Core",
                "Cardio & HIIT",
                "Hypertrophy & Power",
                "Flexibility & Recovery",
                "Cardio Endurance",
                "Habit & Wellness"
        );
    }
}

