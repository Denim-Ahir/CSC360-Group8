# Composite Progress Bar

<p align="center">
  A JavaFX desktop application that runs several background jobs and reports their individual and weighted overall progress.
</p>

<p align="center">
  <a href="#quick-start">Quick start</a> ·
  <a href="#using-the-app">Using the app</a> ·
  <a href="#design">Design</a> ·
  <a href="#project-layout">Project layout</a> ·
  <a href="docs/README.md">Documentation</a>
</p>

---

<p align="center">
  <img src="Images/Screenshot%202026-09-29%20151136.png" alt="Composite Progress Bar before starting jobs" width="85%">
</p>

The application keeps the window responsive while three simulated jobs run in parallel. Each job can be cancelled individually, or all active jobs can be stopped together. When a run ends, the application is ready to start a fresh set of tasks.

## Quick start

### Requirements

- JDK 21
- Apache Maven 3.8 or newer

### Commands

| Task | Command |
| --- | --- |
| Run the application | `mvn javafx:run` |
| Build and run tests | `mvn clean package` |
| Run tests only | `mvn test` |

## Using the app

| Control | Action |
| --- | --- |
| **Start All Jobs** | Creates and starts a fresh task for every job. |
| **Cancel** | Cancels only the job on the selected row. |
| **Stop All Jobs** | Requests cancellation for every active job. |
| **Overall Progress** | Shows weighted progress across every job. |

<p align="center">
  <img src="Images/Screenshot%202026-09-29%20151156.png" alt="Composite Progress Bar while jobs are running" width="85%">
</p>

The controls prevent duplicate runs: Start All Jobs is disabled while work is active, and it becomes available after every job has completed, failed, or been cancelled.

<p align="center">
  <img src="Images/Screenshot%202026-09-29%20151148.png" alt="Composite Progress Bar after jobs are cancelled" width="85%">
</p>

## Features

- Three concurrent, simulated background jobs.
- Individual progress bars and clear task-status labels.
- Individual job cancellation and Stop All Jobs.
- Restartable tasks after completion or cancellation.
- Weighted composite progress instead of a simple average.
- Responsive JavaFX interface with an enlarged, resizable window.
- Unit tests for the pure weighted-progress calculation.

## Design

The user-interface thread draws controls and handles button clicks. Work is performed in JavaFX `Task` objects on an `ExecutorService` worker pool, so long-running work does not freeze the window.

```text
Start All Jobs
      |
      v
CompositeProgressApp ---- creates ----> JavaFX Task per job
      |                                      |
      |                                      v
      |                            ExecutorService workers
      |                                      |
      v                                      v
ProgressCalculator <---- current task progress and work totals
      |
      v
Overall Progress bar and percentage label
```

The job totals are 100, 150, and 200. Overall progress is weighted using:

```text
sum(job progress × job total work) / sum(job total work)
```

For example, Job 3 has twice the influence of Job 1 because it has twice the total work.

## Project layout

```text
CSC360-Group8/
├── Images/                                  # Application screenshots
├── docs/                                    # Project, user, developer, and test documentation
├── pom.xml                                  # Maven configuration
├── README.md                                # Project entry point and demo
└── src/
    ├── main/java/com/csc360/
    │   ├── Main.java                        # JavaFX launcher
    │   ├── model/
    │   │   ├── JobDefinition.java           # Job name and work-total configuration
    │   │   └── WorkProgress.java            # Work amount plus current progress value
    │   ├── service/
    │   │   └── ProgressCalculator.java      # Weighted-progress calculation
    │   └── ui/
    │       └── CompositeProgressApp.java    # JavaFX layout and task lifecycle
    └── test/java/com/csc360/service/
        └── ProgressCalculatorTest.java      # Weighted-progress unit tests
```

## Documentation

The expanded local documentation is in [docs/README.md](docs/README.md):

- [Project Overview](docs/PROJECT_OVERVIEW.md)
- [User Guide](docs/USER_GUIDE.md)
- [Developer Guide](docs/DEVELOPER_GUIDE.md)
- [Test Plan](docs/TEST_PLAN.md)
- [Change Log](docs/CHANGELOG.md)

## Team

**CSC 360 · Group 8**

- Denim Ahir
- Kishankumar Naik
- Devansh Maheshwari

## Current limitations

- The jobs are simulated with a short delay; they do not process real files or network requests.
- Job definitions are currently configured in code.
- UI behaviour is manually tested; only the progress-calculation service has automated tests.
