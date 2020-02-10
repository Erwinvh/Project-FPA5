package PlannerData;

import Enumerators.Genres;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;

public class Show implements Serializable {

    private int expectedPopularity;
    private LocalTime beginTime;
    private LocalTime endTime;
    private String name;
    private String description;
    private Stage stage;
    private ArrayList<Genres> genre;
    private ArrayList<Artist> artists;

    public Show(LocalTime beginTime, LocalTime endTime, ArrayList<Artist> artists, String name, Stage stage, String description, ArrayList<Genres> genre, int expectedPopularity) {
        this.expectedPopularity = expectedPopularity;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.name = name;
        this.description = description;
        this.stage = stage;
        this.genre = genre;
        this.artists = artists;
    }

    public Show(LocalTime beginTime, LocalTime endTime, Artist artist, String name, Stage stage, String description, ArrayList<Genres> genre, int expectedPopularity) {
        this(beginTime, endTime, new ArrayList<>(), name, stage, description, genre, expectedPopularity);
        this.artists.add(artist);
    }

    public Show(LocalTime beginTime, LocalTime endTime, ArrayList<Artist> artists, Stage stage, int expectedPopularity) {
        this(beginTime, endTime, artists, "", stage, "", new ArrayList<>(), expectedPopularity);
        this.name = this.artists.get(0).getName();
        this.description = "";

        for (Artist artist : this.artists) {
            if (!this.genre.contains(artist.getGenre())) {
                this.genre.add(artist.getGenre());
            }
        }
    }

    public Show(LocalTime beginTime, LocalTime endTime, Stage stage, Artist artist, int expectedPopularity) {
        this(beginTime, endTime, new ArrayList<>(), stage, expectedPopularity);
        this.artists.add(artist);
    }

    public Show(LocalTime beginTime, LocalTime endTime, Stage stage, ArrayList<Artist> artists, String name, String description, Genres genre, int expectedPopularity) {
        this(beginTime, endTime, artists, name, stage, description, new ArrayList<>(), expectedPopularity);
        this.genre.add(genre);
    }

    public Show(LocalTime beginTime, LocalTime endTime, Stage stage, Artist artist, String name, String description, Genres genre, int expectedPopularity) {
        this(beginTime, endTime, artist, name, stage, description, new ArrayList<>(), expectedPopularity);
        this.genre.add(genre);
    }

    public int getExpectedPopularity() {
        return expectedPopularity;
    }

    public LocalTime getBeginTime() {
        return beginTime;
    }

    public void setGenre(ArrayList<Genres> genre) {
        this.genre = genre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Stage getStage() {
        return stage;
    }

    public ArrayList<Genres> getGenre() {
        return genre;
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    /**
     * Calculates the duration of the show
     *
     * @return duration, a Local time of the duration of the show
     */
    public LocalTime getDuration() {
        LocalTime duration = endTime.minusMinutes(beginTime.getMinute());
        duration = endTime.minusHours(beginTime.getHour());
        return duration;
    }
}