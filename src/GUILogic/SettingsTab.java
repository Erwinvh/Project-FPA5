package GUILogic;

import javafx.scene.control.Tab;

public class SettingsTab {
    private Tab settingsTab;

    public SettingsTab() {
        this.settingsTab = new Tab("Settings");
    }

    public Tab getSettingsTab() {
        return settingsTab;
    }
}