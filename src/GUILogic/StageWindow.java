package GUILogic;

import PlannerData.Planner;
import PlannerData.Show;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

class StageWindow {

    private Stage currentStage = new Stage();
    private PlannerData.Stage addedStage;
    private ArrayList<String> errorList = new ArrayList<>();
    private Label information = new Label();
    private PlannerData.Stage selectedStage;
    private ScheduleTab ST;
    private VBox WindowStructure;
    private Button CancelButton;
    private TextField stageNameTextField;
    private Label stageCapacityLabel;
    private TextField inputTextField;
    private Label stageNameLabel;

    private Planner plannerReference;

    /**
     * This is the constructor of the base of the submenus.
     * This method also decides which submenu it should show to the user.
     *
     * @param screenNumber       Submenu selection number
     * @param currentParentStage The current parent stage
     * @param ST                 The schedule tab
     */
    StageWindow(int screenNumber, Stage currentParentStage, ScheduleTab ST) {
        plannerReference = DataController.getInstance().getPlanner();

        this.ST = ST;
        this.currentStage.initOwner(currentParentStage);
        this.currentStage.initModality(Modality.WINDOW_MODAL);
        this.currentStage.setResizable(false);
        this.currentStage.getIcons().add(new Image("logoA5.jpg"));
        this.WindowStructure = new VBox();
        this.CancelButton = new Button("Cancel");
        this.CancelButton.setOnAction(e -> this.currentStage.close());
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
     * This method creates the base for the add and edit stage window
     */
    public void AddEditSetup(){
        this.currentStage.setWidth(200);
        this.currentStage.setHeight(250);
        this.stageNameTextField = new TextField();
        this.stageNameLabel = new Label("Stage Name:");
        this.stageCapacityLabel = new Label("Stage Capacity:");
        this.inputTextField = new TextField();
        this.WindowStructure.getChildren().addAll(stageNameLabel, stageNameTextField, stageCapacityLabel, inputTextField);
    }

    /**
     * This method creates the submenu where the user can add a Stage to the festival.
     * The user must choose a name for the Stage and choose a capacity.
     */
    private void stageAddWindow() {
        AddEditSetup();
        this.currentStage.setTitle("Add Stage");

        HBox cancelConfirmButton = new HBox();
        cancelConfirmButton.getChildren().add(this.CancelButton);
        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {
            if (canAddStage(stageNameTextField, inputTextField)) {
                this.addedStage = new PlannerData.Stage(Integer.parseInt(inputTextField.getText()), stageNameTextField.getText());
                plannerReference.addStage(this.addedStage);
                this.currentStage.close();
            }
        });
        finalizeSetup(cancelConfirmButton,confirmButton);
    }

    /**
     * The submenu window for the editing of a Stage
     */
    private void editStageWindow() {
        this.currentStage.setTitle("Edit Stage");
        Label startEdit = new Label("Which stage do you want to edit?");
        this.WindowStructure.getChildren().add(startEdit);
        ComboBox stageBox = new ComboBox();
        stageBox.getItems().add("Select");
        for (PlannerData.Stage stage : plannerReference.getStages()) {
            stageBox.getItems().add(stage.getName());
        }
        stageBox.getSelectionModel().selectFirst();
        AddEditSetup();

        HBox cancelConfirmHBox = new HBox();
        cancelConfirmHBox.getChildren().add(this.CancelButton);
        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {
            if (!stageBox.getValue().toString().equals("Select")) {
                if (canAddStage(this.stageNameTextField, inputTextField)) {
                    for (Show show : plannerReference.getShows()) {
                        PlannerData.Stage stage = show.getStage();
                        if (stage.getName().equals(this.selectedStage.getName())) {
                            stage.setName(this.stageNameTextField.getText());
                            stage.setCapacity(Integer.parseInt(inputTextField.getText()));
                        }

                        if (show.getExpectedPopularity() > stage.getCapacity()) {
                            show.setExpectedPopularity(stage.getCapacity());
                        }
                    }

                    this.selectedStage.setName(this.stageNameTextField.getText());
                    this.selectedStage.setCapacity(Integer.parseInt(inputTextField.getText()));
                    plannerReference.savePlanner();
                    this.ST.resetData();
                    this.currentStage.close();
                }
            } else {
                this.errorList.clear();
                this.errorList.add("No stage has been Selected");
                new ErrorWindow(this.currentStage, this.errorList);
            }
        });

