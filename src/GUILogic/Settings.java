package GUILogic;
/**
 * A class for saving the settings for the simulator Tab
 */
public class Settings {
    private double simulatorSpeed;
    private double visitors;
    private boolean usingPredictedPerson;
    private int beginHours;
    private int beginMinutes;
    private boolean overwriteStartTime;

    public boolean isOverwriteStartTime() {
        return overwriteStartTime;
    }

    public void setOverwriteStartTime(boolean overwriteStartTime) {
        this.overwriteStartTime = overwriteStartTime;
    }

    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
    }

    public int getBeginHours() {
        return beginHours;
    }

    public void setBeginHours(int beginHours) {
        this.beginHours = beginHours;
    }

    public int getBeginMinutes() {
        return beginMinutes;
    }

    public void setBeginMinutes(int beginMinutes) {
        this.beginMinutes = beginMinutes;
    }

    private String saveFileName = "Resources/settings.json";

    public double getSimulatorSpeed() {
        return simulatorSpeed;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public void setSimulatorSpeed(double simulatorSpeed) {
        this.simulatorSpeed = simulatorSpeed;
    }

    public double getVisitors() {
        return visitors;
    }

    public void setVisitors(double visitorsPerPerson) {
        this.visitors = visitorsPerPerson;
    }

    public boolean isUsingPredictedPerson() {
        return usingPredictedPerson;
    }

    public void setUsingPredictedPerson(boolean usingPredictedPerson) {
        this.usingPredictedPerson = usingPredictedPerson;
    }
}

