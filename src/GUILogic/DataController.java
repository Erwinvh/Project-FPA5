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
import javax.management.Descriptor;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalTime;
import java.util.ArrayList;

import static PlannerData.Planner.saveFileName;

public class DataController {

    private static Planner planner;

    public DataController() {
        planner = new Planner();

        try {
            File file = new File(saveFileName);
            if (!file.exists()) {
                file.createNewFile();
            } else {
                try (Reader reader = new FileReader(saveFileName)) {
                    if (file.length() != 0){
                        JsonReader jsonReader = Json.createReader(reader);
                        JsonObject planner = jsonReader.readObject();
                        JsonArray shows = planner.getJsonArray("shows");
                        JsonArray artists = planner.getJsonArray("artists");
                        JsonArray stages = planner.getJsonArray("stages");
                        for(JsonObject stage : stages.getValuesAs(JsonObject.class)){
                            String name = stage.getString("name");
                            int capacity = stage.getInt("capacity");
                            this.planner.getStages().add(new Stage( capacity,name));
                        }
                        for(JsonObject artist : artists.getValuesAs(JsonObject.class)){
                            String name = artist.getString("name");
                            String description = artist.getString("description");
                            Genres genre = stringToGenre(artist.getString("genre"));
                            this.planner.getArtists().add(new Artist(name, genre, description));
                        }
                        for(JsonObject show : shows.getValuesAs(JsonObject.class)){
                            JsonArray showArtists = show.getJsonArray("artists");
                            JsonObject stage = show.getJsonObject("stage");

                            ArrayList<Artist> artistsInShow = new ArrayList<>();
                            for(JsonObject artist : showArtists.getValuesAs(JsonObject.class)){
                                artistsInShow.add(new Artist(artist.getString("name"),stringToGenre(artist.getString("genre")),artist.getString("description")));
                            }

                            Stage stageInShow = new Stage(stage.getInt("capacity"),stage.getString("name"));
                            String name = show.getString("name");
                            Genres genre = stringToGenre( show.getString("genre"));
                            String description = show.getString("description");
                            int expectedPopularity = show.getInt("expectedPopularity");
                            ArrayList<Genres> genres =  new ArrayList<>();
                            genres.add(genre);
                            LocalTime beginTime = stringToLocalTime(show.getString("beginTime"));
                            LocalTime endTime = stringToLocalTime(show.getString("endTime"));
                            Show readShow = new Show(beginTime,endTime,artistsInShow,name,stageInShow,description,genres,expectedPopularity);
                            this.planner.getShows().add(readShow);
                        }
                    }
                    else{

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

//        Artist lars = new Artist("Lars", Genres.BLUES, "Gekke man");
//        Stage stage = new Stage(100, "Main stage");
//        ArrayList<Genres> genres = new ArrayList<>();
//        genres.add(Genres.DANCE);
//        genres.add(Genres.NIGHTCORE);
//        planner.addShow(new Show(LocalTime.now(), LocalTime.now().plusHours(1), lars, "De ochtendshow van Lars", stage, "Lars die jamt", genres, 100000));
//        planner.savePlanner();

//        ArrayList<Artist> artists = new ArrayList<>();
//        artists.add(new Artist("Arne de Beer", Genres.BLUES, "Smoking hot"));
//        artists.add(new Artist("Lars Giskes", Genres.PUNK_ROCK, "The legend of Spoderman"));
//        artists.add(new Artist("Henk", Genres.METAL, "Dit is Henk"));
//
//        ArrayList<Stage> stages = new ArrayList<>();
//        stages.add(new Stage(500, "Main Stage"));
//        stages.add(new Stage(100, "Second Stage"));
//
//        planner.addShow(new Show(LocalTime.now(), LocalTime.now().plusMinutes(30), stages.get(0), artists.get(0), 400));
//        planner.addShow(new Show(LocalTime.now().plusMinutes(45), LocalTime.now().plusHours(2), stages.get(0), artists.get(1), 400));
//        planner.addShow(new Show(LocalTime.now(), LocalTime.now().plusMinutes(30), stages.get(1), artists.get(2), 75));
    }

    static Planner getPlanner() {
        return planner;
    }

    private LocalTime stringToLocalTime(String time){
        int hours = Integer.parseInt( time.charAt(0) + "") * 10 + Integer.parseInt( time.charAt(1)+"");
        int minutes = Integer.parseInt( time.charAt(3) + "") * 10 + Integer.parseInt( time.charAt(4)+"");
        LocalTime localTime = LocalTime.MIN;
        localTime = localTime.plusHours(hours);
        localTime = localTime.plusMinutes(minutes);
        return  localTime;
    }

    private Genres stringToGenre(String text){
        for(Genres genre : Genres.values()){
            if( text.equals(genre.getFancyName())){
                return genre;
            }
        }
        return null;
    }
}