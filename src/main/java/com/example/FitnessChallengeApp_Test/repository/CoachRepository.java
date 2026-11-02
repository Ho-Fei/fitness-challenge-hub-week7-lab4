package com.example.FitnessChallengeApp_Test.repository;

import com.example.FitnessChallengeApp_Test.model.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for Coach entities.
 */
@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {

    List<Coach> findBySpecialtyIgnoreCase(String specialty);
}

