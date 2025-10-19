package com.example.demo.Service.observer;

import com.example.demo.Entity.Vote;
import org.springframework.stereotype.Component;

@Component
public class ModeratorPanel implements VoteObserver {
    @Override
    public void update(Vote vote) {
        System.out.println("👁️ Moderator notified — new vote in session: " + vote.getSessionId());
    }
}
