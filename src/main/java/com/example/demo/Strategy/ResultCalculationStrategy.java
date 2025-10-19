package com.example.demo.Strategy;

import com.example.demo.Entity.Result;
import java.util.List;

public interface ResultCalculationStrategy {
    List<Result> calculateResults(List<Result> results);
}
