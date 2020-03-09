package GUILogic;

import PlannerData.Show;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class StageWindow {

    private Stage currentStage = new Stage();
    private PlannerData.Stage addedStage;
    private ArrayList<String> errorList = new ArrayList<>();
    private Label information = new Label();
    private PlannerData.Stage selectedStage;

    /**
     * This is the constructor of the base of the submenus.
     * This method also decides which submenu it should show to the user.
     *
     * @param screenNumber
     * @param currentParentStage
     */
    public StageWindow(int screenNumber, Stage currentParentStage) {
        this.currentStage.initOwner(currentParentStage);
        this.currentStage.initModality(Modality.WINDOW_MODAL);
        this.currentStage.setResizable(false);

        switch (screenNumber) {
            case 4:
                stageAddWindow();
                break;
            case 5:
                editStageWindow();
                break;
            case 6:
                deleteStageWindow();
                break;
        }
    }

    /**
     * This method creates the submenu where the user can add a Stage to the festival.
     * The user must choose a name for the Stage and choose a capacity.
     */
    public void stageAddWindow() {
        this.currentStage.setWidth(200);
        this.currentStage.setHeight(250);

        VBox newStageList = new VBox();

        Label stageNameLabel = new Label("Stage Name:");

        TextField stageNameTextField = new TextField();

        Label stageCapacityLabel = new Label("Stage Capacity:");
        TextField inputTextField = new TextField();

        HBox cancelConfirmButton = new HBox();
        Button cancelButton = new Button("Cancel");
        cancelConfirmButton.getChildren().add(cancelButton);
        cancelButton.setOnAction(e -> this.currentStage.close());

        newStageList.getChildren().addAll(stageNameLabel, stageNameTextField, stageCapacityLabel, inputTextField);

        Button confirmButton = new Button("Confirm");
        cancelConfirmButton.getChildren().add(confirmButton);
        confirmButton.setOnAction(e -> {
            if (canAddStage(stageNameTextField, inputTextField)) {
                this.addedStage = new PlannerData.Stage(Integer.parseInt(inputTextField.getText()), stageNameTextField.getText());
                DataController.getPlanner().addStage(this.addedStage);
                this.currentStage.close();
            }
        });

        cancelConfirmButton.setPadding(new Insets(10));
        cancelConfirmButton.setSpacing(20);

        newStageList.getChildren().add(cancelConfirmButton);

        Scene artistAddScene = new Scene(newStageList);
        artistAddScene.getStylesheets().add("Window-StyleSheet.css");
        this.currentStage.setScene(artistAddScene);
        this.currentStage.show();
    }

    public void editStageWindow() {
        this.currentStage.setWidth(200);
        this.currentStage.setHeight(250);

        VBox newStageList = new VBox();
        Label startEdit = new Label("Which stage do you want to edit?");
        newStageList.getChildren().add(startEdit);

        ComboBox stageBox = new ComboBox();
        stageBox.getItems().add("Select");
        for (PlannerData.Stage stage : DataController.getPlanner().getStages()) {
            stageBox.getItems().add(stage.getName());
        }

        stageBox.getSelectionModel().selectFirst();
        newStageList.getChildren().add(stageBox);

        Label stageNameLabel = new Label("Stage Name:");
        newStageList.getChildren().add(stageNameLabel);

        TextField stageName = new TextField();
        newStageList.getChildren().add(stageName);

        Label stageCapacityLabel = new Label("Stage Capacity:");
        newStageList.getChildren().add(stageCapacityLabel);
        TextField inputTextField = new TextField();
        newStageList.getChildren().add(inputTextField);

        newStageList.getChildren().addAll(stageBox, stageNameLabel, stageName, stageCapacityLabel, inputTextField);

        HBox cancelConfirmHBox = new HBox();
        Button cancelButton = new Button("Cancel");
        cancelConfirmHBox.getChildren().add(cancelButton);
        cancelButton.setOnAction(e -> this.currentStage.close());

        Button confirmButton = new Button("Confirm");
        cancelConfirmHBox.getChildren().add(confirmButton);
        confirmButton.setOnAction(e -> {
            if (canAddStage(stageName, inputTextField)) {
                for (Show show : DataController.getPlanner().getShows()) {
                    PlannerData.Stage stage = show.getStage();
                    if (stage.getName().equals(this.selectedStage.getName())) {
                        stage.setName(stageName.getText());
                        stage.setCapacity(Integer.parseInt(inputTextField.getText()));
                    }

                    if (show.getExpectedPopularity() > stage.getCapacity()) {
                        show.setExpectedPopularity(stage.getCapacity());
                    }
                }

                this.selectedStage.setName(stageName.getText());
                this.selectedStage.setCapacity(Integer.parseInt(inputTextField.getText()));
                DataController.getPlanner().savePlanner();
                this.currentStage.close();
            }
        });

        cancelConfirmHBox.setPadding(new Insets(10));
        cancelConfirmHBox.setSpacing(20);

        stageBox.setOnAction(event -> {
            if (!stageBox.getValue().equals("Select")) {
                this.selectedStage = stringToStage(stageBox.getValue().toString());
                stageName.setText(this.selectedStage.getName());
                inputTextField.setText("" + this.selectedStage.getCapacity());
            } else {
                stageName.setText("");
                inputTextField.setText("");
            }
        });

        newStageList.getChildren().add(cancelConfirmHBox);

        Scene artistAddScene = new Scene(newStageList);
        artistAddScene.getStylesheets().add("Window-StyleSheet.css");
        this.currentStage.setScene(artistAddScene);
        this.currentStage.show();
        this.currentStage.show();
    }

    public void deleteStageWindow() {
        BorderPane borderPane = new BorderPane();
        HBox startLine = new HBox();
        startLine.getChildren().add(new Label("Choose the stage you want to delete:"));
        ComboBox stageBox = new ComboBox();
        stageBox.getItems().add("Select");
        for (PlannerData.Stage stage : DataController.getPlanner().getStages()) {
            stageBox.getItems().add(stage.getName());
        }

        stageBox.getSelectionModel().selectFirst();
        stageBox.setOnAction(event -> {
            if (!stageBox.getValue().equals("Select")) {
                this.selectedStage = stringToStage(stageBox.getValue().toString());
                if (selectedStage != null && !selectedStage.getName().isEmpty() && selectedStage.getCapacity() > 0) {
                    this.information.textProperty().setValue("Do you want to delete the stage: " + selectedStage.getName() + '\n' + " with the capacity of " + selectedStage.getCapacity());
                }
            } else {
                this.information.textProperty().setValue("         " + '\n');
                //something here?
            }
        });

        startLine.getChildren().add(stageBox);
        borderPane.setTop(startLine);
        borderPane.setCenter(this.information);

        HBox cancelConfirmButton = new HBox();
        Button cancelButton = new Button("Cancel");
        cancelConfirmButton.getChildren().add(cancelButton);
        cancelButton.setOnAction(e -> this.currentStage.close());

        Button confirm = new Button("Confirm");
        cancelConfirmButton.getChildren().add(confirm);
        confirm.setOnAction(e -> {
            if (stageDeleteChecker()) {
                try {
                    DataController.getPlanner().deleteStage(stageBox.getValue().toString());
                    DataController.getPlanner().savePlanner();
                    this.currentStage.close();

                } catch (Exception exception) {
                    this.errorList.clear();
                    this.errorList.add("The stage could not be deleted.");
                    new ErrorWindow(this.currentStage, this.errorList);
                }
            }
        });

        cancelConfirmButton.setPadding(new Insets(10));
        cancelConfirmButton.setSpacing(20);
        borderPane.setBottom(cancelConfirmButton);

        Scene stageDeleteScene = new Scene(borderPane);
        stageDeleteScene.getStylesheets().add("Window-StyleSheet.css");
        this.currentStage.setScene(stageDeleteScene);

        this.currentStage.show();
    }

    // TODO: Move logic to stage class
    public PlannerData.Stage stringToStage(String stageString) {
        if (stageString == null || stageString.isEmpty()) {
            return null;
        }
        for (PlannerData.Stage stage : DataController.getPlanner().getStages()) {
            if (stageString.equals(stage.getName())) {
                return stage;
            }
        }
        return null;
    }

    /**
     * This method checks if the new Stage is valid or not.
     * If it isn't valid it will notify the user of the mistakes so the user may repair them before submitting them again.
     *
     * @param stageName
     * @param capacity
     */
    public boolean canAddStage(TextField stageName, TextField capacity) {
        this.errorList.clear();

        if (stageName.getText().length() == 0) {
            this.errorList.add("The stage name has not been filled in.");
        } else {
            for (PlannerData.Stage stage : DataController.getPlanner().getStages()) {
                if (this.selectedStage != null) {
                    if ((!this.selectedStage.equals(stage)) && stageName.getText().equals(stage.getName())) {
                        this.errorList.add("This Stage already exists.");
                    }
                } else {
                    if (stageName.getText().equals(stage.getName())) {
                        this.errorList.add("This Stage already exists.");
                    }
                }
            }
        }

        if (capacity.getText().length() == 0) {
            this.errorList.add("The capacity has not been filled in.");
        } else {
            try {
                int capacityCheck = Integer.parseInt(capacity.getText());
                if (capacityCheck < 20) {
                    this.errorList.add("The capacity must be at least 20.");
                } else if (capacityCheck > 10000) {
                    this.errorList.add("The capacity cannot be larger than 10.000.");
                }

            } catch (Exception e) {
                this.errorList.add("The capacity must be a number.");
            }
        }

        if (this.errorList.isEmpty()) {
            return true;
        }

        new ErrorWindow(this.currentStage, this.errorList);
        return false;
    }

    public boolean stageDeleteChecker() {
        this.errorList.clear();
        for (Show show : DataController.getPlanner().getShows()) {
            if (show.getStage().getName().equals(this.selectedStage.getName())) {
                this.errorList.add("Stages in use cannot be removed from the event.");
            }
        }

        if (this.errorList.isEmpty()) {
            return true;
        }

        new ErrorWindow(this.currentStage, this.errorList);
        return false;
    }
}