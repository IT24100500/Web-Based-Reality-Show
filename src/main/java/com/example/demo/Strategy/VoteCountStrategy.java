package com.example.demo.Strategy;

import com.example.demo.Entity.Result;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class VoteCountStrategy implements ResultCalculationStrategy {
    @Override
    public List<Result> calculateResults(List<Result> results) {
        // Sort descending by votes
        return results.stream()
                .sorted(Comparator.comparingInt(Result::getVotesCount).reversed())
                .collect(Collectors.toList());
    }
}
