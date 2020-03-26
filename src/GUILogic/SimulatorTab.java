package GUILogic;

import GUILogic.SimulatorLogic.Simulator;
import javafx.scene.control.Tab;

class SimulatorTab {
    private Tab simulatorTab;
    private Simulator simulator;

    /**
     * The constructor for the simulator tab
     */
    SimulatorTab() {
        this.simulatorTab = new Tab("Simulator");
        this.simulator = new Simulator();
        this.simulatorTab.setContent(this.simulator.getSimulatorLayout());
    }

    /**
     * The getter for the Simulator
     * @return The simulator
     */
    Simulator getSimulator() {
        return simulator;
    }

    /**
     * The getter for the simulator tab
     * @return The simulator tab
     */
    Tab getSimulatorTab() {
        return simulatorTab;
    }
}