package GUILogic;

public class Settings {
    private double simulatorSpeed;
    private double visitorsPerPerson;
    private boolean usingPredictedPerson;
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

    public double getVisitorsPerPerson() {
        return visitorsPerPerson;
    }

    public void setVisitorsPerPerson(double visitorsPerPerson) {
        this.visitorsPerPerson = visitorsPerPerson;
    }

    public boolean isUsingPredictedPerson() {
        return usingPredictedPerson;
    }

    public void setUsingPredictedPerson(boolean usingPredictedPerson) {
        this.usingPredictedPerson = usingPredictedPerson;
    }
}

