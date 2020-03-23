package GUILogic;

public class Clock {
    private int hours, minutes;
    private double seconds;
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

    public boolean isHalfHourPassed() {
        return halfHourPassed;
    }

    private void pulse() {
        System.out.println("Tick!");
        this.halfHourPassed = true;
    }

    }

    /**
     * set the speed 1 real second is how many seconds in the simulator
     *
     * @param simulatorSpeed limited to 30, higher then 30 makes the simulator not runnable
     */
    public void setSimulatorSpeed(double simulatorSpeed) throws Exception {
        if (simulatorSpeed > 30)
            throw new Exception("speed out of bounds");

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
