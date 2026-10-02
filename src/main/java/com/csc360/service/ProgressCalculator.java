package com.csc360.service;

import com.csc360.model.WorkProgress;

import java.util.Collection;

/** Calculates weighted progress without depending on JavaFX controls. */
public final class ProgressCalculator {

    private ProgressCalculator() {
    }

    public static double calculate(Collection<WorkProgress> jobs) {
        if (jobs == null || jobs.isEmpty()) {
            return 0;
        }

        double completedWork = 0;
        double totalWork = 0;

        for (WorkProgress job : jobs) {
            double progress = Math.clamp(job.progress(), 0, 1);
            completedWork += progress * job.totalWork();
            totalWork += job.totalWork();
        }

        return totalWork == 0 ? 0 : completedWork / totalWork;
    }
}
