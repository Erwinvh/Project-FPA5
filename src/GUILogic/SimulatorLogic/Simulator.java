package GUILogic.SimulatorLogic;

import GUILogic.Clock;
import GUILogic.DataController;
import GUILogic.SimulatorLogic.NPCLogic.PopularityTracker;
import GUILogic.Settings;
import GUILogic.SimulatorLogic.MapData.MapDataController;
import GUILogic.SimulatorLogic.NPCLogic.Person;
import PlannerData.Artist;
import PlannerData.Planner;
import PlannerData.Show;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.BorderPane;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;

public class Simulator {
    private MapDataController mapDataController;
    private ArrayList<Person> people;
    private ArrayList<Artist> artists;

    private int peopleAmount;
    private CameraTransform cameraTransform;
    private ArrayList<Integer> prediction;
    private boolean predictedGuests;

    private BorderPane simulatorLayout;
    private PopularityTracker tracker;
    private ArrayList<Show> activeShows;

    private Clock clockReference;
    private Settings settingsReference;
    private Planner plannerReference;

    /**
     * The constructor for the Simulator
     */
    public Simulator() {
        init();
        start();
    }

    /**
     * This method sets all items and attributes by initialisation
     */
    public void init() {
        clockReference = DataController.getInstance().getClock();
        settingsReference = DataController.getInstance().getSettings();
        plannerReference = DataController.getInstance().getPlanner();

        activeShows = DataController.getInstance().getActiveShows();
        tracker = new PopularityTracker();
        mapDataController = new MapDataController();
        this.people = new ArrayList<>();
        this.artists = new ArrayList<>();
        peopleAmount = settingsReference.getVisitors();
        prediction = new ArrayList<>();
        createPredictions();
        predictedGuests = settingsReference.isUsingPredictedPerson();

        ArrayList<Show> sortedShowList = DataController.getInstance().getPlanner().getShows();
        sortedShowList.sort(Show::compareToTime);

        if (sortedShowList.isEmpty() || sortedShowList.get(0) == null) return;

        LocalTime firstShowTime = sortedShowList.get(0).getBeginTime();

        if (firstShowTime != null && !settingsReference.isOverwriteStartTime()) {
            if (firstShowTime.getHour() != 0) {
                clockReference.setTime(firstShowTime.getHour() - 1, firstShowTime.getMinute(), firstShowTime.getSecond());
            } else if (firstShowTime.getMinute() == 30) {
                clockReference.setTime(firstShowTime.getHour(), firstShowTime.getMinute() - 30, firstShowTime.getSecond());
            } else {
                clockReference.setTime(firstShowTime.getHour(), firstShowTime.getMinute(), firstShowTime.getSecond());
            }
        } else {
            if (settingsReference.getBeginHours() != Integer.MIN_VALUE && settingsReference.getBeginMinutes() != Integer.MIN_VALUE) {
                clockReference.setTime(settingsReference.getBeginHours(), settingsReference.getBeginMinutes(), 0);
            } else {
                clockReference.setToMidnight();
            }
        }
    }

    /**
     * This method starts the simulator
     */
    private void start() {
        this.simulatorLayout = new BorderPane();
        ResizableCanvas canvas = new ResizableCanvas(this::draw, this.simulatorLayout);
        this.simulatorLayout.setCenter(canvas);

        FXGraphics2D graphics = new FXGraphics2D(canvas.getGraphicsContext2D());
        this.cameraTransform = new CameraTransform(canvas);
        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1) last = now;

