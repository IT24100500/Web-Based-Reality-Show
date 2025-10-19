package com.example.demo.Strategy;

import com.example.demo.Entity.Result;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class WeightedScoreStrategy implements ResultCalculationStrategy {
    @Override
    public List<Result> calculateResults(List<Result> results) {
        // Weighted example: 70% votes + 30% inverse of place
        return results.stream()
                .sorted(Comparator.comparingDouble((Result r) -> {
                    double votesScore = r.getVotesCount() * 0.7;
                    double placeScore = (r.getPlace() != null ? (1.0 / r.getPlace()) * 100 : 0) * 0.3;
                    return votesScore + placeScore;
                }).reversed())
                .collect(Collectors.toList());
    }
}
