package com.example.demo.Service.observer;

import com.example.demo.Entity.Vote;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VotePublisher {
    private final List<VoteObserver> observers = new ArrayList<>();

    public void addObserver(VoteObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(Vote vote) {
        for (VoteObserver observer : observers) {
            observer.update(vote);
        }
    }
}
