package GUILogic;

import PlannerData.Show;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class that counts the amount of people present at shows
 */

public class PopularityTracker {
    HashMap<Show, Integer> peopleAtShows;
    private ArrayList<Show> activeShows;

    public PopularityTracker(){
        peopleAtShows = new HashMap<>();
        activeShows = DataController.getInstance().getActiveShows();
        for(Show show : activeShows){
            peopleAtShows.put(show, 0);
        }
    }

    /**
     * Looks if there is room for one extra visitor in a show and adds one to the amount people to the show
     * @param show the show the visitor wants to go to
     * @return true if there is room, false if the show is full
     */
    public boolean canGoToShow(Show show){
        for(Show activeShow : peopleAtShows.keySet()){
            if(show == activeShow){
                if(show.getStage().getCapacity() >= peopleAtShows.get(show) + 1){
                    peopleAtShows.put(show, peopleAtShows.get(show) + 1);
                    return true;
                }
            }
        }

        return false;
    }
}
