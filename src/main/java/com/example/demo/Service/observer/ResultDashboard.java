package com.example.demo.Service.observer;

import com.example.demo.Entity.Vote;
import org.springframework.stereotype.Component;

@Component
public class ResultDashboard implements VoteObserver {
    @Override
    public void update(Vote vote) {
        System.out.println("📊 Dashboard updated — new vote in session: " + vote.getSessionId());
    }
}
