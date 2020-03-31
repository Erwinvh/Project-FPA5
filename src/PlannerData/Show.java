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
                lineUp.append(artist.getName()).append(", ");
            } else {
                lineUp.append(artist.getName());
            }
        }
        return lineUp.toString();
    }

    /**
     * The getter for the Expected Popularity
     *
     * @return The expected popularity
     */
    public int getExpectedPopularity() {
        return this.expectedPopularity;
    }

    /**
     * The setter for the expected popularity
     */
    public void setExpectedPopularity(int expectedPopularity) {
        this.expectedPopularity = expectedPopularity;
    }

    /**
     * The getter for the begin time of the show
     *
     * @return The shows begin time
     */
    public LocalTime getBeginTime() {
        return this.beginTime;
    }

    /**
     * The setter for the show genre
     *
     * @param genre the new genre for this show
     */
    public void setGenre(Genres genre) {
        this.genre = genre;
    }

    /**
     * The setter for the show description
     *
     * @param description the new description for this show
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * The getter for the shows end time.
     *
     * @return The end time of the show
     */
    public LocalTime getEndTime() {
        return this.endTime;
    }

    /**
     * The getter of the show name
     *
     * @return The show name
     */
    public String getName() {
        return this.name;
    }

    /**
     * The setter for the show name
     *
     * @param name the new name for this show
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The getter for the show description
     *
     * @return The show description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * The getter for the show stage
     *
     * @return the stage this show will be held on
     */
    public Stage getStage() {
        return this.stage;
    }

    /**
     * The getter for the show genre
     *
     * @return The show genre
     */
    public Genres getGenre() {
        return this.genre;
    }

    /**
     * The getter for the list of artists for the show
     *
     * @return The ArrayList of artists
     */
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
     *
     * @param time time as LocalTime
     * @return time as String.
     */
    private String getTimeString(LocalTime time) {
        String timeString;

        if (time.getHour() < 10) timeString = "0" + time.getHour() + ":";
        else timeString = time.getHour() + ":";

        if (time.getMinute() < 10) timeString += "0" + time.getMinute();
        else timeString += time.getMinute();

        return timeString;
    }

    /**
     * A method that compares shows based on the expectedPopularity
     *
     * @param otherShow the show this show is compared to
     * @return 1 if this show has higher expectedPopularity, 0 if equal, -1 if less
     */
    public int compareTo(Show otherShow) {
        return Integer.compare(this.expectedPopularity, otherShow.expectedPopularity);
    }

    /**
     * This method allows two show begin times to be compared
     *
     * @param otherShow the show to compare this show with
     * @return an integer
     */
    public int compareToTime(Show otherShow) {
        return this.beginTime.compareTo(otherShow.beginTime);
        //return this.beginTime - otherShow.beginTime;
    }

    /**
     * The setter for the show begin time
     */
    public void setBeginTime(LocalTime beginTime) {
        this.beginTime = beginTime;
    }

    /**
     * The setter for the show end time
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * The setter for the show stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * The setter for the list of artists
     */
    public void setArtists(ArrayList<Artist> artists) {
        this.artists = artists;
    }
}