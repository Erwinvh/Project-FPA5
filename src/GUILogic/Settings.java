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
    private boolean reset;

    /**
     * The getter for the state of reset
     *
     * @return
     */
    public boolean isReset() {
        return reset;
    }

    /**
     * The setter for the state of reset
     *
     * @param reset
     */
    public void setReset(boolean reset) {
        this.reset = reset;
    }

    private String saveFileName = "Resources/settings.json";

    /**
     * getter for the overwrite option
     *
     * @return
     */
    public boolean isOverwriteStartTime() {
        return overwriteStartTime;
    }

    /**
     * setter fot the overwrite option
     *
     * @param overwriteStartTime
     */
    void setOverwriteStartTime(boolean overwriteStartTime) {
        this.overwriteStartTime = overwriteStartTime;
    }

    /**
     * getter for the begin time in hours
     *
     * @return
     */
    public int getBeginHours() {
        return beginHours;
    }

    /**
     * A setter for the begin time hours
     *
     * @param beginHours
     */
    void setBeginHours(int beginHours) {
        this.beginHours = beginHours;
    }

    /**
     * the getter for the begin time Minutes
     *
     * @return
     */
    public int getBeginMinutes() {
        return beginMinutes;
    }

    /**
     * the setter for the beginTime minutes
     *
     * @param beginMinutes
     */
    void setBeginMinutes(int beginMinutes) {
        this.beginMinutes = beginMinutes;
    }

    /**
     * a getter for the simulator speed,
     * a multiplier for the standard simulator speed
     *
     * @return
     */
    double getSimulatorSpeed() {
        return simulatorSpeed;
    }

    /**
     * a getter for the saveFile name
     *
     * @return
     */
    String getSaveFileName() {
        return saveFileName;
    }

    /**
     * a setter for the simulator speed,
     * a multiplier for the standard simulator speed
     *
     * @param simulatorSpeed
     */
    void setSimulatorSpeed(double simulatorSpeed) {
        this.simulatorSpeed = simulatorSpeed;
    }

    /**
     * A getter for the amount of visitors
     *
     * @return
     */
    public double getVisitors() {
        return visitors;
    }

    /**
     * a setter for the amount of visitors
     *
     * @param visitorsPerPerson
     */
    void setVisitors(double visitorsPerPerson) {
        this.visitors = visitorsPerPerson;
    }

    /**
     * a getter for if the visitor Favorite Genre prediction is being used
     *
     * @return
     */
    public boolean isUsingPredictedPerson() {
        return usingPredictedPerson;
    }

    /**
     * a setter for if the visitor Favorite Genre prediction is being used
     *
     * @param usingPredictedPerson
     */
    void setUsingPredictedPerson(boolean usingPredictedPerson) {
        this.usingPredictedPerson = usingPredictedPerson;
    }
}

