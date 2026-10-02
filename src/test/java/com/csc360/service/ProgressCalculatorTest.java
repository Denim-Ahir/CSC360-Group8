package com.csc360.service;

import com.csc360.model.WorkProgress;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProgressCalculatorTest {

    @Test
    void calculatesWeightedProgress() {
        double result = ProgressCalculator.calculate(List.of(
                new WorkProgress(100, 1.0),
                new WorkProgress(150, 0.5),
                new WorkProgress(200, 0.0)
        ));

        assertEquals(175.0 / 450.0, result, 0.00001);
    }

    @Test
    void returnsZeroWhenNoJobsAreProvided() {
        assertEquals(0, ProgressCalculator.calculate(List.of()));
    }
}
