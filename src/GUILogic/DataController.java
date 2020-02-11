package GUILogic;

import Enumerators.Genres;
import PlannerData.Artist;
import PlannerData.Planner;
import PlannerData.Show;
import PlannerData.Stage;
import com.google.gson.Gson;

import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;

import static PlannerData.Planner.saveFileName;

public class DataController {

    static Planner planner;

    public DataController() {
        planner = new Planner();
        Gson gson = new Gson();

        try {
            File file = new File(saveFileName);
            if (!file.exists()) {
                file.createNewFile();
            } else {
                try (Reader reader = new FileReader(saveFileName)){
                    planner = gson.fromJson(reader,Planner.class);
                }catch (Exception e){
                    System.out.println("error loading data due to: ");
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Was not able to gather data from " + saveFileName + " due to: ");
            e.printStackTrace();
        }

//        Artist lars = new Artist("Lars", Genres.BLUES, "Gekke man");
//        Stage stage = new Stage(100, "Main stage");
//        ArrayList<Genres> genres = new ArrayList<>();
//        genres.add(Genres.DANCE);
//        genres.add(Genres.NIGHTCORE);
//        planner.addShow(new Show(LocalTime.now(), LocalTime.now().plusHours(1), lars, "De ochtendshow van Lars", stage, "Lars die jamt", genres, 100000));
//        planner.savePlanner();

        ArrayList<Artist> artists = new ArrayList<>();
        artists.add(new Artist("Arne de Beer", Genres.BLUES, "Smoking hot"));
        artists.add(new Artist("Lars Giskes", Genres.PUNK_ROCK, "The legend of Spoderman"));
        artists.add(new Artist("Henk", Genres.METAL, "Dit is Henk"));

        ArrayList<Stage> stages = new ArrayList<>();
        stages.add(new Stage(500, "Main Stage"));
        stages.add(new Stage(100, "Second Stage"));

//        planner.addShow(new Show(LocalTime.now(), LocalTime.now().plusMinutes(30), stages.get(0), artists.get(0), 400));
//        planner.addShow(new Show(LocalTime.now().plusMinutes(45), LocalTime.now().plusHours(2), stages.get(0), artists.get(1), 400));
//        planner.addShow(new Show(LocalTime.now(), LocalTime.now().plusMinutes(30), stages.get(1), artists.get(2), 75));
    }

    public static Planner getPlanner() {
        return planner;
    }
}