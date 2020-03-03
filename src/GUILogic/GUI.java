package GUILogic;

import PlannerData.Planner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        TabPane tabPane = new TabPane();
        ScheduleTab scheduleTab = new ScheduleTab(primaryStage);
        SimulatorTab simulatorTab = new SimulatorTab();
        SettingsTab settingsTab = new SettingsTab(primaryStage);
        VisualTab visualTab = new VisualTab();


        tabPane.getTabs().addAll(scheduleTab.getScheduleTab(), visualTab.getVisualTab(), simulatorTab.getSimulatorTab(), settingsTab.getSettingsTab());
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            if (newTab == visualTab.getVisualTab()) {
                visualTab.update();
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
}