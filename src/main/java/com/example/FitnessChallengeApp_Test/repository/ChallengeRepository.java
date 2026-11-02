package com.example.FitnessChallengeApp_Test.repository;

import com.example.FitnessChallengeApp_Test.model.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA Repository for Challenge entities.
 * Demonstrates Derived Query Methods, Custom JPQL @Query annotations,
 * Projections, Aggregates, @Modifying queries, and Pagination & Sorting via
 * Pageable.
 */
@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

        // =========================================================================
        // 1. Derived Query Methods (Method Name Keywords)
        // =========================================================================

        // Exact match by category
        List<Challenge> findByCategory(String category);

        // Boolean filter + ordering
        List<Challenge> findByActiveTrueOrderByRewardPointsDesc();

        // Case-insensitive partial title search with pagination
        Page<Challenge> findByTitleContainingIgnoreCase(String title, Pageable pageable);

        // Numeric threshold filter with pagination
        Page<Challenge> findByRewardPointsGreaterThanEqual(int minRewardPoints, Pageable pageable);

        // Case-insensitive difficulty filter with pagination
        Page<Challenge> findByDifficultyIgnoreCase(String difficulty, Pageable pageable);

        // =========================================================================
        // 2. Custom JPQL Queries (@Query)
        // =========================================================================

        // Custom JPQL sorting: rank challenges by highest XP reward points descending
        @Query("SELECT c FROM Challenge c ORDER BY c.rewardPoints DESC")
        Page<Challenge> findTopRewardChallenges(Pageable pageable);

        // Custom JPQL with named @Param parameters
        @Query("SELECT c FROM Challenge c WHERE LOWER(c.category) LIKE LOWER(CONCAT('%', :cat, '%')) AND c.rewardPoints >= :minReward ORDER BY c.rewardPoints DESC")
        Page<Challenge> findEliteChallengesByCategory(
                        @Param("cat") String category,
                        @Param("minReward") int minReward,
                        Pageable pageable);

        // Full-text keyword search across title and description
        @Query("SELECT c FROM Challenge c WHERE " +
                        "LOWER(c.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                        "LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%'))")
        List<Challenge> searchByKeyword(@Param("search") String search);

        // Range query with BETWEEN
        @Query("SELECT c FROM Challenge c WHERE c.durationDays BETWEEN :minDays AND :maxDays ORDER BY c.durationDays ASC")
        List<Challenge> findByDurationRange(
                        @Param("minDays") int minDays,
                        @Param("maxDays") int maxDays);

        // Projection: select distinct categories
        @Query("SELECT DISTINCT c.category FROM Challenge c ORDER BY c.category ASC")
        List<String> findDistinctCategories();

        // Aggregate function: maximum XP reward points across all challenges
        @Query("SELECT MAX(c.rewardPoints) FROM Challenge c")
        Integer findMaxRewardPoints();

        // Modifying DML query: toggle active status directly via JPQL UPDATE
        @Modifying(clearAutomatically = true)
        @Transactional
        @Query("UPDATE Challenge c SET c.active = :active WHERE c.id = :id")
        int updateActiveStatus(@Param("id") Long id, @Param("active") boolean active);

}
