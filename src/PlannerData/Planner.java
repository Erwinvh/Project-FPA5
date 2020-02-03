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
     * @param show object where data about the show is stored
     */
    public void addShow(Show show){
        this.shows.add(show);
        //TODO add the stage and artist
    }

    /**
     *
     * @param show
     */
    public void addShow(ArrayList<Show> show){
        this.shows.addAll(show);
        //TODO add the stage and artist
    }

    /**
     *
     */
    public void addShow(/*TODO add contructor from show*/){
        Show show = new Show(/*TODO*/);
        this.shows.add(show);
        //TODO add the stage and artist
    }




}