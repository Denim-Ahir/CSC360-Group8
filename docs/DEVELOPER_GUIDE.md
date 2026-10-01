# Developer Guide

## Project Layout

```text
CSC360-Group8/
├── src/main/java/com/csc360/Main.java  # Application launcher
├── src/main/java/com/csc360/model/     # Immutable job/progress values
├── src/main/java/com/csc360/service/   # Pure progress calculation
├── src/main/java/com/csc360/ui/        # JavaFX interface
├── src/test/java/com/csc360/           # Unit tests
├── docs/                               # Project documentation
├── pom.xml                             # Maven and JavaFX configuration
└── README.md                           # Repository introduction
```

## Dependencies

The Maven configuration targets Java 21 and uses JavaFX 21.0.12. The `javafx-maven-plugin` runs `com.csc360.Main`. JUnit 5 is available for unit tests.

## Main Components

| Component | Responsibility |
| --- | --- |
| `Main` | Starts the JavaFX application. |
| `CompositeProgressApp` | Creates the stage, manages job rows, controls, and overall progress. |
| `JobDefinition` | Immutable name and work-total configuration for a job. |
| `WorkProgress` | Immutable work total and current-progress value. |
| `ProgressCalculator` | Calculates weighted progress without depending on JavaFX. |
| `ExecutorService` | Runs up to three JavaFX tasks on background threads. |
| `Task<Void>` | Performs simulated work and publishes progress/status updates. |

## Task Lifecycle

1. `CompositeProgressApp.startAllJobs()` prevents a second concurrent run with `activeJobs`.
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
| `createJobRow(...)` | Builds a row for an immutable `JobDefinition`. |
| `startAllJobs()` | Resets state and submits a fresh task per job. |
| `createTask(Job)` | Defines the background simulation and progress updates. |
| `cancelJob(Job)` | Cancels one active job. |
| `stopAllJobs()` | Cancels all active jobs. |
| `finishJob(Job, String)` | Marks a job terminal and updates controls. |
| `ProgressCalculator.calculate(...)` | Computes weighted progress across jobs. |
| `stop()` | Shuts down worker threads when JavaFX exits. |

## Adding a Job

Add another definition to `CompositeProgressApp.JOB_DEFINITIONS`:

```java
new JobDefinition("Job 4", 250)
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
