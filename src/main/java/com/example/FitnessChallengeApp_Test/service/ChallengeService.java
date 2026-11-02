package com.example.FitnessChallengeApp_Test.service;

import com.example.FitnessChallengeApp_Test.model.Challenge;
import com.example.FitnessChallengeApp_Test.repository.ChallengeRepository;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Service managing business operations and Spring Data JPA persistence for FitSquad Challenges.
 * Delegates all CRUD, Derived Query, JPQL @Query, Pagination, and Sorting operations
 * to ChallengeRepository.
 */
@Service
@Getter
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

    public ChallengeService(ChallengeRepository challengeRepository) {
        this.challengeRepository = challengeRepository;
    }

    // =========================================================================
    // Standard CRUD Operations (Inherited from JpaRepository)
    // =========================================================================

    public List<Challenge> getAllChallenges() {
        return challengeRepository.findAll();
    }

    public Challenge getChallengeById(Long id) {
        if (id == null) {
            return null;
        }
        return challengeRepository.findById(id).orElse(null);
    }

    public Challenge addChallenge(Challenge challenge) {
        if (challenge.getCreatedAt() == null) {
            challenge.setCreatedAt(new Date());
        }
        if (challenge.getImageUrl() == null || challenge.getImageUrl().trim().isEmpty()) {
            challenge.setImageUrl("https://images.unsplash.com/photo-1517838277536-f5f99be501cd?auto=format&fit=crop&w=800&q=80");
        }
        return challengeRepository.save(challenge);
    }

    public void deleteChallengeById(Long id) {
        if (id != null) {
            challengeRepository.deleteById(id);
        }
    }

    // =========================================================================
    // Paginated & Custom Query Methods (Derived Queries & @Query JPQL)
    // =========================================================================

    public Page<Challenge> getChallenges(Pageable pageable) {
        return challengeRepository.findAll(pageable);
    }

    public List<Challenge> getChallengesByCategory(String category) {
        return challengeRepository.findByCategory(category);
    }

    public List<Challenge> getActiveChallengesByRewardDesc() {
        return challengeRepository.findByActiveTrueOrderByRewardPointsDesc();
    }

    public Page<Challenge> searchByTitleIgnoreCase(String title, Pageable pageable) {
        return challengeRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    public Page<Challenge> searchByMinRewardPoints(int minRewardPoints, Pageable pageable) {
        return challengeRepository.findByRewardPointsGreaterThanEqual(minRewardPoints, pageable);
    }

    public Page<Challenge> searchByDifficulty(String difficulty, Pageable pageable) {
        return challengeRepository.findByDifficultyIgnoreCase(difficulty, pageable);
    }

    public Page<Challenge> searchTopRewardChallenges(Pageable pageable) {
        return challengeRepository.findTopRewardChallenges(pageable);
    }

    public Page<Challenge> searchEliteChallengesByCategory(String category, int minReward, Pageable pageable) {
        return challengeRepository.findEliteChallengesByCategory(category, minReward, pageable);
    }

    public List<Challenge> searchByKeyword(String keyword) {
        return challengeRepository.searchByKeyword(keyword);
    }

    public List<Challenge> findByDurationRange(int minDays, int maxDays) {
        return challengeRepository.findByDurationRange(minDays, maxDays);
    }

    public Integer getMaxRewardPoints() {
        Integer max = challengeRepository.findMaxRewardPoints();
        return max != null ? max : 0;
    }

    public int updateChallengeActiveStatus(Long id, boolean active) {
        return challengeRepository.updateActiveStatus(id, active);
    }

    // =========================================================================
    // Form Dropdown Helper Lists
    // =========================================================================

    public List<String> getCategories() {
        return List.of(
                "Strength & Core",
                "Cardio & HIIT",
                "Hypertrophy & Power",
                "Flexibility & Recovery",
                "Cardio Endurance",
                "Habit & Wellness"
        );
    }

    public List<String> getDifficulties() {
        return List.of("Beginner", "Intermediate", "Advanced");
    }
}
