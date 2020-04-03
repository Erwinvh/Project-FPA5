package GUILogic.Tabs.Windows;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ErrorWindow {

    private Stage errorStage;

    /**
     * This Constructor creates a Error Window that shows the user which Error he/she made during a process.
     *
     * @param parentStage The stage that from which the error window is generated.
     * @param errorList   The list of errors that have occurred and will be shown.
     */
    public ErrorWindow(Stage parentStage, ArrayList<String> errorList) {
        this.errorStage = new Stage();
        this.errorStage.setWidth(500);
        this.errorStage.setResizable(false);
        this.errorStage.setHeight(250);
        this.errorStage.initOwner(parentStage);
        this.errorStage.initModality(Modality.WINDOW_MODAL);
        this.errorStage.setTitle("Error");
        this.errorStage.getIcons().add(new Image("alert.png"));

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
        errorScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.ESCAPE) {
                    getErrorStage().close();
                }
            }
        });

        this.errorStage.setScene(errorScene);
        this.errorStage.show();
    }

    /**
     * The getter for the error window stage
     * @return The error window stage
     */
    public Stage getErrorStage() {
        return errorStage;
    }
}