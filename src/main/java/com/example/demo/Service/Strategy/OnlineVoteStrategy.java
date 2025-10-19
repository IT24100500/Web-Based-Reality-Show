package com.example.demo.Service.Strategy;

import com.example.demo.Entity.Contestant;
import com.example.demo.Entity.Vote;
import com.example.demo.Service.ResultService;
import org.springframework.stereotype.Component;

@Component
public class OnlineVoteStrategy implements VoteStrategy {

    private final ResultService resultService;

    public OnlineVoteStrategy(ResultService resultService) {
        this.resultService = resultService;
    }

    @Override
    public void processVote(Vote vote, String contestantId) {
        System.out.println("💻 Processing ONLINE vote for session " + vote.getSessionId());

        // ✅ Validate rules for online users
        if (!vote.isActive()) {
            throw new IllegalStateException("Voting session is not active!");
        }

        // ✅ Convert contestantId (String) to Contestant object
        Contestant contestant = new Contestant();
        contestant.setContestantId(contestantId);

        // ✅ Record the vote using Contestant object
        resultService.castVote(vote.getSessionId(), contestant);
    }
}
