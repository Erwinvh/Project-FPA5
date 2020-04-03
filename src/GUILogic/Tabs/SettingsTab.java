package GUILogic.Tabs;

import GUILogic.DataController;
import GUILogic.GUI;
import GUILogic.Settings;
import GUILogic.Tabs.Windows.ShowWindow;
import PlannerData.Planner;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.ArrayList;

public class SettingsTab {

    private Stage primaryStage;
    private Tab settingsTab;
    private String saveFileName;
    private Slider speedSlider;
    private Slider NPCAmountSlider;
    private CheckBox prediction;
    private ComboBox beginTime;
    private CheckBox overwriteStartTime;

    private Settings settingsReference;

    /**
     * The constructor of the settings tab
     *
     * @param primaryStage the window of the main application
     */
    public SettingsTab(Stage primaryStage) {
        settingsReference = DataController.getInstance().getSettings();

        this.primaryStage = primaryStage;
        this.settingsTab = new Tab("Settings");
        this.saveFileName = settingsReference.getSaveFileName();

        this.speedSlider = new Slider();
        speedSlider.setValue(settingsReference.getSimulatorSpeed());

        this.NPCAmountSlider = new Slider();
        NPCAmountSlider.setValue(settingsReference.getVisitors());

        this.prediction = new CheckBox();
        prediction.setSelected(settingsReference.isUsingPredictedPerson());

        this.beginTime = new ComboBox();
        beginTime.setValue(DataController.getInstance().getSettings().getBeginHours());
        this.beginTime = new ComboBox();
        beginTime.setValue(settingsReference.getBeginHours());
        this.overwriteStartTime = new CheckBox();
        overwriteStartTime.setText("Use this starting time");
        ArrayList timeList = ShowWindow.setupTimeList();
        beginTime = ShowWindow.getTimestampsComboBox(0, timeList);
    }

    /**
     * builds and returns the settingsTab
     *
     * @return the settings tab
     */
    public Tab getSettingsTab() {

        GridPane gridPane = new GridPane();

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(100);
        gridPane.setVgap(20);

        Label plannerSettings = new Label("Planner settings");
        plannerSettings.setStyle("-fx-font-weight: bold;");
        Label simulatorSettings = new Label("Simulator settings");
        simulatorSettings.setStyle("-fx-font-weight: bold;");

        Label simulatorSpeed = new Label("Simulator speed");
        Label NPCAmount = new Label("Amount of visitors");

        Button deleteAllButton = new Button("Delete all");
        deleteAllButton.setOnAction(e -> deleteData(false));

        Button deleteShowsButton = new Button("Delete all shows");
        deleteShowsButton.setOnAction(e -> deleteData(true));

        //Prediction checkbox
        prediction.setText("Predicted types of guests");
        prediction.setTooltip(new Tooltip("This feature creates a more realistic view \n of the type of visitor the festival will attract.\n When it is turned off the simulator will spawn\n random types of visitors. \n If on it will spawn the predicted visitors."));
        //Simulator speed slider
        speedSlider.setMax(2);
        speedSlider.setMin(0);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(1);
        speedSlider.setBlockIncrement(0.1);
        Label speedLabel = new Label("");
        DecimalFormat format = new DecimalFormat("0.0");
        speedLabel.setText(format.format(settingsReference.getSimulatorSpeed() * 100) + "%");

        speedSlider.valueProperty().addListener((observableValue, oldValue, newValue) ->
                speedLabel.textProperty().setValue(format.format(newValue.floatValue() * 100) + "%")
        );

        //NPC amount slider
        NPCAmountSlider.setMin(1);
        NPCAmountSlider.setMax(300);
        NPCAmountSlider.setShowTickLabels(true);
        NPCAmountSlider.setShowTickMarks(true);
        NPCAmountSlider.setMajorTickUnit(50);
        NPCAmountSlider.setMinorTickCount(10);
        NPCAmountSlider.setBlockIncrement(10);

        Label amountLabel = new Label("");
        amountLabel.setText(settingsReference.getVisitors() + "");
        NPCAmountSlider.valueProperty().addListener((observableValue, oldValue, newValue) ->
                amountLabel.textProperty().setValue(String.valueOf(newValue.intValue()))
        );

        //Hour ComboBox
        Label timeLabel = new Label("Begin time");

        beginTime.getItems().remove("Select");
        if (settingsReference.getBeginHours() >= 0) {
            String time;
            if (settingsReference.getBeginHours() <= 9) {
                time = "0" + settingsReference.getBeginHours();
            } else {
                time = settingsReference.getBeginHours() + "";
            }
            if (settingsReference.getBeginMinutes() < 10) {
                time += ":0" + settingsReference.getBeginMinutes();
            } else {
                time += ":" + settingsReference.getBeginMinutes();
            }
            beginTime.getSelectionModel().select(ShowWindow.localTimeToIndex(LocalTime.parse(time)));
        } else {
            beginTime.getSelectionModel().selectFirst();
        }

        //Save button
        Button saveButton = new Button("Save settings");
        saveButton.setOnAction(event -> saveSettings());

        //Reset simulator
        Button resetButton = new Button("Reset simulator");
        resetButton.setOnAction(event -> {
            DataController.getInstance().readSettings();
            GUI.getSimulatorTab().getSimulator().init();
            if (overwriteStartTime.isSelected()) {
                DataController.getInstance().getClock().setTime(Integer.parseInt(beginTime.getValue().toString().substring(0, 2)), Integer.parseInt(beginTime.getValue().toString().substring(3, 5)), 0);
            }
        });

        overwriteStartTime.setSelected(settingsReference.isOverwriteStartTime());

        //Adding all nodes to the GridPane
        gridPane.add(plannerSettings, 0, 0);
        gridPane.add(deleteAllButton, 0, 1);
        gridPane.add(deleteShowsButton, 0, 2);

        gridPane.add(simulatorSettings, 2, 0);
        gridPane.add(simulatorSpeed, 2, 1);
        gridPane.add(speedSlider, 2, 2);
        gridPane.add(NPCAmount, 2, 4);
        gridPane.add(NPCAmountSlider, 2, 5);

        gridPane.add(speedLabel, 3, 2);
        gridPane.add(amountLabel, 3, 5);

        gridPane.add(prediction, 2, 6);
        gridPane.add(saveButton, 2, 10);
        gridPane.add(resetButton, 3, 10);

        gridPane.add(timeLabel, 2, 7);
        gridPane.add(beginTime, 2, 8);

        gridPane.add(overwriteStartTime, 2, 9);

        settingsTab.setContent(gridPane);
        return settingsTab;
    }

