import GUILogic.DataController;
import GUILogic.GUI;

import static javafx.application.Application.launch;

public class Main {
    public static void main(String[] args) {
        new DataController();
        launch(GUI.class);
    }
}