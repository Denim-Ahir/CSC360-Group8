# Test Plan

## Purpose

This plan checks the primary functions of the Composite Progress Bar prototype. The tests can be performed manually after starting the application with `mvn javafx:run`.

## Test Cases

| ID | Scenario | Steps | Expected result |
| --- | --- | --- | --- |
| TC-01 | Launch application | Run `mvn javafx:run`. | Window opens with three job rows, an overall bar, Start All Jobs, and Stop All Jobs. |
| TC-02 | Start jobs | Select Start All Jobs. | Three jobs show Running and progress moves without freezing the window. |
| TC-03 | Complete jobs | Start jobs and wait until all finish. | All rows show Completed, overall progress reaches 100%, and Start All Jobs is enabled again. |
| TC-04 | Restart after completion | Complete TC-03, then select Start All Jobs again. | All bars reset and a fresh set of tasks runs. |
| TC-05 | Cancel one job | Start jobs, then select Cancel for Job 2. | Job 2 shows Cancelled; Jobs 1 and 3 continue. |
| TC-06 | Stop all jobs | Start jobs, then select Stop All Jobs. | Each active job becomes Cancelled and Start All Jobs becomes available once all are terminal. |
| TC-07 | Restart after stopping | Complete TC-06, then select Start All Jobs. | All jobs start again from zero. |
| TC-08 | Weighted progress | Start jobs and compare bars while Job 3 is farther behind. | Overall progress reflects work weights 100, 150, and 200, not a simple average. |
| TC-09 | Cancel-button layout | Open the app at the minimum window size. | Every Cancel button displays its complete label and remains usable. |
| TC-10 | Close application | Start jobs, then close the window. | The application exits without leaving its worker executor running. |

## Build Verification

Run the following before manual testing:

```powershell
mvn clean package
```

Expected result: Maven reports `BUILD SUCCESS`.

## Automated Testing Roadmap

Future automated tests should cover:

- The weighted-progress formula with known inputs.
- State transitions for completed, cancelled, and failed jobs.
- The prevention of duplicate concurrent starts.
- Re-enabling controls after the final job reaches a terminal state.
