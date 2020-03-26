package GUILogic.SimulatorLogic.NPCLogic;

import GUILogic.DataController;
import GUILogic.SimulatorLogic.MapData.MapDataController;
import NPCLogic.Person;
import PlannerData.Artist;
import PlannerData.Show;
import PlannerData.Stage;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class PersonLogic {

    private Point2D position;
    private double angle;
    private double speed;
    private Point2D target;
    private double rotationSpeed;
    private DistanceMap distanceMap;
    private String activity;
    private Person person;
    private Point2D newPosition;
    private double speedMultiplier;

    private boolean isRoaming = false;
    private int negativeFeedback = 5;

    /**
     * The constructor for the npc logic
     * @param position the position of the npc
     * @param speed the speed of the npc
     * @param person the person object connected to the npc
     * @param isArtist the
     */
    public PersonLogic(Point2D position, double speed, Person person, boolean isArtist) {
        Random random = new Random();
        this.speedMultiplier = ((120.0 - random.nextInt(40)) / 100);

        this.position = position;
        this.person = person;
        this.angle = 0;
        this.speed = speed * speedMultiplier;
        this.rotationSpeed = 100;
        selectNewMap(DataController.getActiveShows());
        target = PathCalculator.nextPositionToTarget(this.position, distanceMap);
    }

    /**
     * checks if the Person has arrived at the target
     * @return true or false depending on distance between target and current position
     */
    public boolean hasArrivedAtTarget() {
        double distanceAmount = 17;
        return position.distance(target.getX(), target.getY()) < distanceAmount;
    }

    /**
     * Checks if the Person has arrived at it's end destination
     */
    private boolean hasArrivedAtDestination() {
        double distanceAmount = 16;
        return this.target.distance(new Point2D.Double(-100, -100)) < distanceAmount;
    }

    /**
     * Sets the next target of the person of all adjacent tiles
     */
    public void setNextTarget() {
        this.target = PathCalculator.nextPositionToTarget(this.position, distanceMap);
    }

    /**
     * Sets the DistanceMap to a map determined by the isGoingToShow method
     * @param activeShows a list of shows being performed on the current time of the Clock
     */
    public void selectNewMap(ArrayList<Show> activeShows) {
        this.isRoaming = false;
        Collections.sort(activeShows);
        int totalExpectedPopularity = 0;
        for(Show show : activeShows){
            totalExpectedPopularity += show.getExpectedPopularity();
        }
        for (Show show : activeShows) {
            if (isGoingToShow(show, totalExpectedPopularity)) {
                DistanceMap targetMap = getDistanceMap(show.getStage(), person.isArtist());
                if (targetMap != null) {
                    this.distanceMap = targetMap;
                    return;
                }
            }
        }

        String idleName = "Idle" + (int) (Math.random() * 5 + 1);
        this.distanceMap = MapDataController.getDistanceMap(idleName);
    }

    /**
     * Gets the distanceMap of a stage depending if the person is an artist
     * If the person is an artist the distance map will be located on the stage itself
     * If the person is not an artist the distance map destination will be the viewing area
     * @param wantedStage The desired stage
     * @param isArtist True if the person is an artists
     * @return The correct DistanceMap of the stage
     */
    public DistanceMap getDistanceMap(Stage wantedStage, boolean isArtist){
        Stage searchingStage = null;
        for (Stage stage : DataController.getPlanner().getStages()) {
            if (stage.getName().equals(wantedStage.getName())) {
                searchingStage = stage;
            }
        }

        int stageIndex = DataController.getPlanner().getStages().indexOf(searchingStage);
        for (DistanceMap distanceMap : MapDataController.getDistanceMaps()) {
            if (isArtist) {
                if (distanceMap.getMapName().equals("ArtistStage" + (stageIndex + 1))) {
                    return distanceMap;
                }
            } else {
                if (distanceMap.getMapName().equals("VisitorStage" + (stageIndex + 1))) {
                    return distanceMap;
                }
            }
        }

        return null;
    }

    /**
     * Determines if a person is going to a show
     *
     * @param show the show the person is deciding to go to
     * @return true if the persone wants to go to the show
     */
    private boolean isGoingToShow(Show show, int totalExpectedPopularity) {
        if (person.isArtist()) {
            for (Artist artist : show.getArtists()) {
                if (person.getName().equals(artist.getName())) {
                    return true;
                }
            }
            return false;
        }

        double chance = Math.random();
        if (person.getFavoriteGenre().getFancyName().equals(show.getGenre().getSuperGenre())) {
            return chance <= ( ( show.getExpectedPopularity() *3.0)) /  ((double) totalExpectedPopularity);
        }

        return chance <= ((double) (show.getExpectedPopularity())  /((double) ((totalExpectedPopularity)) * 2.0)) ;
    }

    /**
     * The getter for the new position of the npc
     * @return The new position of the npc
     */
    public Point2D getNewPosition() {
        return newPosition;
    }

    /**
     * The update function for the npc
     */
    public void update() {
        if (hasArrivedAtDestination()) {
            isRoaming = true;
            roamInTargetArea();
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
        tx.scale(0.5, 0.5);

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

    public int getNegativeFeedback() {
        return negativeFeedback;
    }

    public void setSpeed(double speed) {
        this.speed = speed * this.speedMultiplier;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public void setNegativeFeedback(int negativeFeedback) {
        this.negativeFeedback = negativeFeedback;
    }

}