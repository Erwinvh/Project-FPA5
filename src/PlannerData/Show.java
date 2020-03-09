package PlannerData;

import Enumerators.Genres;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * A class that indicates a show
 */
public class Show implements Serializable {

    private LocalTime beginTime;
    private LocalTime endTime;
    private String name;
    private String description;
    private ArrayList<Artist> artists;
    private Stage stage;
    private ArrayList<Genres> genre;
    private int expectedPopularity;

    /**
     * Creates a show
     * @param beginTime the starting time of the show
     * @param endTime the ending time of the show
     * @param artists the artists performing
     * @param name the name of the show
     * @param stage the stage of the show
     * @param description the description of the show
     * @param genre the genre of the show
     * @param expectedPopularity the expected popularity of the show
     */
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

    public String getStageName() {
        return "" + this.stage.getName();
    }

    public String getGenreFancyName(){
        return "" + this.genre.get(0).getFancyName();
    }

    /**
     * Gets all the artists in the String format
     * @return all the Artist names
     */
    public String getArtistsNames() {
        StringBuilder LineUp = new StringBuilder();
        for (Artist artist : this.artists) {
            if (!artist.equals(this.artists.get(this.artists.size()-1))){
                LineUp.append(artist.getName() + ", ");
            }
            else {
                LineUp.append(artist.getName());
            }
        }
        return LineUp.toString();
    }


    public int getExpectedPopularity() {
        return expectedPopularity;
    }

    public void setExpectedPopularity(int expectedPopularity) {
        this.expectedPopularity = expectedPopularity;
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
     * Gets the begin time of the show in a String format
     * @return the begin time
     */
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

    /**
     * Gets the begin time of the show in a String format
     * @return the end time
     */
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