                update((now - last) / 1000000000.0);
                last = now;
                draw(graphics);
            }
        }.start();

        draw(graphics);
    }

    /**
     * updates the persons and sets their speed relative to the time passed
     *
     * @param deltaTime time passed in seconds
     */
    private void update(double deltaTime) {
        clockReference.update(deltaTime);

        if (clockReference.isIntervalPassed()) {
            pulse();
        }

        double speed = clockReference.getSimulatorSpeed() * 10;

        if (artists.size() < plannerReference.getArtists().size()) {
            peopleAmount++;
            artists = plannerReference.getArtists();
        }

        if (people.size() < peopleAmount)
            spawnPerson();

        for (Person person : people) {
            person.setSpeed(speed * deltaTime);
            person.update(people);
        }
    }

    /**
     * Spawns on either of the 2 spawn locations, randomly chosen.
     */
    private void spawnPerson() {
        Point2D spawnLocation1 = new Point2D.Double(2 * 32, 20 * 32);
        Point2D spawnLocation2 = new Point2D.Double(31 * 32, 97 * 32);

        Random r = new Random();
        if (r.nextBoolean()) {
            spawnOnLocation(spawnLocation1);
        } else {
            spawnOnLocation(spawnLocation2);
        }
    }

    /**
     * Spawns a person, if all the artists are spawned then spawning visitors
     *
     * @param p2d spawnLocation
     */
    private void spawnOnLocation(Point2D p2d) {
        if (canSpawn(p2d)) {
            //loop trough all the artists to see if they are spawned already
            for (Artist artist : plannerReference.getArtists()) {
                boolean hasBeenSpawned = false;
                for (Person person : people) {
                    if (person.getName() != null)
                        if (person.getName().equals(artist.getName()))
                            hasBeenSpawned = true;
                }

                if (!hasBeenSpawned) {
                    Person newPerson = new Person(new Point2D.Double(p2d.getX(), p2d.getY()), this.prediction, artist.getName(), clockReference.getSimulatorSpeed(), true);
                    this.people.add(newPerson);
                    newPerson.getPersonLogic().selectNewMap(this.activeShows, this.tracker);
                    newPerson.getPersonLogic().setNextTarget();
                    return;
                }
            }

            //if all the artists have been spawned then we spawn visitors
            Person newPerson = new Person(new Point2D.Double(p2d.getX(),
                    p2d.getY()), this.prediction, clockReference.getSimulatorSpeed(), false);
            this.people.add(newPerson);
            newPerson.getPersonLogic().selectNewMap(this.activeShows, this.tracker);
            newPerson.getPersonLogic().setNextTarget();
        }
    }

    /**
     * A method that checks if a spot is not occupied by another person
     *
     * @param spawnPosition the location to check if it's available
     * @return true if empty, false if occupied
     */
    private boolean canSpawn(Point2D spawnPosition) {
        for (Person person : people) {
            if (spawnPosition.distance(person.getPersonLogic().getPosition()) <= 64) {
                return false;
            }
        }

        return true;
    }

    /**
     * This method creates the prediction of the type of guests that will visit the festival
     */
    private void createPredictions() {
        int total = 6;
        int metal = 1;
        int country = 1;
        int classic = 1;
        int rap = 1;
        int pop = 1;
        int electro = 1;

        if (this.predictedGuests) {
            for (Show show : plannerReference.getShows()) {
                String showGenre = show.getGenre().getSuperGenre();
                switch (showGenre) {
                    case "Metal":
                        metal++;
                        break;
                    case "Country":
                        country++;
                        break;
                    case "Classic":
                        classic++;
                        break;
                    case "Electro":
                        electro++;
                        break;
                    case "Rap":
                        rap++;
                        break;
                    case "Pop":
                        pop++;
                        break;
                }

                total++;
            }
        }

        this.prediction.add(metal);
        this.prediction.add(classic);
        this.prediction.add(country);
        this.prediction.add(rap);
        this.prediction.add(pop);
        this.prediction.add(electro);
        this.prediction.add(total);
    }

    /**
     * This method draws the items on the simulator
     *
     * @param g the FXGraphics2D instance which is used to draw everything
     */
    private void draw(FXGraphics2D g) {
        //Gets inverseTransform from cameraTransform so the correct rectangle can be cleared.
        AffineTransform inverse = this.cameraTransform.getInverseTransform();

        g.clearRect(
                (int) (inverse.getTranslateX()),
                (int) (inverse.getTranslateY()),
                (int) (MapDataController.getMapWidth() * 16 * inverse.getScaleX()),
                (int) (MapDataController.getMapHeight() * 8 * inverse.getScaleY())
        );

        g.setTransform(this.cameraTransform.getTransform());
        g.setBackground(Color.black);

        // draws map dependent on time, day or night
        MapDataController.draw(g);
        double timeHours;
        timeHours = clockReference.getHours();
        timeHours += clockReference.getMinutes() / 60.0;

        float opacity;

        if (timeHours >= 14) {
            opacity = (float) ((2.0f / 3.0f) * Math.pow((timeHours - 4), 2) - (float) (38 / 3) * (float) (timeHours - 4) + 55) / 100.0f;
        } else {
            opacity = (float) ((25.0f / 84.0f) * Math.pow(timeHours, 2) - (float) (355 / 42) * (float) timeHours + 60) / 100.0f;
        }

        if (opacity < 0) {
            opacity = 0f;
        } else if (opacity > 0.7f) {
            opacity = 0.7f;
        }

        for (Person person : people)
            person.draw(g);

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g.drawImage(MapDataController.getNightLayerImage(), 0, 0, null);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        g.setTransform(new AffineTransform());
        String time = clockReference.toString();
        Font font = new Font("Arial", Font.PLAIN, 30);
        Shape timeShape = font.createGlyphVector(g.getFontRenderContext(), time).getOutline();
        timeShape = AffineTransform.getTranslateInstance(0, 30).createTransformedShape(timeShape);

        g.setColor(Color.BLACK);
        g.fill(timeShape);
        g.setColor(Color.WHITE);
        g.draw(timeShape);

        g.setTransform(this.cameraTransform.getTransform());
    }

    /**
     * updates the new target of all people
     */
    private void pulse() {
        activeShows = DataController.getInstance().getActiveShows();

        PopularityTracker tracker = new PopularityTracker();
        for (Person person : people) {
            person.getPersonLogic().selectNewMap(activeShows, tracker);
        }
    }

    /**
     * The getter for the simulator layout
     *
     * @return The BorderPane in which the simulator is placed
     */
    public BorderPane getSimulatorLayout() {
        return simulatorLayout;
    }
}