    private void deleteData(boolean onlyRemoveShows) {
        Planner plannerReference = DataController.getInstance().getPlanner();

        Stage deleteDataStage = new Stage();
        deleteDataStage.setResizable(false);
        deleteDataStage.initOwner(this.primaryStage);
        deleteDataStage.initModality(Modality.WINDOW_MODAL);

        HBox setup = new HBox();
        Image warning = new Image("file:Resources/alert.png");
        ImageView showError = new ImageView(warning);
        showError.setFitWidth(100);
        showError.setFitHeight(100);
        setup.getChildren().add(showError);

        VBox lineup = new VBox();
        Label text = new Label();
        lineup.getChildren().add(text);

        HBox buttons = new HBox();
        buttons.getChildren().add(cancelButton(deleteDataStage));

        Button confirm = new Button("Confirm");
        if (onlyRemoveShows) {
            text.setText("Are you sure you want to delete all shows?\nThis change cannot be undone!");
            confirm.setOnAction(e -> plannerReference.deleteShows());
        } else {
            text.setText("Are you sure you want to delete all?\nThis change cannot be undone!");
            confirm.setOnAction(e -> plannerReference.deleteAll());
        }

        EventHandler<? super ActionEvent> oldClickAction = confirm.getOnAction();
        confirm.setOnAction(e -> {
            if (oldClickAction != null) oldClickAction.handle(e);

            plannerReference.savePlanner();
            deleteDataStage.close();
        });

        buttons.getChildren().add(confirm);

        lineup.getChildren().add(buttons);
        setup.getChildren().add(lineup);

        Scene deleteDataScene = new Scene(setup);
        deleteDataStage.setScene(deleteDataScene);

        deleteDataStage.show();
    }

    /**
     * This method creates and returns a cancel button
     *
     * @param stage the stage it is on.
     * @return the cancel button
     */
    private Button cancelButton(Stage stage) {
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> stage.close());

        return cancelButton;
    }

    /**
     * Saves the applied settings of the simulator to a Jsonfile
     */
    private void saveSettings() {
        // writes settings into JSON file settings.json
        try (JsonWriter writer = Json.createWriter(new FileWriter(this.saveFileName))) {
            JsonObjectBuilder settingsBuilder = Json.createObjectBuilder();
            settingsBuilder.add("Simulator Speed", speedSlider.getValue() + "");
            settingsBuilder.add("Visitors per NPC", NPCAmountSlider.getValue());
            settingsBuilder.add("Is Using Prediction", prediction.isSelected());
            settingsBuilder.add("Begin hours", Integer.parseInt(beginTime.getValue().toString().substring(0, 2)));
            settingsBuilder.add("Begin minutes", Integer.parseInt(beginTime.getValue().toString().substring(3, 5)));
            settingsBuilder.add("Use overwrite time", overwriteStartTime.isSelected());
            writer.writeObject(settingsBuilder.build());
            writer.close();

            DataController.getInstance().getClock().setSimulatorSpeed(speedSlider.getValue());
            if (overwriteStartTime.isSelected()) {
                DataController.getInstance().getClock().setTime(Integer.parseInt(beginTime.getValue().toString().substring(0, 2)), Integer.parseInt(beginTime.getValue().toString().substring(3, 5)), 0);
            }

            DataController.getInstance().readSettings();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}