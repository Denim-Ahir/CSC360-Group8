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

The first working prototype has been completed.

It currently includes:

- JavaFX graphical interface
- Three simulated jobs
- Individual progress bars for each job
- Individual Cancel buttons
- Job status display
- Concurrent background execution
- Composite/overall progress bar
- Weighted overall progress calculation
- Cooperative task cancellation

## How It Works

The basic flow of the application is:

```text
Start All Jobs
       ↓
Create Background Tasks
       ↓
Run Tasks Using ExecutorService
       ↓
Each Job Updates Its Progress
       ↓
Individual Progress Bars Update
       ↓
Overall Progress Is Calculated
       ↓
Job Completes / Gets Cancelled
