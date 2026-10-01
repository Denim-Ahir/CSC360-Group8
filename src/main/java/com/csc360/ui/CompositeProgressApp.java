package com.csc360.ui;

import com.csc360.model.JobDefinition;
import com.csc360.model.WorkProgress;
import com.csc360.service.ProgressCalculator;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** JavaFX user interface for the Composite Progress Bar. */
public final class CompositeProgressApp extends Application {

    private static final List<JobDefinition> JOB_DEFINITIONS = List.of(
            new JobDefinition("Job 1", 100),
            new JobDefinition("Job 2", 150),
            new JobDefinition("Job 3", 200)
    );

    private final ExecutorService executor = Executors.newFixedThreadPool(JOB_DEFINITIONS.size());
    private final List<JobRow> jobRows = new ArrayList<>();

    private ProgressBar overallProgressBar;
    private Label overallLabel;
    private Button startButton;
    private Button stopAllButton;
    private int activeJobs;

    @Override
    public void start(Stage stage) {
        Label title = new Label("Composite Progress Bar");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        VBox jobsBox = new VBox(18);
        jobsBox.setPadding(new Insets(24));
        JOB_DEFINITIONS.forEach(definition -> createJobRow(definition, jobsBox));

        overallProgressBar = new ProgressBar(0);
        overallProgressBar.setPrefWidth(700);
        overallLabel = new Label("Overall Progress: 0%");
        VBox overallBox = new VBox(8, new Label("Overall Progress"), overallProgressBar, overallLabel);

        startButton = new Button("Start All Jobs");
        startButton.setPrefWidth(170);
        startButton.setOnAction(event -> startAllJobs());

        stopAllButton = new Button("Stop All Jobs");
        stopAllButton.setPrefWidth(170);
        stopAllButton.setDisable(true);
        stopAllButton.setOnAction(event -> stopAllJobs());

        HBox controls = new HBox(14, startButton, stopAllButton);
        controls.setAlignment(Pos.CENTER);

        VBox root = new VBox(24, title, jobsBox, overallBox, controls);
        root.setPadding(new Insets(32));
        root.setAlignment(Pos.TOP_CENTER);

        stage.setTitle("CSC 360 - Composite Progress Bar");
        stage.setMinWidth(850);
        stage.setMinHeight(600);
        stage.setScene(new Scene(root, 900, 650));
        stage.show();
    }

    private void createJobRow(JobDefinition definition, VBox jobsBox) {
        Label nameLabel = new Label(definition.name());
        nameLabel.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;");
        nameLabel.setPrefWidth(70);

        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setMinWidth(250);
        progressBar.setPrefWidth(500);
        HBox.setHgrow(progressBar, Priority.ALWAYS);

        Label statusLabel = new Label("Waiting");
        statusLabel.setPrefWidth(75);

        Button cancelButton = new Button("Cancel");
        cancelButton.setMinWidth(90);
        cancelButton.setPrefWidth(90);
        cancelButton.setWrapText(false);

        JobRow row = new JobRow(definition, progressBar, statusLabel, cancelButton);
        cancelButton.setOnAction(event -> cancelJob(row));

        HBox jobLayout = new HBox(12, nameLabel, progressBar, statusLabel, cancelButton);
        jobLayout.setAlignment(Pos.CENTER_LEFT);
        jobLayout.setMaxWidth(Double.MAX_VALUE);
        jobsBox.getChildren().add(jobLayout);
        jobRows.add(row);
    }

    private void startAllJobs() {
        if (activeJobs > 0) {
            return;
        }

        overallProgressBar.setProgress(0);
        overallLabel.setText("Overall Progress: 0%");
        activeJobs = jobRows.size();
        startButton.setDisable(true);
        stopAllButton.setDisable(false);

        for (JobRow row : jobRows) {
            resetRow(row);
            Task<Void> task = createTask(row);
            row.task = task;
            row.progressBar.progressProperty().bind(task.progressProperty());
            task.messageProperty().addListener((observable, oldMessage, message) -> row.statusLabel.setText(message));
            task.progressProperty().addListener((observable, oldProgress, progress) -> updateOverallProgress());
            task.setOnSucceeded(event -> finishJob(row, "Completed"));
            task.setOnCancelled(event -> finishJob(row, "Cancelled"));
            task.setOnFailed(event -> finishJob(row, "Failed"));
            executor.submit(task);
        }
    }

    private void resetRow(JobRow row) {
        row.finished = false;
        row.progressBar.progressProperty().unbind();
        row.progressBar.setProgress(0);
        row.statusLabel.setText("Waiting");
        row.cancelButton.setDisable(false);
    }

    private Task<Void> createTask(JobRow row) {
        return new Task<>() {
            @Override
            protected Void call() throws InterruptedException {
                updateMessage("Running");

                for (int completed = 0; completed <= row.definition.totalWork(); completed++) {
                    if (isCancelled()) {
                        return null;
                    }

                    Thread.sleep(50);
                    updateProgress(completed, row.definition.totalWork());
                }
                return null;
            }
        };
    }

    private void cancelJob(JobRow row) {
        if (row.task != null && !row.task.isDone()) {
            row.task.cancel(true);
        }
    }

    private void stopAllJobs() {
        jobRows.forEach(this::cancelJob);
    }

    private void finishJob(JobRow row, String status) {
        if (row.finished) {
            return;
        }

        row.finished = true;
        row.statusLabel.setText(status);
        row.cancelButton.setDisable(true);
        activeJobs--;
        updateOverallProgress();

        if (activeJobs == 0) {
            startButton.setDisable(false);
            stopAllButton.setDisable(true);
        }
    }

    private void updateOverallProgress() {
        List<WorkProgress> progressValues = jobRows.stream()
                .map(row -> new WorkProgress(
                        row.definition.totalWork(),
                        row.task == null ? 0 : row.task.getProgress()))
                .toList();

        double overall = ProgressCalculator.calculate(progressValues);
        overallProgressBar.setProgress(overall);
        overallLabel.setText("Overall Progress: " + Math.round(overall * 100) + "%");
    }

    @Override
    public void stop() {
        executor.shutdownNow();
    }

    private static final class JobRow {
        private final JobDefinition definition;
        private final ProgressBar progressBar;
        private final Label statusLabel;
        private final Button cancelButton;
        private Task<Void> task;
        private boolean finished;

        private JobRow(JobDefinition definition, ProgressBar progressBar, Label statusLabel, Button cancelButton) {
            this.definition = definition;
            this.progressBar = progressBar;
            this.statusLabel = statusLabel;
            this.cancelButton = cancelButton;
        }
    }
}
