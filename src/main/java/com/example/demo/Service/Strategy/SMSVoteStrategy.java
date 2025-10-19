package com.example.demo.Service.Strategy;

import com.example.demo.Entity.Contestant;
import com.example.demo.Entity.Vote;
import com.example.demo.Service.ResultService;
import org.springframework.stereotype.Component;

@Component
public class SMSVoteStrategy implements VoteStrategy {

    private final ResultService resultService;

    public SMSVoteStrategy(ResultService resultService) {
        this.resultService = resultService;
    }

    @Override
    public void processVote(Vote vote, String contestantId) {
        System.out.println("📱 Processing SMS vote for session " + vote.getSessionId());

        // ✅ Example: validate SMS sender or message format
        // (e.g., ensure phone number registered, correct code, etc.)
        if (!vote.isActive()) {
            throw new IllegalStateException("Voting session is not active!");
        }

        // ✅ Convert String contestantId → Contestant object
        Contestant contestant = new Contestant();
        contestant.setContestantId(contestantId);

        // ✅ Record the vote properly
        resultService.castVote(vote.getSessionId(), contestant);
    }
}
