package com.csc360.model;

/** Immutable configuration for one background job. */
public record JobDefinition(String name, int totalWork) {

    public JobDefinition {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("A job name is required.");
        }
        if (totalWork <= 0) {
            throw new IllegalArgumentException("Total work must be greater than zero.");
        }
    }
}
