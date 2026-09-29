package com.csc360;

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

public class App extends Application {

    private final ExecutorService executor = Executors.newFixedThreadPool(3);
    private final List<Job> jobs = new ArrayList<>();

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

        createJob("Job 1", 100, jobsBox);
        createJob("Job 2", 150, jobsBox);
        createJob("Job 3", 200, jobsBox);

        overallProgressBar = new ProgressBar(0);
        overallProgressBar.setPrefWidth(700);

        overallLabel = new Label("Overall Progress: 0%");
        VBox overallBox = new VBox(8, new Label("Overall Progress"), overallProgressBar, overallLabel);

        startButton = new Button("Start All Jobs");
        startButton.setPrefWidth(170);
        startButton.setOnAction(e -> startAllJobs());

        stopAllButton = new Button("Stop All Jobs");
        stopAllButton.setPrefWidth(170);
        stopAllButton.setDisable(true);
        stopAllButton.setOnAction(e -> stopAllJobs());

        HBox controls = new HBox(14, startButton, stopAllButton);
        controls.setAlignment(Pos.CENTER);

        VBox root = new VBox(24, title, jobsBox, overallBox, controls);
        root.setPadding(new Insets(32));
        root.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(root, 900, 650);
        stage.setMinWidth(850);
        stage.setMinHeight(600);
        stage.setTitle("CSC 360 - Composite Progress Bar");
        stage.setScene(scene);
        stage.show();
    }

    private void createJob(String jobName, int totalWork, VBox jobsBox) {
        Label nameLabel = new Label(jobName);
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
        Job job = new Job(totalWork, progressBar, statusLabel, cancelButton);
        cancelButton.setOnAction(e -> cancelJob(job));

        HBox jobRow = new HBox(12, nameLabel, progressBar, statusLabel, cancelButton);
        jobRow.setAlignment(Pos.CENTER_LEFT);
        jobRow.setMaxWidth(Double.MAX_VALUE);
        jobsBox.getChildren().add(jobRow);
        jobs.add(job);
    }

    private void startAllJobs() {
        if (activeJobs > 0) {
            return;
        }

        overallProgressBar.setProgress(0);
        overallLabel.setText("Overall Progress: 0%");
        activeJobs = jobs.size();
        startButton.setDisable(true);
        stopAllButton.setDisable(false);

        for (Job job : jobs) {
            job.finished = false;
            job.progressBar.progressProperty().unbind();
            job.progressBar.setProgress(0);
            job.statusLabel.setText("Waiting");
            job.cancelButton.setDisable(false);

            Task<Void> task = createTask(job);
            job.task = task;
            job.progressBar.progressProperty().bind(task.progressProperty());
            task.messageProperty().addListener((obs, oldMessage, newMessage) -> job.statusLabel.setText(newMessage));
            task.progressProperty().addListener((obs, oldValue, newValue) -> updateOverallProgress());
            task.setOnSucceeded(e -> finishJob(job, "Completed"));
            task.setOnCancelled(e -> finishJob(job, "Cancelled"));
            task.setOnFailed(e -> finishJob(job, "Failed"));
            executor.submit(task);
        }
    }

    private Task<Void> createTask(Job job) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                updateMessage("Running");

                for (int i = 0; i <= job.totalWork; i++) {
                    if (isCancelled()) {
                        return null;
                    }

                    Thread.sleep(50);
                    updateProgress(i, job.totalWork);
                }

                return null;
            }
        };
    }

    private void cancelJob(Job job) {
        if (job.task != null && !job.task.isDone()) {
            job.task.cancel(true);
        }
    }

    private void stopAllJobs() {
        for (Job job : jobs) {
            cancelJob(job);
        }
    }

    private void finishJob(Job job, String status) {
        if (job.finished) {
            return;
        }

        job.finished = true;
        job.statusLabel.setText(status);
        job.cancelButton.setDisable(true);
        activeJobs--;
        updateOverallProgress();

        if (activeJobs == 0) {
            startButton.setDisable(false);
            stopAllButton.setDisable(true);
        }
    }

    private void updateOverallProgress() {
        double completedWork = 0;
        double totalWork = 0;

        for (Job job : jobs) {
            double progress = job.task == null ? 0 : Math.max(0, job.task.getProgress());
            completedWork += progress * job.totalWork;
            totalWork += job.totalWork;
        }

        double overall = totalWork == 0 ? 0 : completedWork / totalWork;
        overallProgressBar.setProgress(overall);
        overallLabel.setText("Overall Progress: " + Math.round(overall * 100) + "%");
    }

    @Override
    public void stop() {
        executor.shutdownNow();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static final class Job {
        private final int totalWork;
        private final ProgressBar progressBar;
        private final Label statusLabel;
        private final Button cancelButton;
        private Task<Void> task;
        private boolean finished;

        private Job(int totalWork, ProgressBar progressBar, Label statusLabel, Button cancelButton) {
            this.totalWork = totalWork;
            this.progressBar = progressBar;
            this.statusLabel = statusLabel;
            this.cancelButton = cancelButton;
        }
    }
}
