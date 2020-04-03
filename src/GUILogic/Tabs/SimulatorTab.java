package GUILogic.Tabs;

import GUILogic.SimulatorLogic.Simulator;
import javafx.scene.control.Tab;

public class SimulatorTab {
    private Tab simulatorTab;
    private Simulator simulator;

    /**
     * The constructor for the simulator tab
     */
    public SimulatorTab() {
        this.simulatorTab = new Tab("Simulator");
        this.simulator = new Simulator();
        this.simulatorTab.setContent(this.simulator.getSimulatorLayout());
    }

    /**
     * The getter for the Simulator
     *
     * @return The simulator
     */
    public Simulator getSimulator() {
        return simulator;
    }

    /**
     * The getter for the simulator tab
     *
     * @return The simulator tab
     */
    public Tab getSimulatorTab() {
        return simulatorTab;
    }
}