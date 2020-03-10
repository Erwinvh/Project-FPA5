package GUILogic;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ErrorWindow {

    /**
     * This Constructor creates a Error Window that shows the user which Error he/she made during a process.
     *
     * @param parentStage
     * @param errorList
     */
    public ErrorWindow(Stage parentStage, ArrayList<String> errorList) {
        Stage errorPopUp = new Stage();
        errorPopUp.setWidth(500);
        errorPopUp.setResizable(false);
        errorPopUp.setHeight(250);
        errorPopUp.initOwner(parentStage);
        errorPopUp.initModality(Modality.WINDOW_MODAL);
        errorPopUp.setTitle("Error");
        errorPopUp.getIcons().add(new Image("alert.png"));

        HBox baseStructure = new HBox();

        Image errorImage = new Image("file:resources/alert.png");
        ImageView showError = new ImageView(errorImage);
        showError.setFitWidth(100);
        showError.setFitHeight(100);
        baseStructure.getChildren().add(showError);

        VBox errorVBox = new VBox();
        for (String error : errorList) {
            errorVBox.getChildren().add(new Label(error));
        }

        errorVBox.getChildren().add(new Label("Please resolve these errors before submitting."));
        errorVBox.setAlignment(Pos.CENTER);
        baseStructure.getChildren().add(errorVBox);

        Scene errorScene = new Scene(baseStructure);
        errorScene.getStylesheets().add("Window-StyleSheet.css");

        errorPopUp.setScene(errorScene);
        errorPopUp.show();
    }
}