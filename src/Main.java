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
        artists.add(new Artist("Arne", Genres.BLUES, "hij zingt en speelt gitaar."));
        artists.add(new Artist("Rik", Genres.ROCK, "Hij schreeuwt."));
        artists.add(new Artist("Ralf", Genres.ALTERNATIVE_ROCK, "GIT is als de dood, niet prettig maar onvermijdbaar."));
        artists.add(new Artist("Kasper", Genres.NIGHTCORE, "Een ware leider en houdt van Wendy."));
        artists.add(new Artist("Erwin", Genres.JAZZ, "Een ware leider."));
        artists.add(new Artist("Lars", Genres.METAL, "De legend van Spoderman"));

        ArrayList<Stage> stages = new ArrayList<>();
        stages.add(new Stage(2500, "Main stage"));
        stages.add(new Stage(300, "Improvisation stage"));
        stages.add(new Stage(100, "Single stage"));

        Show show = new Show(LocalTime.now(), LocalTime.now().plusHours(1), artists, stages.get(0), 100);
        planner.addShow(show);
        planner.savePlanner();
        System.out.println(planner.toString());

        launch(GUI.class);
    }
}