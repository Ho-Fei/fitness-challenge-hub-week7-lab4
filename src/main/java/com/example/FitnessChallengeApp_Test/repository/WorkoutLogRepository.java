package com.example.FitnessChallengeApp_Test.repository;

import com.example.FitnessChallengeApp_Test.model.WorkoutLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA Repository for WorkoutLog entities.
 * Students will extend this repository in the Lab with custom derived queries
 * and @Query JPQL methods supporting Pageable pagination and sorting.
 */
@Repository
public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, Long> {
}

