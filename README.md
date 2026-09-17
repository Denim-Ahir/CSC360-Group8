# CSC 360 - Group 8

## Project: Composite Progress Bar

A JavaFX application for tracking multiple background jobs at the same time.

Each job has its own progress bar, status, and cancellation button. The application also shows a composite progress bar representing the overall progress of all jobs.

## Team Members

- Denim Ahir
- Kishankumar Naik
- Devansh

## Project Objective

The main objective of this project is to build a JavaFX application that can run multiple jobs concurrently while showing their individual progress and the overall progress.

The application should also allow the user to cancel an individual job without stopping the other jobs.

## Technologies Used

- Java 21
- JavaFX
- Maven
- JavaFX Task
- ExecutorService
- Git and GitHub

## Current Prototype

The current version is an initial working prototype of the Composite Progress Bar application.

At this stage, the prototype includes:

- JavaFX graphical interface
- Three simulated jobs
- Individual progress bars
- Individual Cancel buttons
- Job status display
- Background task execution
- Composite/overall progress bar
- Initial progress calculation

### Current Output

[View current prototype output](./Screenshot%202026-09-17%20153324.png)

> This is an early-stage prototype and is still under development. More features and improvements will be added in the upcoming stages.

## Planned Improvements

- Implement proper individual file/job progress tracking
- Add Cancel All Jobs
- Improve individual job cancellation
- Improve job status handling
- Add error and exception handling
- Improve the composite progress calculation
- Improve the user interface
- Add testing
- Separate the project into proper classes and modules
- Final integration and implementation

## Project Structure

```text
composite-progress-bar
│
├── src
│   └── main
│       └── java
│           └── com
│               └── csc360
│                   └── App.java
│
├── pom.xml
├── .gitignore
└── README.md
