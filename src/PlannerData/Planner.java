package PlannerData;

import Enumerators.Genres;
import javafx.scene.image.Image;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;

public class Planner implements Serializable {

    private ArrayList<Show> shows;
    private ArrayList<Stage> stages;
    private ArrayList<Artist> artists;
//    private ArrayList<Genres> genres;

    public final static String saveFileName = "Planner.txt";

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
        if (!this.shows.contains(show)) {
            this.shows.add(show);
            if (!this.stages.contains(show.getStage())) {
                this.stages.add(show.getStage());
            }

            for (Artist artist : show.getArtists()) {
                if (!this.artists.contains(artist)) {
                    this.artists.add(artist);
                }
            }
        }
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

    public void savePlanner() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(saveFileName));
            objectOutputStream.writeObject(this);
            objectOutputStream.close();

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

    public ArrayList<Show> getShows() {
        return this.shows;
    }

    public ArrayList<Stage> getStages() {
        return stages;
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    public void addArtist(String name, Genres genre, Image image, String description) {
        boolean contains = false;
        for (Artist existingArtist : this.artists) {
            if (name.equals(existingArtist.getName())) {
                contains = true;
            }
        }
        if (!contains) {
            this.artists.add(new Artist(name, genre, image, description));
        }
    }

    public void addArtist(String name, Genres genre, String description) {
        boolean contains = false;
        for (Artist existingArtist : this.artists) {
            if (name.equals(existingArtist.getName())) {
                contains = true;
            }
        }
        if (!contains) {
            this.artists.add(new Artist(name, genre, description));
        }
    }

    public void addStage(int capacity, String name) {
        boolean contains = false;
        for (Stage stage : this.stages) {
            if (stage.getName().equals(name)) {
                contains = true;
            }
        }
        if (!contains) {
            this.stages.add(new Stage(capacity, name));
        }
    }

    public void addShow(LocalTime beginTime, LocalTime endTime, Stage stage, int popularity, Genres genre, ArrayList<Artist> artists) {
        boolean canBeAdded = true;
        for (Show show : this.shows) {
            if (artists.get(0).getName().equals(show.getName())) {
                canBeAdded = false;
            }

            if (stage.getName().equals(show.getStage().getName())) {
                if (beginTime.isAfter(show.getBeginTime()) && beginTime.isBefore(show.getEndTime())) {
                    canBeAdded = false;
                }

                if (endTime.isAfter(show.getBeginTime()) && endTime.isBefore(show.getEndTime())) {
                    canBeAdded = false;
                }
            }
        }

        if (canBeAdded) {
            this.shows.add(new Show(beginTime, endTime, stage, artists, "", "", genre, popularity));
        }
    }
}