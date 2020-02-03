package PlannerData;

import Enumerators.Genres;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 *
 */
public class Planner implements Serializable {
    private ArrayList<Show> shows;
    private transient ArrayList<Stage> stages;
    private transient ArrayList<Artist> artists;
    private transient ArrayList<Genres> genres;

    public Planner() {
        this.shows = new ArrayList<>();
        this.stages = new ArrayList<>();
        this.artists = new ArrayList<>();
        this.genres = new ArrayList<>();
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
     * overloaded function to add an list of shows
     *
     * @param shows array list of shows which are objects where data about the show is stored
     */
    public void addShow(ArrayList<Show> shows) {
        for (Show show : shows){
            addShow(show);
        }
    }

    /**
     * overloaded function to create and add a show
     *
     * @param beginTime the time the show begins
     * @param endTime the time the show ends
     * @param artists an array list of all the artists that contribute to the show
     * @param name name of the show
     * @param stage Which stage the show is performed
     * @param description special information
     * @param genre genre of the music performed
     * @param expectedPopularity how many visitors are expected
     */
    public void addShow(LocalTime beginTime, LocalTime endTime, ArrayList<Artist> artists, String name, Stage stage, String description, ArrayList<Genres> genre, int expectedPopularity) {
        addShow(new Show(beginTime,endTime,artists,name,stage,description,genre,expectedPopularity));
    }

    public void savePlanner(){
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("Planner.txt"));
            objectOutputStream.writeObject(shows);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}