package com.example.demo.Service;

import com.example.demo.DAO.ResultDAO;
import com.example.demo.Entity.Contestant;
import com.example.demo.Entity.Result;
import com.example.demo.Entity.Vote;
import com.example.demo.Strategy.ResultCalculationStrategy;
import com.example.demo.Strategy.VoteCountStrategy;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResultService {

    private final ResultDAO resultDAO;

    @Setter
    private ResultCalculationStrategy strategy;

    public ResultService(ResultDAO resultDAO) {
        this.resultDAO = resultDAO;
        this.strategy = new VoteCountStrategy(); // Default
    }

    /** ================= CRUD ================= */
    public void saveResult(Result result) {
        if (validateResult(result)) resultDAO.save(result);
    }

    public int updateResult(Result result) {
        if (validateResult(result)) return resultDAO.update(result);
        return 0;
    }

    public int deleteResult(Long resultId) {
        return resultDAO.delete(resultId);
    }

    public List<Result> getAllResults() {
        return resultDAO.findAll();
    }

    public Optional<Result> findById(Long resultId) {
        return resultDAO.findById(resultId);
    }

    /** ================= VOTE COUNT TOTAL ================= */
    public int countVotesBySession(String sessionId) {
        return resultDAO.countVotesBySession(sessionId);
    }

    /** ================= RANKINGS (Auto Placement + Status Update) ================= */
    public List<Result> getRankings(String sessionId) {
        List<Result> results = resultDAO.findBySessionId(sessionId);
        if (results == null || results.isEmpty()) return results;

        // Sort using active strategy
        results = strategy.calculateResults(results);

        // Assign place & status
        int place = 1;
        int total = results.size();

        for (Result r : results) {
            r.setPlace(place);

            if (place == 1) {
                r.setStatus("winner");
            } else if (place == total) {
                r.setStatus("eliminated");
            } else {
                r.setStatus("safe");
            }

            resultDAO.update(r);
            place++;
        }

        return results;
    }

    /** ================= MAINTENANCE ================= */
    public int removeInvalidResults() {
        return resultDAO.removeInvalidResults();
    }

    /** ================= VALIDATION ================= */
    public boolean validateResult(Result result) {
        return result != null &&
                result.getVotingSession() != null &&
                result.getVotingSession().getSessionId() != null &&
                result.getContestant() != null &&
                result.getContestant().getContestantId() != null &&
                result.getVotesCount() >= 0 &&
                (result.getPlace() == null || result.getPlace() > 0) &&
                result.getStatus() != null && !result.getStatus().isBlank();
    }

    /** ================= CAST VOTE ================= */
    public void castVote(String sessionId, Contestant contestant) {
        List<Result> results = resultDAO.findBySessionId(sessionId);
        Optional<Result> existing = results.stream()
                .filter(r -> r.getContestant().getContestantId().equals(contestant.getContestantId()))
                .findFirst();

        if (existing.isPresent()) {
            Result result = existing.get();
            result.setVotesCount(result.getVotesCount() + 1);
            resultDAO.update(result);
        } else {
            Result newResult = new Result();
            Vote voteSession = new Vote();
            voteSession.setSessionId(sessionId);
            voteSession.setActive(true);
            voteSession.setMaxVotesPerUser(1);
            voteSession.setStatus("Ongoing");

            newResult.setVotingSession(voteSession);
            newResult.setContestant(contestant);
            newResult.setVotesCount(1);
            newResult.setPlace(null);
            newResult.setStatus("safe");
            resultDAO.save(newResult);
        }
    }
}
