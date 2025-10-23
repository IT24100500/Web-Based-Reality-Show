package com.example.demo.Controller;

import com.example.demo.Entity.Admin;
import com.example.demo.Entity.Result;
import com.example.demo.Entity.User;
import com.example.demo.Entity.Vote;
import com.example.demo.Service.ResultService;
import com.example.demo.Service.VoteService;
import com.example.demo.Service.ContestantService;
import com.example.demo.Strategy.VoteCountStrategy;
import com.example.demo.Strategy.WeightedScoreStrategy;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
public class ResultC {

    private final ResultService resultService;
    private final VoteService voteService;
    private final ContestantService contestantService;

    public ResultC(ResultService resultService, VoteService voteService, ContestantService contestantService) {
        this.resultService = resultService;
        this.voteService = voteService;
        this.contestantService = contestantService;
    }

    /** ================= ADMIN: VIEW RESULTS ================= */
    @GetMapping("/resultsA")
    public String viewAllResults(Model model, HttpSession session) {
        Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
        if (loggedInAdmin == null) return "redirect:/loginA";

        // Restore strategy from session
        String strategyType = (String) session.getAttribute("strategyType");
        if ("weighted".equalsIgnoreCase(strategyType)) {
            resultService.setStrategy(new WeightedScoreStrategy());
        } else {
            resultService.setStrategy(new VoteCountStrategy());
        }

        // Use latest session
        List<Vote> sessions = voteService.getAllSessions();
        if (sessions.isEmpty()) {
            model.addAttribute("message", "No voting sessions found.");
            return "resultsA";
        }

        Vote latestSession = sessions.get(sessions.size() - 1);
        model.addAttribute("resultList", resultService.getRankings(latestSession.getSessionId()));
        model.addAttribute("sessionList", sessions);
        model.addAttribute("contestantList", contestantService.getAllContestants());
        model.addAttribute("newResult", new Result());
        model.addAttribute("totalVotes", resultService.countVotesBySession(latestSession.getSessionId()));
        model.addAttribute("currentStrategy", strategyType != null ? strategyType : "vote");
        return "resultsA";
    }

    /** ================= USER: VIEW RESULTS ================= */
    @GetMapping("/resultsU")
    public String viewUserResults(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/loginU";

        List<Vote> activeSessions = voteService.getActiveSessions();
        if (activeSessions.isEmpty()) {
            model.addAttribute("message", "No active sessions.");
            return "resultsU";
        }

        Vote latestSession = activeSessions.stream()
                .max(Comparator.comparing(Vote::getStartTime))
                .orElse(null);

        if (latestSession != null) {
            model.addAttribute("resultList", resultService.getRankings(latestSession.getSessionId()));
            model.addAttribute("totalVotes", resultService.countVotesBySession(latestSession.getSessionId()));
        }

        model.addAttribute("session", latestSession);
        return "resultsU";
    }

    /** ================= ADD RESULT ================= */
    @PostMapping("/result/add")
    public String addResult(@ModelAttribute Result result, HttpSession session) {
        Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
        if (loggedInAdmin == null) return "redirect:/loginA";

        if (resultService.validateResult(result)) {
            resultService.saveResult(result);
        }
        return "redirect:/resultsA";
    }

    /** ================= UPDATE RESULT ================= */
    @PostMapping("/result/update")
    public String updateResult(@ModelAttribute Result result, HttpSession session) {
        Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
        if (loggedInAdmin == null) return "redirect:/loginA";

        if (resultService.validateResult(result)) {
            resultService.updateResult(result);
        }

        // redirect back to results page
        return "redirect:/resultsA";
    }

    /** ================= STRATEGY SWITCH ================= */
    @PostMapping("/results/strategy")
    public String switchStrategy(@RequestParam("type") String type, HttpSession session) {
        Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
        if (loggedInAdmin == null) return "redirect:/loginA";

        session.setAttribute("strategyType", type.toLowerCase());
        if ("weighted".equalsIgnoreCase(type))
            resultService.setStrategy(new WeightedScoreStrategy());
        else
            resultService.setStrategy(new VoteCountStrategy());

        return "redirect:/resultsA";
    }

    /** ================= CLEAN INVALID RESULTS ================= */
    @PostMapping("/results/clean")
    public String cleanInvalidResults(HttpSession session) {
        Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
        if (loggedInAdmin == null) return "redirect:/loginA";

        resultService.removeInvalidResults();
        return "redirect:/resultsA";
    }

    /** ================= DELETE RESULT ================= */
    @PostMapping("/result/delete/{id}")
    public String deleteResult(@PathVariable("id") Long resultId, HttpSession session) {
        Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
        if (loggedInAdmin == null) return "redirect:/loginA";

        resultService.deleteResult(resultId);
        return "redirect:/resultsA";
    }
}
