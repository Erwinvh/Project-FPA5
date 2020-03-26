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
 */
public class DataController {

    private static Planner planner;
    private static Clock clock;
    private static Settings settings;

    /**
     * Reads the Json file and creates an instance of Planner
     */
    public DataController() {
        planner = new Planner();
        clock = new Clock();
        settings = new Settings();

        readSettings();
        try {
            clock.setSimulatorSpeed(Math.round( clock.getSimulatorSpeed() * settings.getSimulatorSpeed()));
        }
        catch (Exception e){
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
                        JsonObject planner = jsonReader.readObject();
                        JsonArray shows = planner.getJsonArray("shows");
                        JsonArray artists = planner.getJsonArray("artists");
                        JsonArray stages = planner.getJsonArray("stages");
                        for (JsonObject stage : stages.getValuesAs(JsonObject.class)) {
                            String name = stage.getString("name");
                            int capacity = stage.getInt("capacity");
                            this.planner.getStages().add(new Stage(capacity, name));
                        }
                        for (JsonObject artist : artists.getValuesAs(JsonObject.class)) {
                            String name = artist.getString("name");
                            String description = artist.getString("description");
                            Genres genre = stringToGenre(artist.getString("genre"));
                            this.planner.getArtists().add(new Artist(name, genre, description));
                        }
                        for (JsonObject show : shows.getValuesAs(JsonObject.class)) {
                            JsonArray showArtists = show.getJsonArray("artists");
                            JsonObject stage = show.getJsonObject("stage");

                            ArrayList<Artist> artistsInShow = new ArrayList<>();
                            for (JsonObject artist : showArtists.getValuesAs(JsonObject.class)) {
                                artistsInShow.add(new Artist(artist.getString("name"), stringToGenre(artist.getString("genre")), artist.getString("description")));
                            }

                            Stage stageInShow = new Stage(stage.getInt("capacity"), stage.getString("name"));
                            String name = show.getString("name");
                            Genres genre = stringToGenre(show.getString("genre"));
                            String description = show.getString("getShowDescription");
                            int expectedPopularity = show.getInt("expectedPopularity");
                            LocalTime beginTime = stringToLocalTime(show.getString("beginTime"));
                            LocalTime endTime = stringToLocalTime(show.getString("endTime"));
                            Show readShow = new Show(beginTime, endTime, artistsInShow, name, stageInShow, description, genre, expectedPopularity);
                            this.planner.getShows().add(readShow);
                        }
                    } else {

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

    public static ArrayList<Show> getActiveShows() {
        LocalTime currentTime = LocalTime.MIDNIGHT;
        currentTime = currentTime.plusHours(DataController.getClock().getHours());
        currentTime = currentTime.plusMinutes(DataController.getClock().getMinutes());
        ArrayList<Show> allShows = getPlanner().getShows();
        ArrayList<Show> activeShows = new ArrayList<>();

        for (Show show : allShows) {
            if ((currentTime.equals(show.getBeginTime())) || (currentTime.isAfter(show.getBeginTime()) && currentTime.isBefore(show.getEndTime()))) {
                activeShows.add(show);
            }
        }

        return activeShows;
    }

    public static Planner getPlanner() {
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
     * Converts a String to a Fancy name Genre
     *
     * @param text the String value of a Genre
     * @return the genre in the instance of Genres
     */
    private Genres stringToGenre(String text) {
        for (Genres genre : Genres.values()) {
            if (text.equals(genre.getFancyName())) {
                return genre;
            }
        }
        return null;
    }

    public void readSettings(){
        try {
            File file = new File(this.settings.getSaveFileName());
            if (!file.exists()) {
                file.createNewFile();
            } else {
                try (Reader reader = new FileReader(this.settings.getSaveFileName())) {
                    if (file.length() != 0) {
                        JsonReader jsonReader = Json.createReader(reader);
                        JsonObject settings = jsonReader.readObject();
                        this.settings.setSimulatorSpeed((Double.parseDouble(settings.getString("Simulator Speed"))));
                        this.settings.setVisitorsPerPerson(settings.getInt("Vistors per NPC"));
                        this.settings.setUsingPredictedPerson(settings.getBoolean("Is Using Prediction"));
                    }
                } catch (Exception e) {
                    System.out.println("error loading data due to: ");
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            System.out.println("Was not able to gather data from " + this.settings.getSaveFileName() + " due to: ");
            e.printStackTrace();
        }

    }

    public static Clock getClock() {
        return clock;
    }

    public static Settings getSettings(){
        return settings;
    }
}