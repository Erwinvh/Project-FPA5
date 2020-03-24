package GUILogic;

import java.time.LocalTime;

public class Clock {    
    private int hours, minutes;
    private double seconds;
    private double simulatorSpeed;
    private boolean halfHourPassed;

    public Clock() {
        this.hours = 0;
        this.minutes = 0;
        this.seconds = 0;
        this.simulatorSpeed = 30;
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
        seconds += deltaTime * simulatorSpeed;
        if (seconds >= 60) {
            System.out.println(minutes);
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

    public boolean isHalfHourPassed() {
        return halfHourPassed;
    }

    private void pulse() {
        System.out.println("Tick!");
        this.halfHourPassed = true;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public double getSeconds() {
        return seconds;
    }

    public void setSeconds(double seconds) {
        this.seconds = seconds;
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

    public String toString(){
        String h, m;
        if (hours < 10){
            h = "0" + hours;
        } else {
            h = "" + hours;
        }

        if (minutes < 10){
            m = "0" + minutes;
        } else {
            m = "" + minutes;
        }
        return h + ":" + m;
    }
}
