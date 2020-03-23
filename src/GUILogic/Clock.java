package GUILogic;

import java.time.LocalTime;

public class Clock {
    private LocalTime localTime;
    private double simulatorSpeed;
    private double counter;

    public Clock(LocalTime localTime) {
        this.localTime = localTime;
        this.simulatorSpeed = 4;
        this.counter = 0;
    }

    /**
     * function adds the corresponding time by calculating how much time has passed
     *
     * @param deltaTime in seconds
     */
    public void update(double deltaTime) {
        counter += deltaTime * simulatorSpeed;
        if (counter >= 1) {
            localTime = localTime.plusSeconds(1);
            counter--;
        }
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    /**
     * set the speed 1 real second is how many seconds in the simulator
     * @param simulatorSpeed limited to 30, higher then 30 makes the simulator not runnable
     */
    public void setSimulatorSpeed(double simulatorSpeed) throws Exception {
        if (simulatorSpeed>30)
            throw new Exception("speed out of bounds");

        this.simulatorSpeed = simulatorSpeed;
    }

    public double getSimulatorSpeed() {
        return simulatorSpeed;
    }
}
