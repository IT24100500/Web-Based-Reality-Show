package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Entity representing a reality show contestant
 */
@Entity
@Table(name = "contestants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contestant {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "vote_count", nullable = false)
    private Integer voteCount = 0;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    public Contestant(String name, String description) {
        this.name = name;
        this.description = description;
        this.voteCount = 0;
        this.isActive = true;
    }
}