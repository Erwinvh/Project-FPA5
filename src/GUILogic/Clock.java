package GUILogic;

public class Clock {
    private int hours, minutes;
    private double seconds;
    private double simulatorSpeed;

    public Clock() {
        this.hours = 0;
        this.minutes = 0;
        this.seconds = 0;
        this.simulatorSpeed = 60;
    }

    /**
     * function adds the corresponding time by calculating how much time has passed
     *
     * @param deltaTime in seconds
     */
    public void update(double deltaTime) {
        seconds += deltaTime * simulatorSpeed;
        if (seconds == 60) {
            minutes++;
            seconds = 0;
            if (minutes == 30)
                pulse();
            if (minutes == 60) {
                pulse();
                hours++;
                minutes = 0;
                if (hours == 24) {
                    hours = 0;
                }
            }
        }

    }

    private void pulse() {

    }

    /**
     * set the speed 1 real second is how many seconds in the simulator
     */
    public void setSimulatorSpeed(double simulatorSpeed) throws Exception {
        this.simulatorSpeed = simulatorSpeed;
    }

    public double getSimulatorSpeed() {
        return simulatorSpeed;
    }

    public void setToMidnight() {
        this.hours = 0;
        this.minutes = 0;
        this.seconds = 0;
    }

}
