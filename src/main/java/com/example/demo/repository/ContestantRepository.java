package com.example.demo.repository;

import com.example.demo.model.Contestant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Contestant entities
 */
@Repository
public interface ContestantRepository extends JpaRepository<Contestant, Long> {
    
    /**
     * Find all active contestants
     */
    List<Contestant> findByIsActiveTrue();
    
    /**
     * Find contestants ordered by vote count (descending)
     */
    @Query("SELECT c FROM Contestant c WHERE c.isActive = true ORDER BY c.voteCount DESC")
    List<Contestant> findActiveContestantsOrderByVoteCountDesc();
}