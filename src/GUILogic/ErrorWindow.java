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
     * @param upperStage
     * @param errorlist
     */
    public ErrorWindow(Stage upperStage, ArrayList<String> errorlist) {
        Stage errorPopUp = new Stage();
        errorPopUp.setWidth(500);
        errorPopUp.setResizable(false);
        errorPopUp.setHeight(250);
        errorPopUp.initOwner(upperStage);
        errorPopUp.initModality(Modality.WINDOW_MODAL);
        HBox baseStructure = new HBox();
        Image errorImage = new Image("file:Resources/alert.png");
        ImageView showError = new ImageView(errorImage);
        showError.setFitWidth(100);
        showError.setFitHeight(100);
        baseStructure.getChildren().add(showError);
        VBox errorList = new VBox();
        for (String Error : errorlist) {
            errorList.getChildren().add(new Label(Error));
        }
        errorList.getChildren().add(new Label("Please resolve these errors before submitting."));
        errorList.setAlignment(Pos.CENTER);
        baseStructure.getChildren().add(errorList);
        Scene errorScene = new Scene(baseStructure);
        errorScene.getStylesheets().add("Window-StyleSheet.css");

        errorPopUp.setScene(errorScene);
        errorPopUp.show();
    }

}
