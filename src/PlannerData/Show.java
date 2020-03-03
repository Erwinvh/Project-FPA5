package PlannerData;

import Enumerators.Genres;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;

public class Show implements Serializable {

    private LocalTime beginTime;
    private LocalTime endTime;
    private String name;
    private String description;
    private ArrayList<Artist> artists;
    private Stage stage;
    private ArrayList<Genres> genre;
    private int expectedPopularity;

    public Show(LocalTime beginTime, LocalTime endTime, ArrayList<Artist> artists, String name, Stage stage, String description, ArrayList<Genres> genre, int expectedPopularity) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.artists = artists;
        this.name = name;
        this.stage = stage;
        this.description = description;
        this.genre = genre;
        this.expectedPopularity = expectedPopularity;
    }

    public Show(LocalTime beginTime, LocalTime endTime, Artist artist, String name, Stage stage, String description, ArrayList<Genres> genre, int expectedPopularity) {
        this(beginTime, endTime, new ArrayList<>(), name, stage, description, genre, expectedPopularity);
        this.artists.add(artist);
    }

    public String getStageName() {
        return "" + this.stage.getName();
    }

    public String getGenreFancyName(){
        return "" + this.genre.get(0).getFancyName();
    }

    public String getArtistsNames() {
        StringBuilder LineUp = new StringBuilder();
        for (Artist artist : this.artists) {
            LineUp.append(artist.getName() + ", ");
        }
        return LineUp.toString();
    }

    public Show(LocalTime beginTime, LocalTime endTime, ArrayList<Artist> artists, Stage stage, int expectedPopularity) {
        this(beginTime, endTime, artists, "", stage, "", new ArrayList<>(), expectedPopularity);
        if (artists.size() != 0) {
            this.name = this.artists.get(0).getName();
        }
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

    public String getBeginTimeString() {
        String beginTimeString = "";
        if (beginTime.getHour()<10){
            beginTimeString = "0" + beginTime.getHour() + ":";
        }
        else{
            beginTimeString = beginTime.getHour() + ":";
        }
        if (beginTime.getMinute()<10){
            beginTimeString += "0" + beginTime.getMinute();
        }
        else{
            beginTimeString+=beginTime.getMinute();
        }

        return beginTimeString;
    }

    public String getEndTimeString() {
        String endTimeString = "";
        if (endTime.getHour()<10){
            endTimeString = "0" + endTime.getHour() + ":";
        }
        else{
            endTimeString = endTime.getHour() + ":";
        }
        if (endTime.getMinute()<10){
            endTimeString += "0" + endTime.getMinute();
        }
        else{
            endTimeString+=endTime.getMinute();
        }

        return endTimeString;
    }
}