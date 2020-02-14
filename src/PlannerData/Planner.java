package PlannerData;

import Enumerators.Genres;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;

public class Planner implements Serializable {

    private ArrayList<Show> shows;
    private ArrayList<Stage> stages;
    private ArrayList<Artist> artists;
//    private ArrayList<Genres> genres;

    public final static String saveFileName = "Resources/saveFile.json";

    public Planner() {
        this.shows = new ArrayList<>();
        this.stages = new ArrayList<>();
        this.artists = new ArrayList<>();
//        this.genres = new ArrayList<>();
    }

    /**
     * method to add shows
     *
     * @param show object where data about the show is stored
     */
    public void addShow(Show show) {
        this.shows.add(show);
        this.savePlanner();

    }

    /**
     * overloaded function to add a list of shows
     *
     * @param shows array list of shows which are objects where data about the show is stored
     */
    public void addShow(ArrayList<Show> shows) {
        for (Show show : shows) {
            addShow(show);
        }
    }

    /**
     * overloaded function to create and add a show
     *
     * @param beginTime          the time the show begins
     * @param endTime            the time the show ends
     * @param artists            an array list of all the artists that contribute to the show
     * @param name               name of the show
     * @param stage              Which stage the show is performed
     * @param description        special information
     * @param genre              genre of the music performed
     * @param expectedPopularity how many visitors are expected
     */
    public void addShow(LocalTime beginTime, LocalTime endTime, ArrayList<Artist> artists, String name, Stage stage, String description, ArrayList<Genres> genre, int expectedPopularity) {
        addShow(new Show(beginTime, endTime, artists, name, stage, description, genre, expectedPopularity));
    }

    public void addShow(LocalTime beginTime, LocalTime endTime, Stage stage, int popularity, Genres genre, ArrayList<Artist> artists) {
        Show newShow = new Show(beginTime, endTime, stage, artists, "", "", genre, popularity);

        if (this.shows.contains(newShow)) return;

        for (Show show : this.shows) {
            if (show.getStage().equals(stage)) {
                if (beginTime.isAfter(show.getBeginTime()) && beginTime.isBefore(show.getEndTime())) {
                    return;
                }

                if (endTime.isAfter(show.getBeginTime()) && endTime.isBefore(show.getEndTime())) {
                    return;
                }
            }
        }

        this.shows.add(newShow);
        this.savePlanner();
    }

//    public void addArtist(String name, Genres genre, Image image, String description) {
//        for (Artist existingArtist : this.artists) {
//            if (name.equals(existingArtist.getName())) {
//                return;
//            }
//        }
//
//        this.artists.add(new Artist(name, genre, image, description));
//        this.savePlanner();
//    }

    public void addArtist(String name, Genres genre, String imagePath, String description) {
        for (Artist existingArtist : this.artists) {
            if (name.equals(existingArtist.getName())) {
                return;
            }
        }

        this.artists.add(new Artist(name, genre, description));
        this.savePlanner();
    }

    public void addArtist(String name, Genres genre, String description) {
        for (Artist existingArtist : this.artists) {
            if (name.equals(existingArtist.getName())) return;
        }

        this.artists.add(new Artist(name, genre, description));
        this.savePlanner();
    }

    public void addStage(int capacity, String name) {

        if (capacity < 1 || capacity > 100000)
            return;

        for (Stage stage : this.stages) {
            if (stage.getName().toLowerCase().equals(name.toLowerCase()))
                return;
        }

        this.stages.add(new Stage(capacity, name));
        this.savePlanner();
    }
    public void addStage(Stage stage) {
        this.stages.add(stage);
        this.savePlanner();
    }

    public boolean deleteShow(Show show) {
        return this.shows.remove(show);
    }

    public boolean deleteShow(String showName) {
        for (Show show : this.shows) {
            if (show.getName().equals(showName)) {
                return deleteShow(show);
            }
        }

        return false;
    }

    public boolean deleteArtist(Artist artist) {
        return this.artists.remove(artist);
    }

    public boolean deleteArtist(String artistName) {
        for (Artist artist : this.artists) {
            if (artist.getName().equals(artistName)) {
                return deleteArtist(artist);
            }
        }

        return false;
    }

    public boolean deleteStage(Stage stage) {
        return this.stages.remove(stage);
    }

    public boolean deleteStage(String stageName) {
        for (Stage stage : this.stages) {
            if (stage.getName().equals(stageName)) {
                return deleteStage(stageName);
            }
        }

        return false;
    }

    public ArrayList<Show> getShows() {
        return this.shows;
    }

    public ArrayList<Stage> getStages() {
        return this.stages;
    }

    public ArrayList<Artist> getArtists() {
        return this.artists;
    }

    public void savePlanner() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter fileWriter = new FileWriter(saveFileName);
            fileWriter.write(gson.toJson(this));
            fileWriter.close();
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(saveFileName));
//            objectOutputStream.writeObject(this);
//            objectOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Planner{" +
                "shows=" + shows +
                ", stages=" + stages +
                ", artists=" + artists +
                ", saveFileName='" + saveFileName + '\'' +
                '}';
    }
}