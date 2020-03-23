package GUILogic.SimulatorLogic;

import GUILogic.DataController;
import GUILogic.SimulatorLogic.MapData.MapDataController;
import GUILogic.SimulatorLogic.NPCLogic.Person;
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
import java.util.ArrayList;

public class Simulator {
    private MapDataController mapDataController;
    private ResizableCanvas canvas;
    private ArrayList<GUILogic.SimulatorLogic.NPCLogic.Person> people;

    private int peopleAmount = 30;
    private int stageAmount = 6;
    private int toiletAmount = 20;
    private int globalSpeed = 4;
    private CameraTransform cameraTransform;
    private boolean predictedGuests = true;
    private ArrayList<Integer> Prediction = new ArrayList<>();

    private BorderPane simulatorLayout;

    public Simulator() {
        init();
        start();
    }

    public BorderPane getSimulatorLayout() {
        return simulatorLayout;
    }

//    public static void main(String[] args) {
////        launch(Simulator.class);
//
//        DistanceMap distanceMap = MapDataController.getDistanceMap(MapDataController.getTargetAreas()[0]);
//        for (int i = 0; i < 100; i++) {
//            for (int j = 0; j < 100; j++) {
//                System.out.println(distanceMap.getMap()[i][j]);
//            }
//        }
//    }

    public void init() {
        mapDataController = new MapDataController();
        this.people = new ArrayList<>();

        createPredictions();
        spawnPeople(peopleAmount);
    }

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

        canvas.setOnMouseClicked(e -> {
            clickAction(e);
//            if (e.getButton() == MouseButton.SECONDARY){
//                this.showNull = !this.showNull;
//                if (this.showNull){
//                    System.out.println("Shows: Non CameraTransformed");
//                } else {
//                    System.out.println("Shows: CameraTransformed");
//                }
//            } else

            if (e.getButton() == MouseButton.PRIMARY) {
                this.init();
            }
        });

//        stage.setScene(new Scene(this.simulatorLayout));
//        stage.setTitle("A5 FP");
//        stage.show();

        draw(graphics);
    }

    public void update(double frameTime) {
        for (GUILogic.SimulatorLogic.NPCLogic.Person person : people) {
            person.update(people);
        }
    }

    /**
     * Spawns a amount of people, stops spawning after 10% failed spawnAttempts of the amount
     *
     * @param amount the amount of people to be spawned
     */
    public void spawnPeople(int amount) {
        int failedSpawnAttempts = 0;
        for (Artist artist : DataController.getPlanner().getArtists()) {
            Point2D newSpawnLocation = new Point2D.Double(Math.random() * 100 * 32, Math.random() * 100 * 32);
            this.people.add(new Person(new Point2D.Double(newSpawnLocation.getX(), newSpawnLocation.getY()), this.Prediction, artist.getName(), this.globalSpeed, true));
        }

        for (int i = 0; i < amount; i++) {
            Point2D newSpawnLocation = new Point2D.Double(Math.random() * 100 * 32, Math.random() * 100 * 32);
            if (canSpawn(newSpawnLocation)) {
                this.people.add(new Person(new Point2D.Double(newSpawnLocation.getX(),
                        newSpawnLocation.getY()), this.Prediction, this.globalSpeed, false));
                failedSpawnAttempts = 0;
            } else {
                failedSpawnAttempts++;
                i--;
                if (failedSpawnAttempts > amount * 0.1) {
                    return;
                }
            }
        }
    }

    /**
     * A method that checks if a spot is not occupied by another person
     *
     * @param spawnPosition the location to check if it's available
     * @return true if empty, false if occupied
     */
    public boolean canSpawn(Point2D spawnPosition) {
        if (this.people.size() <= 0) {
            return true;
        }

        for (Person person : people) {
            if (spawnPosition.distance(person.getPersonLogic().getPosition()) <= 64) {
                return false;
            }
        }

        return true;
    }

    public void clickAction(MouseEvent e) {
        for (Person person : this.people) {
            if (person.getPersonLogic().getPosition().distance(new Point2D.Double(e.getX(), e.getY())) < 32) {
                person.playSoundEffect();
            }
        }
    }

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
                String showGenre = show.getGenre().getSuperGenre();
                switch (showGenre) {
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

    public void draw(FXGraphics2D g) {
        //Gets inverseTransform from cameraTransform so the correct rectangle can be cleared.
        AffineTransform inverse = this.cameraTransform.getInverseTransform();
        g.clearRect(
                (int) inverse.getTranslateX(),
                (int) inverse.getTranslateY(),
                (int) (inverse.getScaleX() * this.canvas.getWidth() - inverse.getTranslateX()),
                (int) (inverse.getScaleY() * this.canvas.getHeight() - inverse.getTranslateY())
        );

        g.setTransform(this.cameraTransform.getTransform());
        g.setBackground(Color.black);
        g.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        mapDataController.draw(g);

        for (Person person : people) {
            person.draw(g);
        }
    }

    public void setPeopleAmount(int peopleAmount) {
        this.peopleAmount = peopleAmount;
    }

    public int getGlobalSpeed() {
        return globalSpeed;
    }

    public int getPeopleAmount() {
        return peopleAmount;
    }

    public void setGlobalSpeed(int globalSpeed) {
        this.globalSpeed = globalSpeed;
    }

    public void setPredictedGuests(boolean predictedGuests) {
        this.predictedGuests = predictedGuests;
    }
}