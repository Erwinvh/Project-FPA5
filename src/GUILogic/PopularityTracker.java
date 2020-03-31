package GUILogic;

import PlannerData.Show;

import java.util.ArrayList;

/**
 * A class that counts the amount of people present at shows
 */

public class PopularityTracker {
    private ArrayList<Show> activeShows;
    private ArrayList<Integer> amountAtShows;

    public PopularityTracker(){
        activeShows = DataController.getInstance().getActiveShows();
        amountAtShows = new ArrayList<>();
        for(Show show : activeShows){
            amountAtShows.add(0);
        }
    }

    /**
     * Looks if there is room for one extra visitor in a show
     * @param show the show the visitor wants to go to
     * @return true if there is room, false if the show is full
     */
    public boolean canGoToShow(Show show){
        for(Show activeShow : activeShows){
            if(show == activeShow){
                int showIndex = activeShows.indexOf(activeShow);
                if(show.getStage().getCapacity() >= amountAtShows.get(showIndex)-1){
                    amountAtShows.set(showIndex,amountAtShows.get(showIndex) + 1);
                    return true;
                }
            }
        }

        return false;
    }
}
