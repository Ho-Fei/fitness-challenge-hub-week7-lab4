package com.example.FitnessChallengeApp_Test.controller;

import com.example.FitnessChallengeApp_Test.model.Coach;
import com.example.FitnessChallengeApp_Test.service.CoachService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller for browsing and registering certified FitSquad Coaches.
 */
@Controller
public class CoachController {

    private final CoachService coachService;

    public CoachController(CoachService coachService) {
        this.coachService = coachService;
    }

    @GetMapping("/coaches")
    public String showCoaches(Model model) {
        model.addAttribute("coaches", coachService.getAllCoaches());
        model.addAttribute("pageTitle", "FitSquad Hub - Certified Coaches");
        return "coaches";
    }

    @GetMapping("/create-coach")
    public String showCreateCoachForm(Model model) {
        model.addAttribute("coach", new Coach());
        model.addAttribute("specialties", coachService.getSpecialties());
        model.addAttribute("pageTitle", "FitSquad Hub - Register Coach");
        return "create-coach";
    }

    @PostMapping("/create-coach")
    public String processCreateCoach(
            @Valid @ModelAttribute("coach") Coach coach,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("specialties", coachService.getSpecialties());
            model.addAttribute("pageTitle", "FitSquad Hub - Register Coach");
            return "create-coach";
        }

        coachService.addCoach(coach);
        return "redirect:/coaches";
    }
}

