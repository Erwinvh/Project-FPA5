package GUILogic.SimulatorLogic;

import GUILogic.DataController;
import GUILogic.SimulatorLogic.MapData.MapDataController;
import NPCLogic.Person;
import PlannerData.Artist;
import PlannerData.Show;
import javafx.animation.AnimationTimer;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.time.LocalTime;
import java.util.ArrayList;

public class Simulator {
    private MapDataController mapDataController;
    private ResizableCanvas canvas;
    private ArrayList<Person> people;
    private ArrayList<Artist> artists;

    private int peopleAmount = 100;
    private int globalSpeed = 4;
    private CameraTransform cameraTransform;
    private boolean predictedGuests = DataController.getSettings().isUsingPredictedPerson();
    private ArrayList<Integer> Prediction = new ArrayList<>();

    private BorderPane simulatorLayout;

    /**
     * The constructor for the Simulator
     */
    public Simulator() {
        init();
        start();
    }

    /**
     * The getter for the simulator layout
     * @return The borderpane in which the simulator is placed
     */
    public BorderPane getSimulatorLayout() {
        return simulatorLayout;
    }

    /**
     * This methode sets all items and attributes by initilisation
     */
    public void init() {
        mapDataController = new MapDataController();
        this.people = new ArrayList<>();
        this.artists = new ArrayList<>();

        LocalTime firstShow = DataController.getPlanner().getShows().get(0).getBeginTime();
        if (firstShow != null){
            if (firstShow.getHour() != 0){
                DataController.getClock().setTime(firstShow.getHour() - 1, firstShow.getMinute(), firstShow.getSecond());
            } else if (firstShow.getMinute() == 30){
                DataController.getClock().setTime(firstShow.getHour(), firstShow.getMinute() - 30, firstShow.getSecond());
            } else {
                DataController.getClock().setTime(firstShow.getHour(), firstShow.getMinute(), firstShow.getSecond());
            }
        } else {
            DataController.getClock().setToMidnight();
        }


        createPredictions();
    }

    /**
     * This methode starts the simulator
     */
    public void start() {
        this.simulatorLayout = new BorderPane();
        canvas = new ResizableCanvas(this::draw, this.simulatorLayout);
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

        canvas.setOnMouseClicked(this::onMousePressed);

        draw(graphics);
    }

    /**
     * updates the persons and sets their speed relative to the time passed
     *
     * @param deltaTime time passed in seconds
     */
    public void update(double deltaTime) {
        DataController.getClock().update(deltaTime);



        if(DataController.getClock().isIntervalPassed()){
            pulse();
        }
        double speed = DataController.getClock().getSimulatorSpeed() * 10;

        if (artists.size() < DataController.getPlanner().getArtists().size()) {
            peopleAmount++;
            artists = DataController.getPlanner().getArtists();
        }

        if (people.size() < peopleAmount)
            spawnPerson();

        for (Person person : people) {
            person.setSpeed(speed * deltaTime);
            person.update(people);
        }
    }

    /**
     * Spawns a person, if all the artists are spawned then spawning visitors
     */
    public void spawnPerson() {
        Point2D newSpawnLocation = new Point2D.Double(2 * 32, 20 * 32);

        if (canSpawn(newSpawnLocation)) {
            //loop trough all the artists to see if they are spawned already
            for (Artist artist : DataController.getPlanner().getArtists()) {
                boolean hasBeenSpawned = false;
                for (Person person : people) {
                    if (person.getName() != null)
                        if (person.getName().equals(artist.getName()))
                            hasBeenSpawned = true;
                }

                if (!hasBeenSpawned) {
                    this.people.add(new Person(new Point2D.Double(newSpawnLocation.getX(), newSpawnLocation.getY()), this.Prediction, artist.getName(), DataController.getClock().getSimulatorSpeed(), true));
                    return;
                }
            }

            //if all the artists have been spawned then we spawn visitors
            this.people.add(new Person(new Point2D.Double(newSpawnLocation.getX(),
                    newSpawnLocation.getY()), this.Prediction, DataController.getClock().getSimulatorSpeed(), false));
        }
    }

    /**
     * A method that checks if a spot is not occupied by another person
     *
     * @param spawnPosition the location to check if it's available
     * @return true if empty, false if occupied
     */
    public boolean canSpawn(Point2D spawnPosition) {
        for (Person person : people) {
            if (spawnPosition.distance(person.getPersonLogic().getPosition()) <= 64) {
                return false;
            }
        }

        return true;
    }

    /**
     * This methode handles the mouse event when its pressed
     * @param e the mouse event
     */
    public void onMousePressed(MouseEvent e) {
        for (Person person : this.people) {
            if (person.getPersonLogic().getPosition().distance(new Point2D.Double(e.getX(), e.getY())) < 32) {
                person.playSoundEffect();
            }
        }
    }

    /**
     * This methode creates the prediction of the type of guests that will visit the festival
     */
    public void createPredictions() {
        int Total = 6;
        int metal = 1;
        int Country = 1;
        int classic = 1;
        int Rap = 1;
        int Pop = 1;
        int electro = 1;

        if (this.predictedGuests) {
            for (Show show : DataController.getPlanner().getShows()) {
                String showgenre = show.getGenre().getSuperGenre();
                switch (showgenre) {
                    case "Metal":
                        metal++;
                        break;
                    case "Country":
                        Country++;
                        break;
                    case "Classic":
                        classic++;
                        break;
                    case "Electro":
                        electro++;
                        break;
                    case "Rap":
                        Rap++;
                        break;
                    case "Pop":
                        Pop++;
                        break;
                }
                Total++;
            }
        }

        this.Prediction.add(metal);
        this.Prediction.add(classic);
        this.Prediction.add(Country);
        this.Prediction.add(Rap);
        this.Prediction.add(Pop);
        this.Prediction.add(electro);
        this.Prediction.add(Total);
    }

    /**
     * This methode draws the items on the simulator
     * @param g
     */
    public void draw(FXGraphics2D g) {
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

        mapDataController.draw(g);

        for (Person person : people)
            person.draw(g);

        g.setTransform(new AffineTransform());
        String time = DataController.getClock().toString();
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
     * This is the setter for the amount of people per NPC
     * @param peopleAmount Amount of people per npc
     */
    public void setPeopleAmount(int peopleAmount) {
        this.peopleAmount = peopleAmount;
    }

    /**
     * The getter for the amount of people per npc
     * @return The amount of people amount
     */
    public int getPeopleAmount() {
        return peopleAmount;
    }

    /**
     *
     * @param predictedGuests
     */
    public void setPredictedGuests(boolean predictedGuests) {
        this.predictedGuests = predictedGuests;
    }

    public void pulse() {
        ArrayList<Show> currentShows = DataController.getActiveShows();
        for(Person person: people){
            person.getPersonLogic().selectNewMap(currentShows);
        }
    }
}