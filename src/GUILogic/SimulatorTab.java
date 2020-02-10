package GUILogic;

import javafx.scene.control.Tab;

class SimulatorTab {
    private Tab simulatorTab;

    SimulatorTab() {
        this.simulatorTab = new Tab("Simulator");
    }

    Tab getSimulatorTab() {
        return simulatorTab;
    }
}