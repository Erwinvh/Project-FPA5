package GUILogic;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class GUI extends Application {

    public static void main(String[] args) {
        launch(GUI.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        TabPane tabPane = new TabPane();
        ScheduleTab scheduleTab = new ScheduleTab();
        SimulatorTab simulatorTab = new SimulatorTab();
        SettingsTab settingsTab = new SettingsTab();
        

        tabPane.getTabs().addAll(scheduleTab.getScheduleTab(), simulatorTab.getSimulatorTab(), settingsTab.getSettingsTab());
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Scene scene = new Scene(tabPane);
        primaryStage.setTitle("Festival Planner");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setWidth(960);
        primaryStage.setHeight(540);
        primaryStage.show();
    }
}