package GUILogic;

import Enumerators.Genres;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class AddingNewWindow {

    private Stage upperStage;
    private Stage currentStage = new Stage();
    private ArrayList<String> errorList = new ArrayList<>();
    private FileChooser fileChooser = new FileChooser();
    private ImageView artistImage = new ImageView();

    /**
     * This is the constructor of the base of the submenus.
     * This method also decides which submenu it should show to the user.
     *
     * @param ScreenNumber
     * @param upperStage
     */
    public AddingNewWindow(int ScreenNumber, Stage upperStage) {
        this.upperStage = upperStage;
        this.artistImage.setFitWidth(200);
        this.artistImage.setFitHeight(200);
        this.currentStage.initOwner(this.upperStage);
        this.currentStage.initModality(Modality.WINDOW_MODAL);
        this.currentStage.setResizable(false);
        if (ScreenNumber == 1) {
            stageAddWindow();
        } else {
            artistAddWindow();
        }
    }

    /**
     * This method creates the submenu where the user can add a Stage to the festival.
     * The user must choose a name for the Stage and choose a capacity.
     */
    public void stageAddWindow() {
        this.currentStage.setWidth(200);
        this.currentStage.setHeight(250);
        VBox newStageList = new VBox();

        Label StageLabelName = new Label("Stage Name:");
        newStageList.getChildren().add(StageLabelName);
        TextField StageName = new TextField();
        newStageList.getChildren().add(StageName);

        Label StageLabelCapacity = new Label("Stage Capacity:");
        newStageList.getChildren().add(StageLabelCapacity);
        TextField InputTextField = new TextField();
        newStageList.getChildren().add(InputTextField);

        HBox choice = new HBox();
        Button stop = new Button("Cancel");
        stop.setOnAction(event -> {
            this.currentStage.close();
        });
        choice.getChildren().add(stop);
        Button confirm = new Button("Confirm");
        confirm.setOnAction(event -> {
            newStageControl(StageName, InputTextField);
        });
        choice.getChildren().add(confirm);
        choice.setPadding(new Insets(10));
        choice.setSpacing(20);

        newStageList.getChildren().add(choice);
        Scene artistAddScene = new Scene(newStageList);
        artistAddScene.getStylesheets().add("Window-StyleSheet.css");
        this.currentStage.setScene(artistAddScene);
        this.currentStage.show();
    }

    /**
     * This method checks if the new Stage is valid or not.
     * If it isn't valid it will notify the user of the mistakes so the user may repair them before submitting them again.
     *
     * @param stageName
     * @param capacity
     */
    public void newStageControl(TextField stageName, TextField capacity) {
        this.errorList.clear();
        if (stageName.getText().length() == 0) {
            this.errorList.add("The stage name has not been filled in.");
        }
        if (capacity.getText().length() == 0) {
            this.errorList.add("The capacity has not been filled in.");
        } else {
            try {
                int capacityCheck = Integer.parseInt(capacity.getText());
                if (capacityCheck <= 0) {
                    this.errorList.add("The Capacity must be larger than 0");
                }
            } catch (Exception e) {
                this.errorList.add("The capacity must be a Number.");
            }
        }
        if (this.errorList.isEmpty()) {
            DataController.getPlanner().addStage(Integer.parseInt(capacity.getText()), stageName.getText());
            this.currentStage.close();
        } else {
            new ErrorWindow(this.currentStage, this.errorList);
        }
    }

    /**
     * This method sets the submenu to the Add Artist submenu.
     * The user will be able to fill in all the required fields for an unknown artist.
     */
    public void artistAddWindow() {
        this.currentStage.setWidth(275);
        this.currentStage.setHeight(400);

        VBox newArtistList = new VBox();
        newArtistList.setPrefWidth(250);

        //artist name
        Label artistNameLabel = new Label("Artist name:");
        TextField artistName = new TextField();
        artistName.setPrefWidth(250);
        newArtistList.getChildren().add(artistNameLabel);
        newArtistList.getChildren().add(artistName);

        //genre
        Label artistGenreLabel = new Label("Artist's Genres:");
        newArtistList.getChildren().add(artistGenreLabel);
        ComboBox genreComboBox = new ComboBox();
        genreComboBox.getItems().add("None");
        for (Genres genre : Enumerators.Genres.values()) {
            genreComboBox.getItems().add(genre.getFancyName());
        }
        newArtistList.getChildren().add(genreComboBox);

        //picture
        Label artistPictureLabel = new Label("Artist's picture:");
        newArtistList.getChildren().add(artistPictureLabel);
        newArtistList.getChildren().add(this.artistImage);
        Button artistPictureInput = new Button("Add a picture");
        newArtistList.getChildren().add(artistPictureInput);
        newArtistList.getChildren().add(new Label("the picture wil be resized"));
        newArtistList.getChildren().add(new Label("to 200X200 pixels"));


        artistPictureInput.setOnAction(event -> {
            File selectedFile = fileChooser.showOpenDialog(this.currentStage);
            fileChooser.setInitialDirectory(new File("Resources"));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PNG Files", "*.png")
                    , new FileChooser.ExtensionFilter("Jpg Files", "*.jpg")
                    , new FileChooser.ExtensionFilter("Jpeg Files", "*.jpeg")
            );
            try {
                BufferedImage bufferedArtistImage = ImageIO.read(selectedFile);
                Image image = SwingFXUtils.toFXImage(bufferedArtistImage, null);
                artistImage.setImage(image);
            } catch (Exception e) {
                this.errorList.add("this is not an image file: choose a Jpeg/Jpg/PNG file");
                new ErrorWindow(this.currentStage, this.errorList);
                System.out.println("failed image");
                System.out.println("Exception:" + e);
            }
        });

        //artist description
        Label artistDescriptionLabel = new Label("Artist's description:");
        TextArea artistDescription = new TextArea();
        artistDescription.setPrefWidth(250);
        newArtistList.getChildren().add(artistDescriptionLabel);
        newArtistList.getChildren().add(artistDescription);

        //buttons
        HBox choice = new HBox();
        Button stop = new Button("Cancel");
        stop.setOnAction(event -> {
            this.currentStage.close();
        });
        choice.getChildren().add(stop);
        Button confirm = new Button("Confirm");
        confirm.setOnAction(event -> {
            newArtistControl(artistName, artistDescription, genreComboBox.getValue().toString(), artistImage.getImage(), artistDescription);
        });
        choice.getChildren().add(confirm);
        choice.setPadding(new Insets(10));
        choice.setSpacing(20);
        newArtistList.getChildren().add(choice);
        ScrollPane artistScroller = new ScrollPane();
        artistScroller.setContent(newArtistList);
        newArtistList.setAlignment(Pos.CENTER);

        Scene artistAddScene = new Scene(artistScroller);
        artistAddScene.getStylesheets().add("Window-StyleSheet.css");
        this.currentStage.setScene(artistAddScene);
        this.currentStage.show();
    }

    public Genres stringToGenre(String genreString) {
        for (Genres genre : Genres.values()) {
            if (genre.getFancyName().equals(genreString)) {
                return genre;
            }
        }
        return null;
    }

    /**
     * This method checks whether the new Artist is valid or not.
     * If it isn't this method will notify the user what he/she needs to repair before submitting again.
     *
     * @param artistName
     * @param artistDescription
     * @param genre
     * @param image
     * @param description
     */
    public void newArtistControl(TextField artistName, TextArea artistDescription, String genre, Image image, TextArea description) {
        this.errorList.clear();
        if (artistName.getText().length() == 0) {
            this.errorList.add("The artist's name has not been filled in.");
        }
        if (artistDescription.getText().length() == 0) {
            this.errorList.add("The artist's description has not been filled in.");
        }
        if (this.errorList.isEmpty()) {
            try {
                DataController.getPlanner().addArtist(artistName.getText(), stringToGenre(genre), image, description.getText());
                this.currentStage.close();
            } catch (Exception e) {
                this.errorList.add("Failed to add the artist.");
                new ErrorWindow(this.currentStage, this.errorList);
            }
        } else {
            new ErrorWindow(this.currentStage, this.errorList);
        }
    }

}
