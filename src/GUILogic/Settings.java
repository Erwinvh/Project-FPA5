package GUILogic;

/**
 * A class for saving the settings for the simulator Tab
 */
public class Settings {
    private double simulatorSpeed;
    private double visitors;
    private boolean usingPredictedPerson;
    private int beginHours = Integer.MIN_VALUE;
    private int beginMinutes = Integer.MIN_VALUE;
    private boolean overwriteStartTime;

    private static final String SAVE_FILE_NAME = "Resources/settings.json";

    /**
     * getter for the overwrite option
     */
    public boolean isOverwriteStartTime() {
        return overwriteStartTime;
    }

    /**
     * setter fot the overwrite option
     */
    void setOverwriteStartTime(boolean overwriteStartTime) {
        this.overwriteStartTime = overwriteStartTime;
    }

    /**
     * getter for the begin time in hours
     */
    public int getBeginHours() {
        return beginHours;
    }

    /**
     * A setter for the begin time hours
     */
    void setBeginHours(int beginHours) {
        this.beginHours = beginHours;
    }

    /**
     * the getter for the begin time Minutes
     */
    public int getBeginMinutes() {
        return beginMinutes;
    }

    /**
     * the setter for the beginTime minutes
     */
    void setBeginMinutes(int beginMinutes) {
        this.beginMinutes = beginMinutes;
    }

    /**
     * a getter for the simulator speed,
     * a multiplier for the standard simulator speed
     */
    double getSimulatorSpeed() {
        return simulatorSpeed;
    }

    /**
     * a getter for the saveFile name
     */
    String getSaveFileName() {
        return SAVE_FILE_NAME;
    }

    /**
     * a setter for the simulator speed,
     * a multiplier for the standard simulator speed
     */
    void setSimulatorSpeed(double simulatorSpeed) {
        this.simulatorSpeed = simulatorSpeed;
    }

    /**
     * A getter for the amount of visitors
     */
    public double getVisitors() {
        return visitors;
    }

    /**
     * a setter for the amount of visitors
     */
    void setVisitors(double visitorsPerPerson) {
        this.visitors = visitorsPerPerson;
    }

    /**
     * a getter for if the visitor Favorite Genre prediction is being used
     */
    public boolean isUsingPredictedPerson() {
        return usingPredictedPerson;
    }

    /**
     * a setter for if the visitor Favorite Genre prediction is being used
     */
    void setUsingPredictedPerson(boolean usingPredictedPerson) {
        this.usingPredictedPerson = usingPredictedPerson;
    }
}