package GUILogic;

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

class SettingsTab {

    private Stage primaryStage;
    private Tab settingsTab;
    private String saveFileName;
    private Slider speedSlider;
    private Slider NPCAmountSlider;
    private CheckBox prediction;
    private ComboBox beginHours;
    private CheckBox overwriteStartTime;

    /**
     * The constructor of the settings tab
     *
     * @param primaryStage the window of the main application
     */
    SettingsTab(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.settingsTab = new Tab("Settings");
        this.saveFileName = DataController.getSettings().getSaveFileName();

        this.speedSlider = new Slider();
        speedSlider.setValue(DataController.getSettings().getSimulatorSpeed());

        this.NPCAmountSlider = new Slider();
        NPCAmountSlider.setValue(DataController.getSettings().getVisitors());

        this.prediction = new CheckBox();
        prediction.setSelected(DataController.getSettings().isUsingPredictedPerson());

        this.beginHours = new ComboBox();
        beginHours.setValue(DataController.getSettings().getBeginHours());
        this.overwriteStartTime = new CheckBox();
        overwriteStartTime.setText("Use this startingTime");
        ArrayList timeList = ShowWindow.setupTimeList();
        beginHours = ShowWindow.getTimestampsComboBox(0, timeList);
    }

    /**
     * builds and returns the settingsTab
     *
     * @return the settings tab
     */
    Tab getSettingsTab() {

        GridPane split = new GridPane();

        split.setAlignment(Pos.CENTER);
        split.setHgap(200);
        split.setVgap(20);

        Label planner = new Label("Planner settings");
        Label simulator = new Label("Simulator settings");

        Label deleteAll = new Label("Delete all data");
        Label deleteShows = new Label("Delete all shows");

        Label speed = new Label("Simulator speed");
        Label NPCAmount = new Label("Amount of visitors");

        Button deleteAllButton = new Button("Delete All");
        deleteAllButton.setOnAction(e -> deleteData(false));

        Button deleteShowsButton = new Button("Delete All Shows");
        deleteShowsButton.setOnAction(e -> deleteData(true));

        //Prediction checkbox
        prediction.setText("Predicted types of guests");

        //Simulator speed slider
        speedSlider.setMax(2);
        speedSlider.setMin(0);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(1);
        speedSlider.setBlockIncrement(0.1);
        Label speedLabel = new Label("");
        DecimalFormat format = new DecimalFormat("0.0");
        speedLabel.setText(format.format(DataController.getSettings().getSimulatorSpeed() * 100) + "%");

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
        amountLabel.setText(DataController.getSettings().getVisitors() + "");
        NPCAmountSlider.valueProperty().addListener((observableValue, oldValue, newValue) ->
                amountLabel.textProperty().setValue(String.valueOf(newValue.intValue()))
        );



        //Hour ComboBox
        Label timeLabel = new Label("Begin time");
//        for(int i = 0; i < 24; i++){
//            beginHours.getItems().add(i);
//        }
//
if (DataController.getSettings().getBeginHours()>=0){
    String time;
    if (DataController.getSettings().getBeginHours()<=9){
        time = "0"+DataController.getSettings().getBeginHours();
    }
    else{
        time = DataController.getSettings().getBeginHours()+"";
    }
    if (DataController.getSettings().getBeginMinutes()<10){
        time+=":0" + DataController.getSettings().getBeginMinutes();
    }
    else{
        time+= ":" + DataController.getSettings().getBeginMinutes();
    }
    beginHours.getSelectionModel().select(ShowWindow.localTimeToIndex(LocalTime.parse(time)));
}
else{
    beginHours.getSelectionModel().selectFirst();
}

        //Save button
        Button saveButton = new Button("Save settings");
        saveButton.setOnAction(event -> saveSettings());

        //Reset simulator
        Button resetButton = new Button("Reset simulator");
        resetButton.setOnAction(event -> {
            DataController.readSettings();
            GUI.getSimulatorTab().getSimulator().init();
            if(overwriteStartTime.isSelected()) {
                DataController.getClock().setTime(Integer.parseInt(beginHours.getValue().toString().substring(0,2)), Integer.parseInt(beginHours.getValue().toString().substring(3,5)), 0);
            }
        });

        overwriteStartTime.setSelected(DataController.getSettings().isOverwriteStartTime());

        //Adding all nodes to the GridPane
        split.add(planner, 0, 0);
        split.add(deleteAll, 0, 2);
        split.add(deleteAllButton, 0, 3);
        split.add(deleteShows, 0, 4);
        split.add(deleteShowsButton, 0, 5);

        split.add(simulator, 2, 0);
        split.add(speed, 2, 2);
        split.add(speedSlider, 2, 3);
        split.add(NPCAmount, 2, 4);
        split.add(NPCAmountSlider, 2, 5);

        split.add(speedLabel, 3, 3);
        split.add(amountLabel, 3, 5);

        split.add(prediction, 2, 6);
        split.add(saveButton, 2, 10);
        split.add(resetButton, 3, 10);

        split.add(timeLabel,2,7);

        split.add(beginHours,2,8);

        split.add(overwriteStartTime, 2, 9);

        settingsTab.setContent(split);
        return settingsTab;
    }

    private void deleteData(boolean onlyRemoveShows) {
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
            confirm.setOnAction(e -> DataController.getPlanner().deleteShows());
        } else {
            text.setText("Are you sure you want to delete all?" + '\n' + "This change cannot be undone!");
            confirm.setOnAction(e -> DataController.getPlanner().deleteAll());
        }

        EventHandler<? super ActionEvent> oldClickAction = confirm.getOnAction();
        confirm.setOnAction(e -> {
            if (oldClickAction != null) oldClickAction.handle(e);

            DataController.getPlanner().savePlanner();
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
        try (JsonWriter writer = Json.createWriter(new FileWriter(this.saveFileName))) {
            JsonObjectBuilder settingsBuilder = Json.createObjectBuilder();
            settingsBuilder.add("Simulator Speed", speedSlider.getValue() + "");
            settingsBuilder.add("Visitors per NPC", NPCAmountSlider.getValue());
            settingsBuilder.add("Is Using Prediction", prediction.isSelected());
            settingsBuilder.add("Begin hours", Integer.parseInt( beginHours.getValue().toString().substring(0,2)));
            settingsBuilder.add("Begin minutes", Integer.parseInt( beginHours.getValue().toString().substring(3,5)));
            settingsBuilder.add("Use overwrite time", overwriteStartTime.isSelected());
            writer.writeObject(settingsBuilder.build());
            writer.close();

            DataController.getClock().setSimulatorSpeed(speedSlider.getValue());
            if(overwriteStartTime.isSelected()) {
                DataController.getClock().setTime(Integer.parseInt(beginHours.getValue().toString().substring(0,2)), Integer.parseInt(beginHours.getValue().toString().substring(3,5)), 0);
            }

            DataController.readSettings();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}