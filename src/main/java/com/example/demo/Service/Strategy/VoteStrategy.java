package com.example.demo.Service.Strategy;

import com.example.demo.Entity.Vote;

public interface VoteStrategy {
    void processVote(Vote vote, String contestantId);
}
