import GUILogic.DataController;
import GUILogic.GUI;

import static javafx.application.Application.launch;

public class Main {
    public static void main(String[] args) {
        DataController dataController = new DataController();
        launch(GUI.class);
    }
}