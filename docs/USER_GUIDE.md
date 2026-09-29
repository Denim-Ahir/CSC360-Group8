# User Guide

## Requirements

- Java Development Kit (JDK) 21
- Maven
- Internet access the first time Maven downloads JavaFX dependencies

## Start the Application

Open a terminal in the project folder and run:

```powershell
mvn javafx:run
```

To build the project without opening the application, run:

```powershell
mvn clean package
```

## Application Window

The application opens at 900 × 650 pixels and cannot be resized below 850 × 600 pixels. It contains:

| Control | Description |
| --- | --- |
| Job progress bar | Displays the current percentage for one job. |
| Status label | Displays Waiting, Running, Completed, Cancelled, or Failed. |
| Cancel | Stops only the job on that row. |
| Overall Progress | Displays weighted progress for all jobs. |
| Start All Jobs | Starts a new run of every job. |
| Stop All Jobs | Cancels every currently active job. |

## Run Jobs

1. Launch the application.
2. Select **Start All Jobs**.
3. Observe the three job bars and the weighted Overall Progress bar advance.
4. Wait for every job to complete, or use cancellation controls as needed.
5. Select **Start All Jobs** again to begin a fresh run.

While jobs are running, Start All Jobs is disabled to prevent duplicate submissions. It becomes available after all jobs complete, are cancelled, or fail.

## Cancel One Job

Select the **Cancel** button beside a running job. That job changes to Cancelled; the remaining jobs continue normally. The overall-progress bar keeps the cancelled job's progress achieved before cancellation.

## Stop All Jobs

Select **Stop All Jobs** while work is running. Each active job receives a cancellation request. When all jobs have reached a terminal state, Stop All Jobs is disabled and Start All Jobs becomes available for a new run.

## Troubleshooting

| Problem | Suggested action |
| --- | --- |
| `mvn` is not recognized | Install Maven and ensure its `bin` folder is in the system PATH. |
| Java version error | Install JDK 21 and make it the active Java version. |
| JavaFX dependencies do not download | Check the internet connection, then run the command again. |
| Start All Jobs is disabled | Wait until every active job completes or is cancelled. |
| Application does not open | Run `mvn clean package` and inspect the Maven error output. |
