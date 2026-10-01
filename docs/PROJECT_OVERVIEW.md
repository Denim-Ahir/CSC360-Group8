# Composite Progress Bar — Project Overview

## 1. Project Summary

Composite Progress Bar is a JavaFX desktop application that simulates several background jobs running at the same time. Each job shows its own progress, status, and Cancel button. A separate overall bar presents the weighted progress of all jobs together.

The project was developed for CSC 360 by Group 8:

- Denim Ahir
- Kishankumar Naik
- Devansh Maheshwari

## 2. Objective

The application demonstrates safe background work in a graphical user interface. It keeps the JavaFX user-interface thread responsive while work is performed by background tasks, then updates the interface with task progress and status.

## 3. Current Scope

The current prototype provides:

- Three simulated jobs: Job 1, Job 2, and Job 3.
- Independent progress bars and status labels.
- An individual Cancel button for every job.
- Start All Jobs and Stop All Jobs controls.
- Restartable job runs after all jobs finish or are stopped.
- A weighted overall-progress calculation.
- A responsive 900 × 650 default application window.

The jobs are simulated. Each unit of work pauses for 50 milliseconds, rather than processing a real file or network request.

## 4. Functional Requirements

| ID | Requirement | Current behaviour |
| --- | --- | --- |
| FR-1 | Start all jobs together | Start All Jobs creates and submits one new task for each job. |
| FR-2 | Show individual progress | Each job progress bar is bound to its task progress. |
| FR-3 | Show job status | Jobs display Waiting, Running, Completed, Cancelled, or Failed. |
| FR-4 | Cancel one job | The job's Cancel button cancels only that job. |
| FR-5 | Stop all jobs | Stop All Jobs requests cancellation for every active job. |
| FR-6 | Show combined progress | The overall bar calculates weighted progress across all jobs. |
| FR-7 | Support another run | After every job reaches a terminal state, Start All Jobs becomes available again. |

## 5. Architecture

```text
JavaFX Application Thread
        |
        +-- CompositeProgressApp.start() builds labels, bars, and buttons
        |
        +-- Start All Jobs creates a Task for each Job
                         |
                         v
                ExecutorService (3 worker threads)
                         |
                         v
                  JavaFX Task.call() simulates work
                         |
                         v
        task progress/message listeners update the JavaFX interface
```

`Main` is the launcher. `CompositeProgressApp` builds and manages the JavaFX interface; `JobDefinition` describes each job; and `ProgressCalculator` performs the weighted-progress calculation independently from the GUI. The executor has three worker threads, allowing all three simulated jobs to execute concurrently.

## 6. Weighted Overall Progress

The total work for the three jobs is 100, 150, and 200 units. Overall progress is calculated as:

```text
overall progress = Σ(job progress × job total work) / Σ(job total work)
```

The combined total is 450 work units. Therefore, a job with more work has a larger effect on the overall bar: Job 3 contributes more than Job 1.

## 7. Current Limitations

- Jobs use fixed simulated work, not actual files or services.
- The task configuration is currently hard-coded in `CompositeProgressApp`.
- The weighted-progress calculator has unit tests; the JavaFX UI flow is still manually tested.
- Failed tasks display Failed, but no detailed error message is shown to the user.

## 8. Suggested Future Work

- Replace simulated jobs with real work units.
- Allow users to add, remove, or configure jobs.
- Display a clear error message and retry option for failed jobs.
- Add automated tests for cancellation and UI-state handling.
- Move job model and user-interface code into separate classes.
