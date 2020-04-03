package GUILogic;

import Enumerators.Genres;
import PlannerData.Artist;
import PlannerData.Planner;
import PlannerData.Show;
import PlannerData.Stage;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalTime;
import java.util.ArrayList;

import static PlannerData.Planner.saveFileName;

/**
 * Reads the Json file and converts it into the instance of Planner
 * Also controls public accessible data for the clock and settings
 */
public class DataController {

    private Planner planner;
    private Clock clock;
    private Settings settings;

    private static DataController instance;

    public static DataController getInstance() {
        if (instance == null) {
            instance = new DataController();
        }

        return instance;
    }

    /**
     * The constructor for the data controller
     */
    private DataController() {
        settings = new Settings();
        readSettings();
        planner = new Planner();
        clock = new Clock();

        try {
            clock.setSimulatorSpeed((settings.getSimulatorSpeed()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            File file = new File(saveFileName);
            if (!file.exists()) {
                file.createNewFile();
            } else {
                try (Reader reader = new FileReader(saveFileName)) {
                    if (file.length() != 0) {
                        JsonReader jsonReader = Json.createReader(reader);
                        JsonObject plannerJsonObject = jsonReader.readObject();
                        JsonArray shows = plannerJsonObject.getJsonArray("shows");
                        JsonArray artists = plannerJsonObject.getJsonArray("artists");
                        JsonArray stages = plannerJsonObject.getJsonArray("stages");

                        for (JsonObject stage : stages.getValuesAs(JsonObject.class)) {
                            String name = stage.getString("name");
                            int capacity = stage.getInt("capacity");
                            planner.getStages().add(new Stage(capacity, name));
                        }

                        for (JsonObject artist : artists.getValuesAs(JsonObject.class)) {
                            String name = artist.getString("name");
                            String description = artist.getString("description");
                            Genres genre = Genres.getGenre(artist.getString("genre"));
                            planner.getArtists().add(new Artist(name, genre, description));
                        }

                        for (JsonObject show : shows.getValuesAs(JsonObject.class)) {
                            JsonArray showArtists = show.getJsonArray("artists");
                            JsonObject stage = show.getJsonObject("stage");

                            ArrayList<Artist> artistsInShow = new ArrayList<>();
                            for (JsonObject artist : showArtists.getValuesAs(JsonObject.class)) {
                                artistsInShow.add(new Artist(artist.getString("name"), Genres.getGenre(artist.getString("genre")), artist.getString("description")));
                            }

                            Stage stageInShow = new Stage(stage.getInt("capacity"), stage.getString("name"));
                            String name = show.getString("name");
                            Genres genre = Genres.getGenre(show.getString("genre"));
                            String description = show.getString("getShowDescription");
                            int expectedPopularity = show.getInt("expectedPopularity");
                            LocalTime beginTime = stringToLocalTime(show.getString("beginTime"));
                            LocalTime endTime = stringToLocalTime(show.getString("endTime"));
                            Show readShow = new Show(beginTime, endTime, artistsInShow, name, stageInShow, description, genre, expectedPopularity);
                            planner.getShows().add(readShow);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("error loading data due to: ");
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Was not able to gather data from " + saveFileName + " due to: ");
            e.printStackTrace();
        }
    }

    /**
     * A getter for the active shows
     *
     * @return an ArrayList containing all active shows
     */
    public ArrayList<Show> getActiveShows() {
        LocalTime currentTime = LocalTime.MIDNIGHT;
        currentTime = currentTime.plusHours(this.clock.getHours());
        currentTime = currentTime.plusMinutes(this.clock.getMinutes());
        ArrayList<Show> allShows = this.planner.getShows();
        ArrayList<Show> activeShows = new ArrayList<>();

        for (Show show : allShows) {
            if ((currentTime.equals(show.getBeginTime())) || (currentTime.isAfter(show.getBeginTime()) && currentTime.isBefore(show.getEndTime()))) {
                activeShows.add(show);
            }
        }

        return activeShows;
    }

    /**
     * The getter for the planner
     *
     * @return Planner
     */
    public Planner getPlanner() {
        return planner;
    }

    /**
     * Converts a String to a LocalTime
     *
     * @param time the string indicating a time
     * @return the time in LocalTime
     */
    private LocalTime stringToLocalTime(String time) {
        int hours = Integer.parseInt(time.charAt(0) + "") * 10 + Integer.parseInt(time.charAt(1) + "");
        int minutes = Integer.parseInt(time.charAt(3) + "") * 10 + Integer.parseInt(time.charAt(4) + "");

        LocalTime localTime = LocalTime.MIN;
        localTime = localTime.plusHours(hours);
        localTime = localTime.plusMinutes(minutes);

        return localTime;
    }

    /**
     * reads the settings file and sets the attributes of the Settings class accordingly
     */
    public void readSettings() {
        try {
            File file = new File(settings.getSaveFileName());
            if (!file.exists()) {
                file.createNewFile();
                setDefaultSettings();
            } else {
                try (Reader reader = new FileReader(settings.getSaveFileName())) {
                    if (file.length() != 0) {
                        JsonReader jsonReader = Json.createReader(reader);
                        JsonObject settingsJson = jsonReader.readObject();
                        settings.setSimulatorSpeed((Double.parseDouble(settingsJson.getString("Simulator Speed"))));
                        settings.setVisitors(settingsJson.getInt("Visitors per NPC"));
                        settings.setUsingPredictedPerson(settingsJson.getBoolean("Is Using Prediction"));
                        settings.setBeginHours(settingsJson.getInt("Begin hours"));
                        settings.setBeginMinutes(settingsJson.getInt("Begin minutes"));
                        settings.setOverwriteStartTime(settingsJson.getBoolean("Use overwrite time"));
                    } else setDefaultSettings();
                } catch (Exception e) {
                    System.out.println("Error loading data due to: ");
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            System.out.println("Was not able to gather data from " + settings.getSaveFileName() + " due to: ");
            e.printStackTrace();
        }
    }

    private void setDefaultSettings() {
        settings.setSimulatorSpeed(1);
        settings.setVisitors(100);
        settings.setUsingPredictedPerson(false);
        settings.setBeginHours(9);
        settings.setBeginMinutes(0);
        settings.setOverwriteStartTime(false);
    }

    /**
     * The getter for the clock
     *
     * @return The clock
     */
    public Clock getClock() {
        return clock;
    }

    /**
     * The getter for the settings
     *
     * @return The settings
     */
    public Settings getSettings() {
        return settings;
    }
}