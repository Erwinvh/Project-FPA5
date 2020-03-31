package GUILogic;

import PlannerData.Show;

import java.util.ArrayList;

public class PopularityTracker {
    private ArrayList<Show> activeShows;
    private ArrayList<Integer> amountAtShows;

    public PopularityTracker(){
        activeShows = DataController.getActiveShows();
        amountAtShows = new ArrayList<>();
        for(Show show : activeShows){
            amountAtShows.add(0);
        }
    }

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
