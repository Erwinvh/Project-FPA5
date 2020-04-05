package GUILogic.SimulatorLogic.NPCLogic;

import GUILogic.DataController;
import GUILogic.SimulatorLogic.MapData.MapDataController;
import GUILogic.SimulatorLogic.MapData.TargetArea;
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
    private Person person;
    private Point2D newPosition;
    private double speedMultiplier;

    private boolean isRoaming = false;

    /**
     * The constructor for the npc logic
     *
     * @param position the position of the npc
     * @param speed    the speed of the npc
     * @param person   the person object connected to the npc
     */
    PersonLogic(Point2D position, double speed, Person person) {
        Random random = new Random();
        this.speedMultiplier = ((120.0 - random.nextInt(40)) / 100);

        this.position = position;
        this.person = person;
        this.angle = 0;
        this.speed = speed * speedMultiplier;
        this.rotationSpeed = 100;
        this.target = null;
    }

    /**
     * checks if the Person has arrived at the target
     *
     * @return true or false depending on distance between target and current position
     */
    private boolean hasArrivedAtTarget() {
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
     *
     * @param activeShows a list of shows being performed on the current time of the Clock
     */
    public void selectNewMap(ArrayList<Show> activeShows, PopularityTracker tracker) {
        this.isRoaming = false;
        Collections.sort(activeShows);
        int totalExpectedPopularity = 0;
        for (Show show : activeShows) {
            totalExpectedPopularity += show.getExpectedPopularity();
        }
        for (Show show : activeShows) {
            if (isGoingToShow(show, totalExpectedPopularity, tracker)) {
                DistanceMap targetMap = getDistanceMap(show.getStage(), person.isArtist());
                if (targetMap != null) {
                    this.distanceMap = targetMap;
                    return;
                }
            }
        }

        ArrayList<TargetArea> idleAreas = MapDataController.getIdleTargetAreas();
        int idleAreaIndex = (int) (Math.random() * idleAreas.size());
        this.distanceMap = MapDataController.getDistanceMap(idleAreas.get(idleAreaIndex));
    }

    /**
     * Gets the distanceMap of a stage depending if the person is an artist
     * If the person is an artist the distance map will be located on the stage itself
     * If the person is not an artist the distance map destination will be the viewing area
     *
     * @param wantedStage The desired stage
     * @param isArtist    True if the person is an artists
     * @return The correct DistanceMap of the stage
     */
    private DistanceMap getDistanceMap(Stage wantedStage, boolean isArtist) {
        ArrayList<Stage> stages = DataController.getInstance().getPlanner().getStages();
        Stage searchingStage = null;
        for (Stage stage : stages) {
            if (stage.getName().equals(wantedStage.getName())) {
                searchingStage = stage;
            }
        }

        TargetArea[] targetAreas = MapDataController.getTargetAreas();
        int stageIndex = stages.indexOf(searchingStage);
        for (TargetArea targetArea : targetAreas) {
            if (isArtist) {
                if (targetArea.getName().equals("ArtistStage" + (stageIndex + 1))) {
                    if (targetArea.getTargetAreaType() == TargetArea.TargetAreaType.ARTIST) {
                        return MapDataController.getDistanceMap(targetArea);
                    }
                }
            } else {
                if (targetArea.getName().equals("VisitorStage" + (stageIndex + 1))) {
                    if (targetArea.getTargetAreaType() == TargetArea.TargetAreaType.VISITOR) {
                        return MapDataController.getDistanceMap(targetArea);
                    }
                }
            }
        }

        return null;
    }

    /**
     * Determines if a person is going to a show
     *
     * @param show the show the person is deciding to go to
     * @return true if the person wants to go to the show
     */
    private boolean isGoingToShow(Show show, int totalExpectedPopularity, PopularityTracker tracker) {
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
            if (tracker.canGoToShow(show)) {
                return chance <= ((show.getExpectedPopularity() * 3.0)) / ((double) totalExpectedPopularity);
            } else return false;
        }

        if (tracker.canGoToShow(show)) {
            return chance <= ((double) (show.getExpectedPopularity()) / ((double) ((totalExpectedPopularity)) * 2.0));
        } else return false;
    }

    /**
     * The getter for the new position of the npc
     *
     * @return The new position of the npc
     */
    Point2D getNewPosition() {
        return newPosition;
    }

    /**
     * The update function for the npc
     */
    void update() {
        //check if the person is out of bound and if he is he is set to despawn
        if (position.getX()<0||position.getX()>MapDataController.getMapWidth()*MapDataController.getTileSize()||
            position.getY()<0||position.getY()>MapDataController.getMapHeight()*MapDataController.getTileSize()){
                person.setDespawn(true);
        }

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

    /**
     * The getter for the person position
     *
     * @return the person position
     */
    public Point2D getPosition() {
        return position;
    }

    /**
     * The getter for the affine transform of the person
     *
     * @return the affine transform
     */
    AffineTransform getTransform() {
        AffineTransform tx = new AffineTransform();

        tx.translate(position.getX() - this.person.getSprite().getWidth() * 0.5, position.getY() - this.person.getSprite().getHeight() * 0.5);
        tx.scale(0.5, 0.5);
        tx.rotate(this.angle, this.person.getSprite().getWidth() * 0.5, this.person.getSprite().getHeight() * 0.5);

        return tx;
    }

    /**
     * The setter for the target of the person
     *
     * @param target the new target position for the NPC
     */
    void setTarget(Point2D target) {
        this.target = target;
    }

    /**
     * The setter of the speed of the person
     *
     * @param speed the new speed for the NPC
     */
    void setSpeed(double speed) {
        this.speed = speed * this.speedMultiplier;
    }

    /**
     * The setter of the position of the person
     *
     * @param position the new position for the NPC
     */
   public void setPosition(Point2D position) {
        this.position = position;
    }
}