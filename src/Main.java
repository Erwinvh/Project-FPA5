import Enumerators.Genres;
import GUILogic.GUI;
import PlannerData.Artist;
import PlannerData.Planner;
import PlannerData.Show;
import PlannerData.Stage;

import java.time.LocalTime;
import java.util.ArrayList;

import static javafx.application.Application.launch;

public class Main {
    public static void main(String[] args) {

        //test data
        Planner planner = new Planner();
        ArrayList<Artist> artists = new ArrayList<>();
        artists.add(new Artist("Arne", Genres.BLUES,"hij zingt en speelt gitaar"));
        Show show = new Show(LocalTime.now(),LocalTime.now(),artists,new Stage(100,"een"),100);
        planner.addShow(show);
        planner.savePlanner();
        System.out.println(planner.toString());


        launch(GUI.class);
    }
}
