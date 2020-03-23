package GUILogic.SimulatorLogic.NPCLogic;

import GUILogic.SimulatorLogic.MapData.MapDataController;
import GUILogic.SimulatorLogic.MapData.TargetArea;
import NPCLogic.Person;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Random;

public class PersonLogic {

    private final double speedMultiplyer;
    private Point2D position;
    private double angle;
    private double speed;
    private Point2D target;
    private double rotationSpeed;
    private DistanceMap distanceMap;
    private String activity;
    private NPCLogic.Person person;
    private Point2D newPosition;

    private boolean isRoaming = false;

    private int negativeFeedback = 5;

    private boolean isArtist;

    public PersonLogic(Point2D position, double speed, NPCLogic.Person person, boolean isArtist) {
        Random random = new Random();
        this.speedMultiplyer = ((120.0-random.nextInt(40))/100);

        this.position = position;
        this.person = person;
        this.angle = 0;
        this.speed = speed * speedMultiplyer;
        this.rotationSpeed = 100;
        selectRandomMap();
        this.isArtist = isArtist;
        target = PathCalculator.nextPositionToTarget(this.position, distanceMap);

    }

    public void choiceMaker() {
        Random random = new Random();
        int number = random.nextInt(11);
        //int number = (int) (Math.random() * ((10 - 1) + 1)) + 1;
        if (number <= 5/*this.favoriteGenre==genre.getSuperGenre()*/) {
            //change back once integrated with the main application
            if (number < 2) {
                System.out.println("didn't go, so idle");
                this.negativeFeedback--;
            } else {
                System.out.println("did go to the show");
                this.negativeFeedback = 5;
            }
        } else {
            if (number <= this.negativeFeedback) {
                System.out.println("didn't go, so idle");
                this.negativeFeedback--;
            } else {
                System.out.println("did go to the show");
                this.negativeFeedback = 5;
            }
        }
    }

    /**
     * checks if the NPCLogic.Person has arrived at the target
     *
     * @return
     */
    public boolean hasArrivedAtTarget() {
        double distanceAmount = 17;
        return position.distance(target.getX(), target.getY()) < distanceAmount;
    }

    /**
     * checks if the Person has arrived at it's end destination
     */
    private boolean hasArrivedAtDestination() {
        double distanceAmount = 16;
        return this.target.distance(new Point2D.Double(-1, -1)) < distanceAmount;
    }

    public void setNextTarget() {
        this.target = PathCalculator.nextPositionToTarget(this.position, distanceMap);
    }

    /**
     * For testing purposes!
     * Selects a random distanceMap
     *
     * @return the name of the map
     */
    public void selectRandomMap() {

        //TODO: Rename this function and check if it is an artist or visitor with isArtist boolean

        if (isArtist) {
            // Not a random target unless artist doesn't have anything in his schedule at this time
            // TODO: Add artist logic to assign a target stage
        } else {
            TargetArea[] targetAreas = MapDataController.getTargetAreas();
            Random random = new Random();
            int index = random.nextInt(targetAreas.length);

            TargetArea.TargetAreaType targetAreaType = targetAreas[index].getTargetAreaType();
            if (targetAreaType.equals(TargetArea.TargetAreaType.ALL) || targetAreaType.equals(TargetArea.TargetAreaType.VISITOR)) {
                this.distanceMap = MapDataController.getDistanceMap(targetAreas[index]);
            } else selectRandomMap();
        }
    }

    public Point2D getNewPosition() {
        return newPosition;
    }

    public void update() {
        if (hasArrivedAtDestination()) {
            isRoaming = true;
            roamInTargetArea();
//            selectRandomMap();
//            setNextTarget();
        } else if (isRoaming && hasArrivedAtTarget()) {
            roamInTargetArea();
        } else if (hasArrivedAtTarget()) {
            setNextTarget();
        }

        double targetAngle = Math.atan2(this.target.getY() - this.position.getY(),
                this.target.getX() - this.position.getX());

        double angleDifference = this.angle - targetAngle;
        while (angleDifference < -Math.PI)
            angleDifference += 2 * Math.PI;
        while (angleDifference > Math.PI)
            angleDifference -= 2 * Math.PI;

        if (Math.abs(angleDifference) < this.rotationSpeed)
            this.angle = targetAngle;
        else if (angleDifference < 0)
            this.angle += this.rotationSpeed;
        else
            this.angle -= this.rotationSpeed;

        this.newPosition = new Point2D.Double(this.position.getX() + this.speed * Math.cos(this.angle),
                this.position.getY() + this.speed * Math.sin(this.angle));
    }

    /**
     * This function sets the target of this Person to be within it's targetArea, thus allowing it to walk around a certain targetArea
     */
    private void roamInTargetArea() {
        if (isRoaming) {
            Point2D minPos = distanceMap.getTarget().getPosition();

            double randomX = Math.random() * distanceMap.getTarget().getSize().getX();
            double randomY = Math.random() * distanceMap.getTarget().getSize().getY();

            this.setTarget(new Point2D.Double(minPos.getX() + randomX, minPos.getY() + randomY));
        }
    }

    public boolean isRoaming() {
        return isRoaming;
    }

    public void setRoaming(boolean roaming) {
        isRoaming = roaming;
    }

    public DistanceMap getDistanceMap() {
        return this.distanceMap;
    }

    public Point2D getPosition() {
        return position;
    }

    public AffineTransform getTransform() {
        AffineTransform tx = new AffineTransform();
        tx.translate(position.getX() - this.person.getSprite().getWidth() * 0.5, position.getY() - this.person.getSprite().getHeight() * 0.5);
        tx.rotate(this.angle, this.person.getSprite().getWidth() * 0.5, this.person.getSprite().getHeight() * 0.5);
        return tx;
    }

    public void setTarget(Point2D target) {
        this.target = target;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public double getAngle() {
        return angle;
    }

    public double getSpeed() {
        return speed;
    }

    public Point2D getTarget() {
        return target;
    }

    public double getRotationSpeed() {
        return rotationSpeed;
    }

    public Person getPerson() {
        return person;
    }

    public int getNegativeFeedback() {
        return negativeFeedback;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public void setRotationSpeed(double rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public void setNegativeFeedback(int negativeFeedback) {
        this.negativeFeedback = negativeFeedback;
    }
}