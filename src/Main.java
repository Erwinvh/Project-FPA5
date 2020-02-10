import Enumerators.Genres;
import GUILogic.DataController;
import GUILogic.GUI;
import PlannerData.Artist;
import PlannerData.Show;
import PlannerData.Stage;

import java.awt.*;
import java.time.LocalTime;
import java.util.ArrayList;

import static javafx.application.Application.launch;

public class Main {
    public static void main(String[] args) {


        DataController dataController = new DataController();

//        Artist artist = new Artist("Rik", Genres.ROCK,"sings beautyfull");
//        Stage stage = new Stage(100,"main");
//        ArrayList<Genres> genres = new ArrayList<>();
//        genres.add(Genres.DANCE);
//        Show show = new Show(LocalTime.now(),LocalTime.now(),artist,"rock",stage,"temp",genres,100);
//        DataController.getPlanner().addShow(show);

        launch(GUI.class);
    }
}