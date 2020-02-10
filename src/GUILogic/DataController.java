package GUILogic;

import PlannerData.Planner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import static PlannerData.Planner.saveFileName;

public class DataController {

    static Planner planner;

    public DataController() {
        planner = new Planner();

        try {
            File file = new File(saveFileName);
            if (!file.exists()) {
                file.createNewFile();
            } else {
                FileInputStream fileInputStream = new FileInputStream(saveFileName);
                if (fileInputStream.available() > 1) {
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                    planner = (Planner) objectInputStream.readObject();
                }
            }
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


    }

    public static Planner getPlanner() {
        return planner;
    }
}