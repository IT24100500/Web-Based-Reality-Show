package com.example.demo.service;

import com.example.demo.model.Contestant;
import com.example.demo.repository.ContestantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing contestants and voting
 */
@Service
public class VotingService {
    
    @Autowired
    private ContestantRepository contestantRepository;
    
    /**
     * Get all active contestants
     */
    public List<Contestant> getAllActiveContestants() {
        return contestantRepository.findByIsActiveTrue();
    }
    
    /**
     * Get contestants ordered by vote count
     */
    public List<Contestant> getContestantsOrderedByVotes() {
        return contestantRepository.findActiveContestantsOrderByVoteCountDesc();
    }
    
    /**
     * Vote for a contestant
     */
    public boolean voteForContestant(Long contestantId) {
        Optional<Contestant> contestantOpt = contestantRepository.findById(contestantId);
        if (contestantOpt.isPresent() && contestantOpt.get().getIsActive()) {
            Contestant contestant = contestantOpt.get();
            contestant.setVoteCount(contestant.getVoteCount() + 1);
            contestantRepository.save(contestant);
            return true;
        }
        return false;
    }
    
    /**
     * Get contestant by ID
     */
    public Optional<Contestant> getContestantById(Long id) {
        return contestantRepository.findById(id);
    }
    
    /**
     * Add a new contestant
     */
    public Contestant addContestant(String name, String description) {
        Contestant contestant = new Contestant(name, description);
        return contestantRepository.save(contestant);
    }
    
    /**
     * Get total vote count
     */
    public int getTotalVoteCount() {
        return getAllActiveContestants().stream()
                .mapToInt(Contestant::getVoteCount)
                .sum();
    }
}