package com.example.FitnessChallengeApp_Test.controller;

import com.example.FitnessChallengeApp_Test.model.Challenge;
import com.example.FitnessChallengeApp_Test.model.Coach;
import com.example.FitnessChallengeApp_Test.service.ChallengeService;
import com.example.FitnessChallengeApp_Test.service.CoachService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for FitSquad Hub challenges.
 * Demonstrates Spring Data JPA pagination, multi-criteria sorting, custom query routing,
 * @ManyToOne Coach association, and Jakarta Bean Validation with PRG.
 */
@Controller
public class WorkoutChallengeController {

    private final ChallengeService challengeService;
    private final CoachService coachService;

    public WorkoutChallengeController(ChallengeService challengeService, CoachService coachService) {
        this.challengeService = challengeService;
        this.coachService = coachService;
    }

    @GetMapping({"/", "/challenges"})
    public String showChallenges(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "all") String filterType,
            Model model
    ) {
        Sort.Direction sortDir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sort));

        Page<Challenge> challengePage;

        if ("title".equalsIgnoreCase(filterType) && search != null && !search.trim().isEmpty()) {
            challengePage = challengeService.searchByTitleIgnoreCase(search.trim(), pageable);
        } else if ("reward".equalsIgnoreCase(filterType) && search != null && !search.trim().isEmpty()) {
            int minReward = 300;
            try {
                minReward = Integer.parseInt(search.trim());
            } catch (NumberFormatException e) {
                // Keep default minimum reward threshold if input is not a valid integer
            }
            challengePage = challengeService.searchByMinRewardPoints(minReward, pageable);
        } else if ("difficulty".equalsIgnoreCase(filterType) && search != null && !search.trim().isEmpty()) {
            challengePage = challengeService.searchByDifficulty(search.trim(), pageable);
        } else if ("top".equalsIgnoreCase(filterType)) {
            challengePage = challengeService.searchTopRewardChallenges(pageable);
        } else if ("elite".equalsIgnoreCase(filterType) && search != null && !search.trim().isEmpty()) {
            challengePage = challengeService.searchEliteChallengesByCategory(search.trim(), 500, pageable);
        } else {
            challengePage = challengeService.getChallenges(pageable);
        }

        model.addAttribute("pageTitle", "FitSquad Hub - Fitness Challenge Platform");

        // Pagination & content attributes
        model.addAttribute("challenges", challengePage.getContent());
        model.addAttribute("totalPages", challengePage.getTotalPages());
        model.addAttribute("totalElements", challengePage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("currentPage", page);
        model.addAttribute("hasPrevious", challengePage.hasPrevious());
        model.addAttribute("hasNext", challengePage.hasNext());

        // Preserve filter & sort parameters in the view
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("filterType", filterType);

        return "challenges";
    }

    @GetMapping("/create-challenge")
    public String showCreateForm(Model model) {
        Challenge challenge = new Challenge();
        challenge.setCoach(new Coach());
        model.addAttribute("challenge", challenge);
        model.addAttribute("categories", challengeService.getCategories());
        model.addAttribute("difficulties", challengeService.getDifficulties());
        model.addAttribute("coaches", coachService.getAllCoaches());
        model.addAttribute("pageTitle", "FitSquad Hub - Create New Challenge");
        return "create-challenge";
    }

    @PostMapping("/create-challenge")
    public String processCreateChallenge(
            @Valid @ModelAttribute("challenge") Challenge challenge,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", challengeService.getCategories());
            model.addAttribute("difficulties", challengeService.getDifficulties());
            model.addAttribute("coaches", coachService.getAllCoaches());
            model.addAttribute("pageTitle", "FitSquad Hub - Create New Challenge");
            return "create-challenge";
        }

        if (challenge.getCoach() != null && challenge.getCoach().getId() != null) {
            Coach selectedCoach = coachService.getCoachById(challenge.getCoach().getId());
            challenge.setCoach(selectedCoach);
        } else {
            challenge.setCoach(null);
        }

        challengeService.addChallenge(challenge);
        return "redirect:/challenges";
    }
}
