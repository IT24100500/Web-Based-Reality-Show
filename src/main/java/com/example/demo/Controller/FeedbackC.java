package com.example.demo.Controller;

import com.example.demo.Entity.Admin;
import com.example.demo.Entity.Feedback;
import com.example.demo.Entity.User;
import com.example.demo.Service.FeedbackService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class FeedbackC {

    private final FeedbackService feedbackService;

    public FeedbackC(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    /** 🛠 Disable form binding of submittedAt (prevents LocalDateTime parsing errors) */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDateTime.class, "submittedAt", new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(null);
            }
        });
    }

    /** ================= USER SIDE ================= */
    @GetMapping("/feedbackU")
    public String viewUserFeedback(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/loginU";

        List<Feedback> myFeedback = feedbackService.findByUserId(loggedInUser.getUserId());
        model.addAttribute("feedbackList", myFeedback);
        model.addAttribute("newFeedback", new Feedback());
        model.addAttribute("user", loggedInUser);
        return "feedbackU";
    }

    @PostMapping("/feedback/add")
    public String addFeedback(@ModelAttribute Feedback feedback, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/loginU";

        feedback.setUser(loggedInUser);
        feedback.setSubmittedAt(LocalDateTime.now());
        feedbackService.saveFeedback(feedback);
        return "redirect:/feedbackU";
    }

    /** ================= ADMIN SIDE ================= */
    @GetMapping("/admin/feedbackA")
    public String viewAllFeedbackAdmin(HttpSession session, Model model) {
        Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
        if (loggedInAdmin == null) return "redirect:/loginA";

        List<Feedback> allFeedback = feedbackService.getAllFeedback();
        model.addAttribute("feedbackList", allFeedback);

        double averageRating = allFeedback.stream()
                .filter(f -> f.getRating() != null)
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0.0);
        model.addAttribute("averageRating", averageRating);

        return "feedbackA";
    }

    /** ================= ADMIN UPDATE (via Modal) ================= */
    @PostMapping("/admin/feedback/update")
    public String updateFeedback(@ModelAttribute Feedback feedback, HttpSession session) {
        Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
        if (loggedInAdmin == null) return "redirect:/loginA";

        Optional<Feedback> existingOpt = feedbackService.findById(feedback.getFeedbackId());
        if (existingOpt.isEmpty()) return "redirect:/admin/feedbackA";

        Feedback existing = existingOpt.get();
        feedback.setUser(existing.getUser());          // Keep original user
        feedback.setSubmittedAt(LocalDateTime.now());  // Update timestamp

        feedbackService.updateFeedback(feedback);
        return "redirect:/admin/feedbackA";
    }

    @PostMapping("/admin/feedback/delete/{id}")
    public String deleteFeedback(@PathVariable("id") Long feedbackId, HttpSession session) {
        Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
        if (loggedInAdmin == null) return "redirect:/loginA";

        feedbackService.deleteFeedback(feedbackId);
        return "redirect:/admin/feedbackA";
    }
}
