package com.example.demo.Controller;

import com.example.demo.Entity.Admin;
import com.example.demo.Entity.Vote;
import com.example.demo.Service.ResultService;
import com.example.demo.Service.ShowService;
import com.example.demo.Service.VoteService;
import com.example.demo.Service.Strategy.OnlineVoteStrategy;
import com.example.demo.Service.Strategy.SMSVoteStrategy;
import com.example.demo.Service.Strategy.VoteStrategy;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class VoteC {

    private final VoteService voteService;
    private final ShowService showService;
    private final ResultService resultService;
    private final OnlineVoteStrategy onlineVoteStrategy;
    private final SMSVoteStrategy smsVoteStrategy;

    public VoteC(VoteService voteService,
                 ShowService showService,
                 ResultService resultService,
                 OnlineVoteStrategy onlineVoteStrategy,
                 SMSVoteStrategy smsVoteStrategy) {
        this.voteService = voteService;
        this.showService = showService;
        this.resultService = resultService;
        this.onlineVoteStrategy = onlineVoteStrategy;
        this.smsVoteStrategy = smsVoteStrategy;
    }

    /** ================= ADMIN: VIEW ALL VOTING SESSIONS ================= */
    @GetMapping("/voteSessionA")
    public String viewAllSessionsAdmin(HttpSession session, Model model) {
        Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
        if (loggedInAdmin == null) return "redirect:/loginA";

        model.addAttribute("sessionList", voteService.getAllSessions());
        model.addAttribute("episodeList", showService.getAllShows());
        model.addAttribute("newSession", new Vote());
        return "voteSessionA";
    }

    /** ================= USER: VIEW ACTIVE VOTING SESSIONS ================= */
    @GetMapping("/voteSessionU")
    public String viewUserVotingSessions(Model model) {
        List<Vote> ongoingSessions = voteService.getAllSessions().stream()
                .filter(s -> "Ongoing".equalsIgnoreCase(s.getStatus()))
                .toList();

        model.addAttribute("sessionList", ongoingSessions);
        return "voteSessionU";
    }

    /** ================= ADD NEW VOTING SESSION ================= */
    @PostMapping("/session/add")
    public String addSession(@ModelAttribute Vote session, HttpSession httpSession) {
        Admin loggedInAdmin = (Admin) httpSession.getAttribute("loggedInAdmin");
        if (loggedInAdmin == null) return "redirect:/loginA";

        voteService.saveSession(session);
        return "redirect:/voteSessionA";
    }

    /** ================= UPDATE EXISTING VOTING SESSION ================= */
    @PostMapping("/session/update")
    public String updateSession(@ModelAttribute Vote session, HttpSession httpSession) {
        Admin loggedInAdmin = (Admin) httpSession.getAttribute("loggedInAdmin");
        if (loggedInAdmin == null) return "redirect:/loginA";

        voteService.updateSession(session);
        return "redirect:/voteSessionA";
    }

    /** ================= DELETE VOTING SESSION ================= */
    @PostMapping("/session/delete/{id}")
    public String deleteSession(@PathVariable("id") String sessionId, HttpSession httpSession) {
        Admin loggedInAdmin = (Admin) httpSession.getAttribute("loggedInAdmin");
        if (loggedInAdmin == null) return "redirect:/loginA";

        voteService.deleteSession(sessionId);
        return "redirect:/voteSessionA";
    }

    /** ================= USER: CAST VOTE ================= */
    @PostMapping("/vote/cast")
    public String castVote(@RequestParam("sessionId") String sessionId,
                           @RequestParam("contestantId") String contestantId,
                           @RequestParam(value = "type", defaultValue = "ONLINE") String type, // "ONLINE" or "SMS"
                           HttpSession session,
                           Model model) {

        // Fetch session
        var voteSessionOpt = voteService.findSessionById(sessionId);
        if (voteSessionOpt.isEmpty() || !voteSessionOpt.get().isActive()) {
            model.addAttribute("message", "⚠️ Voting is not available for this session.");
            return "epiforUser";
        }

        var voteSession = voteSessionOpt.get();

        try {
            // ✅ Select appropriate voting strategy dynamically
            VoteStrategy strategy = switch (type.toUpperCase()) {
                case "SMS" -> smsVoteStrategy;
                default -> onlineVoteStrategy;
            };

            // ✅ Process vote using selected strategy
            strategy.processVote(voteSession, contestantId);

            model.addAttribute("message", "✅ Your " + type + " vote has been recorded successfully!");
        } catch (Exception e) {
            model.addAttribute("message", "❌ Voting failed: " + e.getMessage());
        }

        // ✅ Reload episode data for updated UI
        var episodeOpt = showService.findShowById(voteSession.getShow().getEpisodeId());
        episodeOpt.ifPresent(episode -> model.addAttribute("episode", episode));

        return "epiforUser";
    }
}
