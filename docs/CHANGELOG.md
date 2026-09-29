# Change Log

## Current Prototype

- Added individual progress bars, status labels, and cancellation controls for three jobs.
- Added a weighted composite progress bar using work totals of 100, 150, and 200.
- Added Start All Jobs to begin every job concurrently.
- Added Stop All Jobs to cancel every active job.
- Made the application restartable by creating fresh JavaFX tasks for each run.
- Prevented duplicate starts while a run is active.
- Enlarged the default application window to 900 × 650 pixels.
- Reserved sufficient width for Cancel buttons and made progress bars adapt to available row width.
