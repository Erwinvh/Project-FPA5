package GUILogic;

import PlannerData.Planner;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class SettingsTab {

    private Stage primaryStage;
    private Tab settingsTab;
    private Planner planning = DataController.getPlanner();
    private String saveFileName;
    private Slider speedSlider;
    private Slider NPCAmountSlider;
    private CheckBox prediction;
    private ComboBox beginHours;
    private ComboBox beginMinutes;

    public SettingsTab(Stage primaryStage) {
//        this.amountPerNPC = amount;
//        this.speed = speed;
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
        this.beginMinutes = new ComboBox();
        beginMinutes.setValue(DataController.getSettings().getBeginMinutes());
    }

    public Tab getSettingsTab() {

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
        deleteAllButton.setOnAction(e -> {
            DeleteAllWindow();
        });

        Button deleteShowsButton = new Button("Delete All Shows");
        deleteShowsButton.setOnAction(e -> {
            DeleteShowWindow();
        });

        prediction.setText("Predicted types of guests");

        speedSlider.setMax(2);
        speedSlider.setMin(0);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(1);
       // speedSlider.setMinorTickCount();
        speedSlider.setBlockIncrement(0.1);
        Label speedLabel = new Label("");
        DecimalFormat format = new DecimalFormat("0.0");
        speedLabel.setText(format.format( DataController.getSettings().getSimulatorSpeed()*100)+"%");

        speedSlider.valueProperty().addListener(new ChangeListener<Number>() {



            @Override
            public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldValue,
                    Number newValue) {
                speedLabel.textProperty().setValue(
                        String.valueOf(format.format( newValue.floatValue()* 100)+"%"));
            }
        });


        NPCAmountSlider.setMin(1);
        NPCAmountSlider.setMax(300);
        NPCAmountSlider.setShowTickLabels(true);
        NPCAmountSlider.setShowTickMarks(true);
        NPCAmountSlider.setMajorTickUnit(50);
        NPCAmountSlider.setMinorTickCount(10);
        NPCAmountSlider.setBlockIncrement(10);
        Label amountLabel = new Label("");
        amountLabel.setText(DataController.getSettings().getVisitors()+"");
        NPCAmountSlider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldValue,
                    Number newValue) {
                amountLabel.textProperty().setValue(
                        String.valueOf(newValue.intValue()));
            }
        });

        Label hourLabel = new Label("Begin time hours");
        for(int i = 0; i < 24; i++){
            beginHours.getItems().add(i);
        }
        Label minuteLabel = new Label("Begin time minutes");
        for(int i = 0; i < 60; i++){
            beginMinutes.getItems().add(i);
        }

        Button saveButton = new Button("Save settings");
        saveButton.setOnAction(event ->  saveSettings());

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

        split.add(speedLabel,3,3);
        split.add(amountLabel,3,5);

        split.add(prediction,2,6);
        split.add(saveButton, 2, 9);

        split.add(hourLabel,2,7);
        split.add(minuteLabel,3,7);
        split.add(beginHours,2,8);
        split.add(beginMinutes,3,8);

        settingsTab.setContent(split);
        return settingsTab;
    }

    public void DeleteAllWindow() {
        Stage DeleteAll = new Stage();
        DeleteAll.setResizable(false);
        DeleteAll.initOwner(this.primaryStage);
        DeleteAll.initModality(Modality.WINDOW_MODAL);

        HBox Setup = new HBox();
        Image warning = new Image("file:Resources/alert.png");
        ImageView showError = new ImageView(warning);
        showError.setFitWidth(100);
        showError.setFitHeight(100);
        Setup.getChildren().add(showError);

        VBox lineup = new VBox();
        Label text = new Label("Are you sure you want to delete all?" + '\n' + "This change cannot be undone!");
        lineup.getChildren().add(text);

        HBox Buttons = new HBox();
        Buttons.getChildren().add(cancelButton(DeleteAll));

        Button confirm = new Button("Confirm");
        confirm.setOnAction(e -> {
            DataController.getPlanner().deleteAll();
            DataController.getPlanner().savePlanner();
            DeleteAll.close();
        });
        Buttons.getChildren().add(confirm);

        lineup.getChildren().add(Buttons);
        Setup.getChildren().add(lineup);
        Scene DeleteShowScene = new Scene(Setup);
        DeleteAll.setScene(DeleteShowScene);

        DeleteAll.show();
    }

    public void DeleteShowWindow() {
        Stage DeleteShow = new Stage();
        DeleteShow.setResizable(false);
        DeleteShow.initOwner(this.primaryStage);
        DeleteShow.initModality(Modality.WINDOW_MODAL);

        HBox Setup = new HBox();
        Image warning = new Image("file:Resources/alert.png");
        ImageView showError = new ImageView(warning);
        showError.setFitWidth(100);
        showError.setFitHeight(100);
        Setup.getChildren().add(showError);

        VBox lineup = new VBox();
        Label text = new Label("Are you sure you want to delete all shows?" + '\n' + "This change cannot be undone!");
        lineup.getChildren().add(text);

        HBox Buttons = new HBox();
        Buttons.getChildren().add(cancelButton(DeleteShow));

        Button confirm = new Button("Confirm");
        confirm.setOnAction(e -> {
            DataController.getPlanner().deleteShows();
            DataController.getPlanner().savePlanner();
            DeleteShow.close();
        });
        Buttons.getChildren().add(confirm);

        lineup.getChildren().add(Buttons);
        Setup.getChildren().add(lineup);
        Scene DeleteShowScene = new Scene(Setup);
        DeleteShow.setScene(DeleteShowScene);

        DeleteShow.show();
    }

    public Button cancelButton(Stage stage) {
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
           stage.close();
        });

        return cancelButton;
    }

    /**
     * Saves the applied settings of the simulator to a Jsonfile
     */
    public void saveSettings(){
        try {
            JsonWriter writer = Json.createWriter(new FileWriter(this.saveFileName));
            JsonObjectBuilder settingsBuilder = Json.createObjectBuilder();
            settingsBuilder.add("Simulator Speed", speedSlider.getValue()+ "");
            settingsBuilder.add("Vistors per NPC",NPCAmountSlider.getValue());
            settingsBuilder.add("Is Using Prediction", prediction.isSelected());
            settingsBuilder.add("Begin hours", Integer.parseInt( beginHours.getValue().toString()));
            settingsBuilder.add("Begin minutes",Integer.parseInt( beginMinutes.getValue().toString()));
            writer.writeObject(settingsBuilder.build());
            writer.close();
            DataController.getClock().setHours(Integer.parseInt( beginHours.getValue().toString()));
            DataController.getClock().setMinutes(Integer.parseInt( beginMinutes.getValue().toString()));
            DataController.getClock().setSimulatorSpeed(speedSlider.getValue());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



}