package PlannerData;

import java.util.ArrayList;

public class Planner implements Serializable {
    private ArrayList<Show> shows;
    private transient ArrayList<Stage> stages;
    private transient ArrayList<Artist> artists;
    private ArrayList<Genres> genres;

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
        this.shows.add(show);
        //TODO add the stage and artist
        if (!this.stages.contains(show.getStage())) {
            this.stages.add(show.getStage());
        }
        for (Artist artist : show.getArtists()) {
            if (!this.artists.contains(artist)) {
                this.artists.add(artist);
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
     * @param beginTime
     * @param endTime
     * @param artists
     * @param name
     * @param stage
     * @param description
     * @param genre
     * @param expectedPopularity
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