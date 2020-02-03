package GUILogic;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class GUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        TabPane tabPane = new TabPane();
        ScheduleTab scheduleTab = new ScheduleTab(primaryStage);
        SimulatorTab simulatorTab = new SimulatorTab();
        SettingsTab settingsTab = new SettingsTab();
        VisualTab visualTab = new VisualTab();

        tabPane.getTabs().addAll(scheduleTab.getScheduleTab(), visualTab.getVisualTab(), simulatorTab.getSimulatorTab(), settingsTab.getSettingsTab());
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Scene scene = new Scene(tabPane);
        scene.getStylesheets().add("StyleSheet.css");
        primaryStage.setTitle("Festival Planner");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setWidth(960);
        primaryStage.setHeight(540);
        primaryStage.show();
    }
}