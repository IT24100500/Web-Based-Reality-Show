package com.example.demo.controller;

import com.example.demo.model.Contestant;
import com.example.demo.service.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Main controller for the voting system
 */
@Controller
public class VotingController {
    
    @Autowired
    private VotingService votingService;
    
    /**
     * Home page - display all contestants and voting interface
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("contestants", votingService.getContestantsOrderedByVotes());
        model.addAttribute("totalVotes", votingService.getTotalVoteCount());
        return "index";
    }
    
    /**
     * Handle voting
     */
    @PostMapping("/vote/{id}")
    public String vote(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean success = votingService.voteForContestant(id);
        if (success) {
            redirectAttributes.addFlashAttribute("message", "Thank you for voting!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Unable to vote. Please try again.");
        }
        return "redirect:/";
    }
    
    /**
     * Results page - show current standings
     */
    @GetMapping("/results")
    public String results(Model model) {
        model.addAttribute("contestants", votingService.getContestantsOrderedByVotes());
        model.addAttribute("totalVotes", votingService.getTotalVoteCount());
        return "results";
    }
    
    /**
     * API endpoint to get voting results as JSON
     */
    @GetMapping("/api/results")
    @ResponseBody
    public VotingResults getResults() {
        return new VotingResults(
                votingService.getContestantsOrderedByVotes(),
                votingService.getTotalVoteCount()
        );
    }
    
    /**
     * Simple data class for API responses
     */
    public static class VotingResults {
        private java.util.List<Contestant> contestants;
        private int totalVotes;
        
        public VotingResults(java.util.List<Contestant> contestants, int totalVotes) {
            this.contestants = contestants;
            this.totalVotes = totalVotes;
        }
        
        public java.util.List<Contestant> getContestants() { return contestants; }
        public int getTotalVotes() { return totalVotes; }
    }
}