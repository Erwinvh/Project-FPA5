package PlannerData;

import java.util.ArrayList;

public class Planner {
    private ArrayList<Show> shows;
    private ArrayList<Stage> stages;
    private ArrayList<Artist> artists;

    public Planner() {
        this.shows = new ArrayList<>();
        this.stages = new ArrayList<>();
        this.artists = new ArrayList<>();
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
     * @param show array list of shows which are objects where data about the show is stored
     */
    public void addShow(ArrayList<Show> show) {
        this.shows.addAll(show);
        //TODO add the stage and artist
    }

    /**
     * TODO
     */
    public void addShow(/*TODO add contructor from show*/) {
        Show show = new Show(/*TODO*/);
        this.shows.add(show);
        //TODO add the stage and artist
    }


}