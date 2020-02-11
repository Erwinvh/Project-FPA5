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
        launch(GUI.class);
    }
}