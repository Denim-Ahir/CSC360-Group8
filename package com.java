package com.csc360;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App extends Application {

    private final ExecutorService executor = Executors.newFixedThreadPool(3);

    private final List<Task<Void>> tasks = new ArrayList<>();
    private final List<Integer> totalWorkList = new ArrayList<>();
    private final List<ProgressBar> progressBars = new ArrayList<>();

    private ProgressBar overallProgressBar;
    private Label overallLabel;

    @Override
    public void start(Stage stage) {

        Label title = new Label("Composite Progress Bar");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        VBox jobsBox = new VBox(15);
        jobsBox.setPadding(new Insets(20));

        createJob("Job 1", 100, jobsBox);
        createJob("Job 2", 150, jobsBox);
        createJob("Job 3", 200, jobsBox);

        overallProgressBar = new ProgressBar(0);
        overallProgressBar.setPrefWidth(500);

        overallLabel = new Label("Overall Progress: 0%");

        VBox overallBox = new VBox(8);
        overallBox.getChildren().addAll(
                new Label("Overall Progress"),
                overallProgressBar,
                overallLabel
        );

        Button startButton = new Button("Start All Jobs");
        startButton.setPrefWidth(150);

        startButton.setOnAction(e -> startAllJobs());

        VBox root = new VBox(20);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.TOP_CENTER);

        root.getChildren().addAll(
                title,
                jobsBox,
                overallBox,
                startButton
        );

        Scene scene = new Scene(root, 650, 500);

        stage.setTitle("CSC 360 - Composite Progress Bar");
        stage.setScene(scene);
        stage.show();
    }

    private void createJob(String jobName, int totalWork, VBox jobsBox) {

        Label nameLabel = new Label(jobName);
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(400);

        Label statusLabel = new Label("Waiting");

        Button cancelButton = new Button("Cancel");

        HBox jobRow = new HBox(10);
        jobRow.setAlignment(Pos.CENTER_LEFT);

        jobRow.getChildren().addAll(
                nameLabel,
                progressBar,
                statusLabel,
                cancelButton
        );

        jobsBox.getChildren().add(jobRow);

        Task<Void> task = new Task<>() {

            @Override
            protected Void call() throws Exception {

                for (int i = 0; i <= totalWork; i++) {

                    if (isCancelled()) {
                        updateMessage("Cancelled");
                        return null;
                    }

                    Thread.sleep(50);

                    updateProgress(i, totalWork);
                    updateMessage("Running");
                }

                updateMessage("Completed");

                return null;
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());
        statusLabel.textProperty().bind(task.messageProperty());

        task.setOnSucceeded(e -> {
            statusLabel.setText("Completed");
            updateOverallProgress();
        });

        task.setOnCancelled(e -> {
            statusLabel.setText("Cancelled");
            updateOverallProgress();
        });

        task.setOnFailed(e -> {
            statusLabel.setText("Failed");
            updateOverallProgress();
        });

        task.progressProperty().addListener(
                (obs, oldValue, newValue) -> updateOverallProgress()
        );

        cancelButton.setOnAction(e -> {

            if (!task.isDone()) {
                task.cancel(true);
            }

        });

        tasks.add(task);
        totalWorkList.add(totalWork);
        progressBars.add(progressBar);
    }

    private void startAllJobs() {

        for (Task<Void> task : tasks) {

            if (!task.isDone()) {
                executor.submit(task);
            }

        }
    }

    private void updateOverallProgress() {

        double completedWork = 0;
        double totalWork = 0;

        for (int i = 0; i < tasks.size(); i++) {

            Task<Void> task = tasks.get(i);
            int total = totalWorkList.get(i);

            double progress = task.getProgress();

            if (progress < 0) {
                progress = 0;
            }

            completedWork += progress * total;
            totalWork += total;
        }

        double overall = 0;

        if (totalWork > 0) {
            overall = completedWork / totalWork;
        }

        double finalOverall = overall;

        Platform.runLater(() -> {

            overallProgressBar.setProgress(finalOverall);

            int percentage = (int) Math.round(finalOverall * 100);

            overallLabel.setText(
                    "Overall Progress: " + percentage + "%"
            );
        });
    }

    @Override
    public void stop() {

        executor.shutdownNow();
    }

    public static void main(String[] args) {

        launch(args);
    }
}