        stageBox.setOnAction(event -> {
            if (!stageBox.getValue().equals("Select")) {
                this.selectedStage = plannerReference.getStage(stageBox.getValue().toString());
                stageNameTextField.setText(this.selectedStage.getName());
                inputTextField.setText("" + this.selectedStage.getCapacity());
            } else {
                stageNameTextField.setText("");
                inputTextField.setText("");
            }
        });
        finalizeSetup(cancelConfirmHBox,confirmButton);
    }

    /**
     * The submenu window for deleting a stage
     */
    private void deleteStageWindow() {
        HBox startLine = new HBox();
        this.currentStage.setTitle("Delete Stage");
        startLine.getChildren().add(new Label("Choose the stage you want to delete:"));

        ComboBox stageBox = new ComboBox();
        stageBox.getItems().add("Select");
        for (PlannerData.Stage stage : plannerReference.getStages()) {
            stageBox.getItems().add(stage.getName());
        }
        stageBox.getSelectionModel().selectFirst();
        stageBox.setOnAction(event -> {
            if (!stageBox.getValue().equals("Select")) {
                this.selectedStage = plannerReference.getStage(stageBox.getValue().toString());
                if (selectedStage != null && !selectedStage.getName().isEmpty() && selectedStage.getCapacity() > 0) {
                    this.information.textProperty().setValue("Do you want to delete the stage: " + selectedStage.getName() + '\n' + " with the capacity of " + selectedStage.getCapacity());
                }
            } else {
                this.information.textProperty().setValue("         " + '\n');
            }
        });

        startLine.getChildren().add(stageBox);
        this.WindowStructure.getChildren().add(startLine);
        this.WindowStructure.getChildren().add(this.information);

        HBox cancelConfirmButton = new HBox();
        cancelConfirmButton.getChildren().add(this.CancelButton);
        Button confirm = new Button("Confirm");
        confirm.setOnAction(e -> {
            if (!stageBox.getValue().toString().equals("Select")) {
                if (stageDeleteChecker()) {
                    try {
                        plannerReference.deleteStage(stageBox.getValue().toString());
                        plannerReference.savePlanner();
                        this.currentStage.close();
                    } catch (Exception exception) {
                        this.errorList.clear();
                        this.errorList.add("The stage could not be deleted.");
                        new ErrorWindow(this.currentStage, this.errorList);
                    }
                }
            } else {
                this.errorList.clear();
                this.errorList.add("No stage has been selected.");
                new ErrorWindow(this.currentStage, this.errorList);
            }
        });
        finalizeSetup(cancelConfirmButton,confirm);
    }

    /**
     * This method finalizes the setup for all 3 windows and shows the user the window
     *
     * @param choices The Horizontal box in which the buttons reside
     * @param confirm The confirm button
     */
    public void finalizeSetup(HBox choices, Button confirm ){
        choices.getChildren().add(confirm);
        choices.setPadding(new Insets(10));
        choices.setSpacing(20);
        this.WindowStructure.getChildren().add(choices);
        Scene stageDeleteScene = new Scene(this.WindowStructure);
        stageDeleteScene.getStylesheets().add("Window-StyleSheet.css");
        this.currentStage.setScene(stageDeleteScene);
        this.currentStage.show();
    }

    /**
     * This method checks if the new Stage is valid or not.
     * If it isn't valid it will notify the user of the mistakes so the user may repair them before submitting them again.
     */
    private boolean canAddStage(TextField stageName, TextField capacity) {
        this.errorList.clear();

        if (stageName.getText().length() == 0) {
            this.errorList.add("The stage name has not been filled in.");
        } else {
            for (PlannerData.Stage stage : plannerReference.getStages()) {
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

    /**
     * This method checks whether a stage is allowed to be deleted.
     * If its used in a show you aren't allowed to delete a stage, else you are.
     *
     * @return boolean: true if the stage can be deleted, false if not.
     */
    private boolean stageDeleteChecker() {
        this.errorList.clear();
        for (Show show : plannerReference.getShows()) {
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