package com.example.FitnessChallengeApp_Test;

import com.example.FitnessChallengeApp_Test.controller.WorkoutChallengeController;
import com.example.FitnessChallengeApp_Test.controller.WorkoutLogController;
import com.example.FitnessChallengeApp_Test.model.Challenge;
import com.example.FitnessChallengeApp_Test.model.Coach;
import com.example.FitnessChallengeApp_Test.model.WorkoutLog;
import com.example.FitnessChallengeApp_Test.service.ChallengeService;
import com.example.FitnessChallengeApp_Test.service.CoachService;
import com.example.FitnessChallengeApp_Test.service.WorkoutLogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class WorkoutChallengeControllerTest {

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private CoachService coachService;

    @Autowired
    private WorkoutLogService workoutLogService;

    @Autowired
    private WorkoutChallengeController challengeController;

    @Autowired
    private WorkoutLogController workoutLogController;

    @Test
    void showChallenges_shouldPopulatePaginatedModelAndReturnView() {
        Model model = new ExtendedModelMap();
        String viewName = challengeController.showChallenges(0, 6, "id", "ASC", null, "all", model);

        assertEquals("challenges", viewName);
        assertTrue(model.containsAttribute("pageTitle"));
        assertTrue(model.containsAttribute("challenges"));
        assertEquals(2, model.getAttribute("totalPages"));
        assertEquals(12L, model.getAttribute("totalElements"));

        @SuppressWarnings("unchecked")
        List<Challenge> challenges = (List<Challenge>) model.getAttribute("challenges");
        assertNotNull(challenges);
        assertEquals(6, challenges.size());
        assertEquals("30-Day Core Crusher", challenges.get(0).getTitle());
        assertNotNull(challenges.get(0).getCoach());
    }

    @Test
    void customJpaQueries_shouldFilterAndSortChallenges() {
        // 1. Derived query: findByTitleContainingIgnoreCase
        Page<Challenge> coreChallenges = challengeService.searchByTitleIgnoreCase("core", PageRequest.of(0, 10));
        assertEquals(1, coreChallenges.getTotalElements());
        assertEquals("30-Day Core Crusher", coreChallenges.getContent().get(0).getTitle());

        // 2. Derived query: findByRewardPointsGreaterThanEqual
        Page<Challenge> highReward = challengeService.searchByMinRewardPoints(600, PageRequest.of(0, 10));
        assertTrue(highReward.getTotalElements() >= 4);

        // 3. Custom @Query JPQL: findTopRewardChallenges
        Page<Challenge> topReward = challengeService.searchTopRewardChallenges(PageRequest.of(0, 5));
        assertFalse(topReward.isEmpty());
        assertEquals(900, topReward.getContent().get(0).getRewardPoints());

        // 4. Custom @Query JPQL with @Param: findEliteChallengesByCategory
        Page<Challenge> eliteCardio = challengeService.searchEliteChallengesByCategory("Cardio", 500, PageRequest.of(0, 10));
        assertTrue(eliteCardio.getTotalElements() >= 2);
    }

    @Test
    void showCreateForm_shouldPrepareEmptyModelWithCoachesAndReturnView() {
        Model model = new ExtendedModelMap();
        String viewName = challengeController.showCreateForm(model);

        assertEquals("create-challenge", viewName);
        assertTrue(model.containsAttribute("challenge"));
        assertTrue(model.containsAttribute("categories"));
        assertTrue(model.containsAttribute("difficulties"));
        assertTrue(model.containsAttribute("coaches"));

        @SuppressWarnings("unchecked")
        List<Coach> coaches = (List<Coach>) model.getAttribute("coaches");
        assertEquals(6, coaches.size());
    }

    @Test
    void processCreateChallenge_withValidationErrors_shouldReturnFormView() {
        Challenge invalidChallenge = new Challenge();
        BindingResult bindingResult = new BeanPropertyBindingResult(invalidChallenge, "challenge");
        bindingResult.rejectValue("title", "NotBlank", "Challenge title is required");

        Model model = new ExtendedModelMap();
        String viewName = challengeController.processCreateChallenge(invalidChallenge, bindingResult, model);

        assertEquals("create-challenge", viewName);
        assertTrue(model.containsAttribute("categories"));
        assertTrue(model.containsAttribute("difficulties"));
        assertTrue(model.containsAttribute("coaches"));
        assertEquals(12, challengeService.getAllChallenges().size());
    }

    @Test
    void processCreateChallenge_validSubmission_shouldPersistChallengeAndRedirect() {
        Challenge validChallenge = new Challenge(
                null,
                "Spartan 100 Burpee Blast",
                "Cardio & HIIT",
                "Test burpee endurance progression for high metabolic conditioning.",
                "100 burpees unbroken or partitioned daily",
                14,
                600,
                "Advanced",
                null,
                true
        );
        Coach coach = new Coach();
        coach.setId(1L);
        validChallenge.setCoach(coach);

        BindingResult bindingResult = new BeanPropertyBindingResult(validChallenge, "challenge");
        Model model = new ExtendedModelMap();

        String viewName = challengeController.processCreateChallenge(validChallenge, bindingResult, model);

        assertEquals("redirect:/challenges", viewName);
        assertEquals(13, challengeService.getAllChallenges().size());
    }

    @Test
    void processLogWorkout_validSubmission_shouldPersistWorkoutLogWithManyToOneChallenge() {
        WorkoutLog log = new WorkoutLog();
        log.setAthleteName("Taylor Swift");
        Challenge challengeRef = new Challenge();
        challengeRef.setId(1L);
        log.setChallenge(challengeRef);
        log.setDurationMinutes(40);
        log.setCaloriesBurned(350);
        log.setIntensityLevel("High");
        log.setWorkoutDate("2026-11-02");
        log.setNotes("Strong core session!");

        BindingResult bindingResult = new BeanPropertyBindingResult(log, "workoutLog");
        Model model = new ExtendedModelMap();

        int initialCount = workoutLogService.getAllLogs().size();
        String viewName = workoutLogController.processLogWorkout(log, bindingResult, model);

        assertEquals("redirect:/workout-logs", viewName);
        assertEquals(initialCount + 1, workoutLogService.getAllLogs().size());
    }
}
