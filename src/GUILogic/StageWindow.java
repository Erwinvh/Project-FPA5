package GUILogic;

import PlannerData.Show;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.ArrayList;

public class StageWindow {

    private Stage upperStage;
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
     * @param upperStage
     */
    public StageWindow(int screenNumber, Stage upperStage) {
        this.upperStage = upperStage;
        this.currentStage.initOwner(this.upperStage);
        this.currentStage.initModality(Modality.WINDOW_MODAL);
        this.currentStage.setResizable(false);

        if (screenNumber == 4) {
            stageAddWindow();
        } else if (screenNumber == 5) {
            stageEditWindow();
        } else if (screenNumber == 6) {
            stageDeleteWindow();
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
        newStageList.getChildren().add(stageNameLabel);

        TextField stageName = new TextField();
        newStageList.getChildren().add(stageName);

        Label stageCapacityLabel = new Label("Stage Capacity:");
        newStageList.getChildren().add(stageCapacityLabel);
        TextField InputTextField = new TextField();
        newStageList.getChildren().add(InputTextField);

        HBox choice = new HBox();
        Button cancelButton = new Button("Cancel");
        choice.getChildren().add(cancelButton);
        cancelButton.setOnAction(e -> this.currentStage.close());

        Button confirm = new Button("Confirm");
        choice.getChildren().add(confirm);
        confirm.setOnAction(e -> {
            if (newStageControl(stageName, InputTextField)){
                this.addedStage = new PlannerData.Stage(Integer.parseInt(InputTextField.getText()), stageName.getText());
                DataController.getPlanner().addStage(this.addedStage);
                this.currentStage.close();
            }
    });

        choice.setPadding(new Insets(10));
        choice.setSpacing(20);

        newStageList.getChildren().add(choice);
        Scene artistAddScene = new Scene(newStageList);
        artistAddScene.getStylesheets().add("Window-StyleSheet.css");
        this.currentStage.setScene(artistAddScene);
        this.currentStage.show();
    }

    public void stageEditWindow() {
        this.currentStage.setWidth(200);
        this.currentStage.setHeight(250);
        VBox newStageList = new VBox();
        Label startEdit = new Label("Which stage do you want to edit?");
        newStageList.getChildren().add(startEdit);

        ComboBox stageBox = new ComboBox();
        stageBox.getItems().add("--Select--");
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
        TextField InputTextField = new TextField();
        newStageList.getChildren().add(InputTextField);

        HBox choice = new HBox();
        Button cancelButton = new Button("Cancel");
        choice.getChildren().add(cancelButton);
        cancelButton.setOnAction(e -> this.currentStage.close());

        Button confirm = new Button("Confirm");
        choice.getChildren().add(confirm);
        confirm.setOnAction(e -> {
            if (newStageControl(stageName, InputTextField)){
                this.selectedStage.setName(stageName.getText());
                this.selectedStage.setCapacity(Integer.parseInt(InputTextField.getText()));
                DataController.getPlanner().savePlanner();
                this.currentStage.close();
            }
        });

        choice.setPadding(new Insets(10));
        choice.setSpacing(20);

        stageBox.setOnAction(event -> {
            if (!stageBox.getValue().equals("--Select--")) {
                this.selectedStage = stringToStage(stageBox.getValue().toString());
                stageName.setText(this.selectedStage.getName());
                InputTextField.setText("" + this.selectedStage.getCapacity());
            }
            else{
                stageName.setText("");
                InputTextField.setText("");
            }
        });

        newStageList.getChildren().add(choice);
        Scene artistAddScene = new Scene(newStageList);
        artistAddScene.getStylesheets().add("Window-StyleSheet.css");
        this.currentStage.setScene(artistAddScene);
        this.currentStage.show();
        this.currentStage.show();
    }

    public void stageDeleteWindow() {
        BorderPane structure = new BorderPane();
        HBox startLine = new HBox();
        startLine.getChildren().add(new Label("Choose the stage you want to delete:"));
        ComboBox stageBox = new ComboBox();
        stageBox.getItems().add("--Select--");
        for (PlannerData.Stage stage : DataController.getPlanner().getStages()) {
            stageBox.getItems().add(stage.getName());
        }
        stageBox.getSelectionModel().selectFirst();
        stageBox.setOnAction(event -> {
            if (!stageBox.getValue().equals("--Select--")) {
                   this.selectedStage = stringToStage(stageBox.getValue().toString());
                if (selectedStage != null && !selectedStage.getName().isEmpty() && selectedStage.getCapacity() > 0) {
                    this.information.textProperty().setValue("Do you want to delete the stage: " + selectedStage.getName() + '\n' + " with the capacity of " + selectedStage.getCapacity());
                }
            }
            else{
                this.information.textProperty().setValue("         "+'\n');
                //something here?
            }
        });

        startLine.getChildren().add(stageBox);
        structure.setTop(startLine);
        structure.setCenter(this.information);

        HBox choice = new HBox();
        Button cancelButton = new Button("Cancel");
        choice.getChildren().add(cancelButton);
        cancelButton.setOnAction(e -> this.currentStage.close());

        Button confirm = new Button("Confirm");
        choice.getChildren().add(confirm);
        confirm.setOnAction(e -> {
            if (StageDeleteChecker()){
                try{
                    DataController.getPlanner().deleteStage(stageBox.getValue().toString());
                    DataController.getPlanner().savePlanner();
                    this.currentStage.close();
                }
                catch(Exception exception){
                    this.errorList.clear();
                    this.errorList.add("The Stage could not be deleted.");
                    new ErrorWindow(this.currentStage, this.errorList);
                }
            }

        });

        choice.setPadding(new Insets(10));
        choice.setSpacing(20);
        structure.setBottom(choice);
        Scene stageDeleteScene = new Scene(structure);
        stageDeleteScene.getStylesheets().add("Window-StyleSheet.css");
        this.currentStage.setScene(stageDeleteScene);

        this.currentStage.show();
    }

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
    public Boolean newStageControl(TextField stageName, TextField capacity) {
        this.errorList.clear();

        if (stageName.getText().length() == 0) {
            this.errorList.add("The stage name has not been filled in.");
        }
        else{
            for (PlannerData.Stage stage : DataController.getPlanner().getStages()) {
                if (stageName.getText().equals(stage.getName())){
                    this.errorList.add("This Stage already exists.");
                }
            }
        }

        if (capacity.getText().length() == 0) {
            this.errorList.add("The capacity has not been filled in.");
        } else {
            try {
                int capacityCheck = Integer.parseInt(capacity.getText());
                if (capacityCheck < 20) {
                    this.errorList.add("The capacity must be at least 20");
                }

            } catch (Exception e) {
                this.errorList.add("The capacity must be a number.");
            }
        }

        if (this.errorList.isEmpty()) {
            return true;
        } else {
            new ErrorWindow(this.currentStage, this.errorList);
            return false;
        }
    }

    public Boolean StageDeleteChecker(){
        this.errorList.clear();
        for (Show show : DataController.getPlanner().getShows()) {
            if (show.getStage().getName().equals(this.selectedStage.getName())){
                this.errorList.add("Stages in use cannot be removed from the event.");
            }
        }
        if (this.errorList.isEmpty()){
            return true;
        }
        else{
            new ErrorWindow(this.currentStage,this.errorList);
            return false;
        }
    }
}