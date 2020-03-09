package GUILogic;

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

public class SettingsTab {

    private Stage primaryStage;
    private int speed;
    private int amountPerNPC;
    private Tab settingsTab;

    public SettingsTab(Stage primaryStage) {
//        this.amountPerNPC = amount;
//        this.speed = speed;
        this.primaryStage = primaryStage;
        this.settingsTab = new Tab("Settings");
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
        Label amountPerNPC = new Label("Amount of visitors per NPC");

        Button deleteAllButton = new Button("Delete All");
        deleteAllButton.setOnAction(e -> {
            DeleteAllWindow();
        });

        Button deleteShowsButton = new Button("Delete All Shows");
        deleteShowsButton.setOnAction(e -> {
            DeleteShowWindow();
        });

        CheckBox prediction = new CheckBox("Predicted types of guests");

        Slider speedSlider = new Slider();
        speedSlider.setMax(10);
        speedSlider.setMin(0.1);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(2);
        speedSlider.setMinorTickCount(1);
        speedSlider.setBlockIncrement(10);
        Label speedLabel = new Label("");
        speedSlider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldValue,
                    Number newValue) {
                speedLabel.textProperty().setValue(
                        String.valueOf(newValue.intValue()));
            }
        });


        Slider amountPerNPCSlider = new Slider();
        amountPerNPCSlider.setMin(1);
        amountPerNPCSlider.setMax(10000);
        amountPerNPCSlider.setShowTickLabels(true);
        amountPerNPCSlider.setShowTickMarks(true);
        amountPerNPCSlider.setMajorTickUnit(500);
        amountPerNPCSlider.setMinorTickCount(50);
        amountPerNPCSlider.setBlockIncrement(10);
        Label amountLabel = new Label("");
        amountPerNPCSlider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldValue,
                    Number newValue) {
                amountLabel.textProperty().setValue(
                        String.valueOf(newValue.intValue()));
            }
        });

        split.add(planner, 0, 0);
        split.add(deleteAll, 0, 2);
        split.add(deleteAllButton, 0, 3);
        split.add(deleteShows, 0, 4);
        split.add(deleteShowsButton, 0, 5);

        split.add(simulator, 2, 0);
        split.add(speed, 2, 2);
        split.add(speedSlider, 2, 3);
        split.add(amountPerNPC, 2, 4);
        split.add(amountPerNPCSlider, 2, 5);

        split.add(speedLabel,3,3);
        split.add(amountLabel,3,5);

        split.add(prediction,2,6);

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


}