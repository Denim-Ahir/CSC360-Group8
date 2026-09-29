# Developer Guide

## Project Layout

```text
CSC360-Group8/
├── src/main/java/com/csc360/App.java   # JavaFX application and job model
├── docs/                               # Project documentation
├── pom.xml                             # Maven and JavaFX configuration
└── README.md                           # Repository introduction
```

## Dependencies

The Maven configuration targets Java 21 and uses JavaFX 21.0.12. The `javafx-maven-plugin` runs `com.csc360.App`.

## Main Components

| Component | Responsibility |
| --- | --- |
| `App` | Creates the stage, manages jobs, controls, and overall progress. |
| `Job` | Private model for a job's work amount, controls, task, and terminal state. |
| `ExecutorService` | Runs up to three JavaFX tasks on background threads. |
| `Task<Void>` | Performs simulated work and publishes progress/status updates. |

## Task Lifecycle

1. `startAllJobs()` prevents a second concurrent run with `activeJobs`.
2. It resets each row and builds a **new** JavaFX `Task` for every `Job`.
3. The task progress property is bound to its row's `ProgressBar`.
4. Message and progress listeners update the status and overall progress.
5. Success, cancellation, and failure handlers call `finishJob()`.
6. When `activeJobs` reaches zero, Start All Jobs is enabled and Stop All Jobs is disabled.

Creating a new task on each run is necessary: a JavaFX `Task` is single-use and cannot be submitted again after it has completed or been cancelled.

## Important Methods

| Method | Purpose |
| --- | --- |
| `start(Stage)` | Builds the visual interface and creates the three job rows. |
| `createJob(...)` | Builds a row and stores its `Job` model. |
| `startAllJobs()` | Resets state and submits a fresh task per job. |
| `createTask(Job)` | Defines the background simulation and progress updates. |
| `cancelJob(Job)` | Cancels one active job. |
| `stopAllJobs()` | Cancels all active jobs. |
| `finishJob(Job, String)` | Marks a job terminal and updates controls. |
| `updateOverallProgress()` | Computes weighted progress across jobs. |
| `stop()` | Shuts down worker threads when JavaFX exits. |

## Adding a Job

Add another call in `App.start()`:

```java
createJob("Job 4", 250, jobsBox);
```

The existing logic automatically includes the new job in task execution, cancellation, and weighted progress. The executor currently has three threads; increase the fixed-pool size if more than three jobs should execute at exactly the same time.

## Development Commands

```powershell
# Compile, test, and create the JAR
mvn clean package

# Run the JavaFX application
mvn javafx:run
```

## Code Quality Notes

- UI-control updates run through JavaFX task properties and event handlers.
- Do not perform long-running work directly inside button handlers; use a `Task`.
- Do not reuse a completed `Task`; call `createTask(...)` for every run.
- Keep `stop()` responsible for shutting down executor threads.
