package PlannerData;

import Enumerators.Genres;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * A class that indicates a show
 */
public class Show implements Serializable, Comparable<Show> {

    private LocalTime beginTime;
    private LocalTime endTime;
    private String name;
    private String description;
    private ArrayList<Artist> artists;
    private Stage stage;
    private Genres genre;
    private int expectedPopularity;

    /**
     * Creates a show
     *
     * @param beginTime          the starting time of the show
     * @param endTime            the ending time of the show
     * @param artists            the artists performing
     * @param name               the name of the show
     * @param stage              the stage of the show
     * @param description        the description of the show
     * @param genre              the genre of the show
     * @param expectedPopularity the expected popularity of the show
     */
    public Show(LocalTime beginTime, LocalTime endTime, ArrayList<Artist> artists, String name, Stage stage, String description, Genres genre, int expectedPopularity) {
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

    /**
     * Gets all the artists in the String format
     *
     * @return all the Artist names
     */
    public String getArtistsNames() {
        StringBuilder lineUp = new StringBuilder();
        for (Artist artist : this.artists) {
            if (!artist.equals(this.artists.get(this.artists.size() - 1))) {
                lineUp.append(artist.getName() + ", ");
            } else {
                lineUp.append(artist.getName());
            }
        }
        return lineUp.toString();
    }

    public int getExpectedPopularity() {
        return this.expectedPopularity;
    }

    public void setExpectedPopularity(int expectedPopularity) {
        this.expectedPopularity = expectedPopularity;
    }

    public LocalTime getBeginTime() {
        return this.beginTime;
    }

    public void setGenre(Genres genre) {
        this.genre = genre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalTime getEndTime() {
        return this.endTime;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Stage getStage() {
        return this.stage;
    }

    public Genres getGenre() {
        return this.genre;
    }

    public ArrayList<Artist> getArtists() {
        return this.artists;
    }

    /**
     * Gets the begin time of the show in a String format
     *
     * @return the begin time
     */
    public String getBeginTimeString() {
        return getTimeString(this.beginTime);
    }

    /**
     * Gets the begin time of the show in a String format
     *
     * @return the end time
     */
    public String getEndTimeString() {
        return getTimeString(this.endTime);
    }

    /**
     * A getter for the time that sets it as a string.
     * @param time time as LocalTime
     * @return time as String.
     */
    private String getTimeString(LocalTime time){
        String timeString;

        if (time.getHour() < 10) timeString = "0" + time.getHour() + ":";
        else timeString = time.getHour() + ":";

        if (time.getMinute() < 10) timeString += "0" + time.getMinute();
        else timeString += time.getMinute();

        return timeString;
    }

    /**
     * A method that compares shows based on the expectedPopularity
     * @param otherShow the show this show is compared to
     * @return 1 if this show has higher expectedPopularity, 0 if equal, -1 if less
     */
    public int compareTo(Show otherShow){
        return Integer.compare(this.expectedPopularity, otherShow.expectedPopularity);
    }
}