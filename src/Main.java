import PlannerData.Planner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import static PlannerData.Planner.saveFileName;

public class Main {
    public static void main(String[] args) {
        Planner planner = new Planner();

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(saveFileName));
            planner = (Planner) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
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
//        ArrayList<Artist> artists = new ArrayList<>();
//        artists.add(new Artist("Arne", Genres.BLUES, "hij zingt en speelt gitaar."));
//        artists.add(new Artist("Rik", Genres.ROCK, "Hij schreeuwt."));
//        artists.add(new Artist("Ralf", Genres.ALTERNATIVE_ROCK, "GIT is als de dood, niet prettig maar onvermijdbaar."));
//        artists.add(new Artist("Kasper", Genres.NIGHTCORE, "Een ware leider en houdt van Wendy."));
//        artists.add(new Artist("Erwin", Genres.JAZZ, "Een ware leider."));
//        artists.add(new Artist("Lars", Genres.METAL, "De legend van Spoderman"));
//
//        ArrayList<Stage> stages = new ArrayList<>();
//        stages.add(new Stage(2500, "Main stage"));
//        stages.add(new Stage(300, "Improvisation stage"));
//        stages.add(new Stage(100, "Single stage"));
//
//        Show show = new Show(LocalTime.now(), LocalTime.now().plusHours(1), artists, stages.get(0), 100);
//        planner.addShow(show);
//        planner.savePlanner();
//        Artist lars = new Artist("Lars", Genres.BLUES, "Gekke man");
//        Stage stage = new Stage(100, "Main stage");
//        ArrayList<Genres> genres = new ArrayList<>();
//        genres.add(Genres.DANCE);
//        genres.add(Genres.NIGHTCORE);
//        planner.addShow(new Show(LocalTime.now(), LocalTime.now().plusHours(1), lars, "De ochtendshow van Lars", stage, "Lars die jamt", genres, 100000));
//        planner.savePlanner();

        System.out.println(planner.toString());
        //launch(GUI.class);
    }
}