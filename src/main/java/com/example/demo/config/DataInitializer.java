package com.example.demo.config;

import com.example.demo.service.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Initialize sample data on startup
 */
@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private VotingService votingService;
    
    @Override
    public void run(String... args) throws Exception {
        // Add some sample contestants
        votingService.addContestant("Alex Johnson", "A talented singer from Nashville with a passion for country music");
        votingService.addContestant("Maria Rodriguez", "Professional dancer specializing in Latin ballroom");
        votingService.addContestant("David Chen", "Stand-up comedian and actor from Los Angeles");
        votingService.addContestant("Sarah Williams", "Chef and cookbook author known for innovative fusion cuisine");
        
        System.out.println("Sample contestants have been added to the database.");
    }
}