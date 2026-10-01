package com.csc360.model;

/** A job's current progress paired with its amount of work. */
public record WorkProgress(int totalWork, double progress) {

    public WorkProgress {
        if (totalWork <= 0) {
            throw new IllegalArgumentException("Total work must be greater than zero.");
        }
    }
}
