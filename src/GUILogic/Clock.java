package GUILogic;

import java.time.LocalTime;

public class Clock {
    private LocalTime localTime;
    private double simulatorSpeed;
    private double counter;
    private boolean halfHourPassed;

    public Clock(LocalTime localTime) {
        this.localTime = localTime;
        this.simulatorSpeed = 60;
        this.counter = 0;
        this.halfHourPassed = false;
    }


    /**
     * function adds the corresponding time by calculating how much time has passed
     *
     * @param deltaTime in seconds
     */
    public void update(double deltaTime) {
        if(halfHourPassed){
            halfHourPassed = false;
        }
        counter += deltaTime * simulatorSpeed;
        if (counter >= 1) {
            localTime = localTime.plusSeconds(1);
            if (localTime.getMinute()==0||localTime.getMinute()==30){
                pulse();
            }
            counter--;
        }
    }

    public boolean isHalfHourPassed() {
        return halfHourPassed;
    }

    private void pulse() {
        System.out.println("Tick!");
        this.halfHourPassed = true;
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
