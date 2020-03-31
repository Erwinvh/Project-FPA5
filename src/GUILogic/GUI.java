package GUILogic;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GUI extends Application {

    private static SimulatorTab simulatorTab;

    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();
        ScheduleTab scheduleTab = new ScheduleTab(primaryStage);
        simulatorTab = new SimulatorTab();
        SettingsTab settingsTab = new SettingsTab(primaryStage);
        VisualTab visualTab = new VisualTab();

        tabPane.getTabs().addAll(scheduleTab.getScheduleTab(), visualTab.getVisualTab(), simulatorTab.getSimulatorTab(), settingsTab.getSettingsTab());
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            if (newTab == visualTab.getVisualTab()) {
                visualTab.update();
            } else if (newTab == scheduleTab.getScheduleTab()) {
                scheduleTab.resetData();
            }
        });

        Scene scene = new Scene(tabPane);
        scene.getStylesheets().add("Main-StyleSheet.css");

        primaryStage.setTitle("Festival Planner");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("logoA5.jpg"));
        primaryStage.setWidth(1280);
        primaryStage.setHeight(720);
        primaryStage.show();
    }

    public static SimulatorTab getSimulatorTab(){ return simulatorTab; }
}