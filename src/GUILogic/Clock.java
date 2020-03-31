package GUILogic;

public class Clock {
    private int hours, minutes;
    private double seconds;
    private double simulatorSpeed;
    private boolean intervalPassed;

    /**
     * The constructor for the Clock
     */
    Clock() {
        this.hours = DataController.getSettings().getBeginHours();
        this.minutes = DataController.getSettings().getBeginMinutes();
        this.seconds = 0;
        this.simulatorSpeed = (90 * DataController.getSettings().getSimulatorSpeed());
        this.intervalPassed = false;
    }

    /**
     * function adds the corresponding time by calculating how much time has passed
     *
     * @param deltaTime in seconds
     */
    public void update(double deltaTime) {
        if (intervalPassed) intervalPassed = false;

        seconds += deltaTime * simulatorSpeed;
        if (seconds >= 60) {
            minutes++;
            seconds -= 60;
            if (minutes % 15 == 0)
                pulse();
            if (minutes == 60) {
                hours++;
                minutes -= 60;
                if (hours == 24) {
                    hours -= 24;
                }
            }
        }
    }

    /**
     * A getter for the boolean of whether a interval has passed
     *
     * @return A true or false value
     */
    public boolean isIntervalPassed() {
        return intervalPassed;
    }

    /**
     * A setter for the interval passed that sets it to true
     */
    private void pulse() {
        this.intervalPassed = true;
    }

    /**
     * The getter for the hour integer
     *
     * @return The integer of the hour
     */
    public int getHours() {
        return hours;
    }

    /**
     * The getter for the minute
     *
     * @return The integer of the minute
     */
    int getMinutes() {
        return minutes;
    }

    /**
     * set the speed 1 real second is how many seconds in the simulator
     */
    void setSimulatorSpeed(double simulatorSpeed) {
        this.simulatorSpeed = simulatorSpeed * 90;
    }

    /**
     * The getter for the simulator speed
     *
     * @return The simulator speed
     */
    public double getSimulatorSpeed() {
        return simulatorSpeed;
    }

    /**
     * set time to parameters.
     *
     * @param hours   new value for hours
     * @param minutes new value for minutes
     * @param seconds new value for seconds
     */
    public void setTime(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        pulse();
    }

    /**
     * This method sets the time back to midnight
     */
    public void setToMidnight() {
        this.hours = 0;
        this.minutes = 0;
        this.seconds = 0;
    }

    /**
     * A to string function to make the time representable as a string.
     *
     * @return Time as a string
     */
    public String toString() {
        String h, m;
        if (hours < 10) {
            h = "0" + hours;
        } else {
            h = "" + hours;
        }

        if (minutes < 10) {
            m = "0" + minutes;
        } else {
            m = "" + minutes;
        }
        return h + ":" + m;
    }
}