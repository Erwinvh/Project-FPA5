package GUILogic;

import Enumerators.Genres;
import PlannerData.Artist;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class AddingNewWindow {

    private Stage upperStage;
    private Stage currentStage = new Stage();
    private PlannerData.Stage addedStage;
    private ArrayList<String> errorList = new ArrayList<>();
    private FileChooser fileChooser = new FileChooser();
    private ImageView artistImage = new ImageView();
    private String imageURL = "PersonImageBase.jpg";
    private Boolean stageUpdate = false;
    private Label information = new Label();

    /**
     * This is the constructor of the base of the submenus.
     * This method also decides which submenu it should show to the user.
     *
     * @param screenNumber
     * @param upperStage
     */
    public AddingNewWindow(int screenNumber, Stage upperStage) {
        this.upperStage = upperStage;
        this.artistImage.setFitWidth(200);
        this.artistImage.setFitHeight(200);
        this.currentStage.initOwner(this.upperStage);
        this.currentStage.initModality(Modality.WINDOW_MODAL);
        this.currentStage.setResizable(false);

        if (screenNumber == 1) {
            artistAddWindow();
        } else if (screenNumber == 2) {
            artistEditWindow();
        } else if (screenNumber == 3) {
            artistDeleteWindow();
        } else if (screenNumber == 4) {
            stageAddWindow();
        } else if (screenNumber == 5) {
            stageEditWindow();
        } else if (screenNumber == 6) {
            stageDeleteWindow();
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

        Label stageNameLabel = new Label("Stage Name:");
        newStageList.getChildren().add(stageNameLabel);

        TextField stageName = new TextField();
        newStageList.getChildren().add(stageName);

        Label stageCapacityLabel = new Label("Stage Capacity:");
        newStageList.getChildren().add(stageCapacityLabel);
        TextField InputTextField = new TextField();
        newStageList.getChildren().add(InputTextField);

        HBox choice = new HBox();
        Button cancelButton = new Button("Cancel");
        choice.getChildren().add(cancelButton);
        cancelButton.setOnAction(e -> this.currentStage.close());

        Button confirm = new Button("Confirm");
        choice.getChildren().add(confirm);
        confirm.setOnAction(e -> newStageControl(stageName, InputTextField));

        choice.setPadding(new Insets(10));
        choice.setSpacing(20);

        newStageList.getChildren().add(choice);
        Scene artistAddScene = new Scene(newStageList);
        artistAddScene.getStylesheets().add("Window-StyleSheet.css");
        this.currentStage.setScene(artistAddScene);
        this.currentStage.show();
    }

    public void stageEditWindow() {
        this.currentStage.show();
    }

    public void stageDeleteWindow() {
        BorderPane structure = new BorderPane();
        HBox startLine = new HBox();
        startLine.getChildren().add(new Label("Choose the stage you want to delete:"));
        ComboBox stageBox = new ComboBox();
        stageBox.getItems().add("--Select--");
        for (PlannerData.Stage stage : DataController.getPlanner().getStages()) {
            stageBox.getItems().add(stage.getName());
        }
        stageBox.getSelectionModel().selectFirst();
        stageBox.setOnAction(event -> {
            if (!stageBox.getValue().equals("--Select--")) {
                PlannerData.Stage selectedStage = stringToStage(stageBox.getValue().toString());
                if (selectedStage != null && !selectedStage.getName().isEmpty() && selectedStage.getCapacity() > 0) {
                    this.information.textProperty().setValue("Do you want to delete the stage: " + selectedStage.getName() + '\n' + " with the capacity of " + selectedStage.getCapacity());
                }
            }
            else{
                this.information.textProperty().setValue("         "+'\n');
                //something here?
            }
        });

        startLine.getChildren().add(stageBox);
        structure.setTop(startLine);
        structure.setCenter(this.information);

        HBox choice = new HBox();
        Button cancelButton = new Button("Cancel");
        choice.getChildren().add(cancelButton);
        cancelButton.setOnAction(e -> this.currentStage.close());

        Button confirm = new Button("Confirm");
        choice.getChildren().add(confirm);
        confirm.setOnAction(e -> {});

        choice.setPadding(new Insets(10));
        choice.setSpacing(20);
        structure.setBottom(choice);
        Scene stageDeleteScene = new Scene(structure);
        stageDeleteScene.getStylesheets().add("Window-StyleSheet.css");
        this.currentStage.setScene(stageDeleteScene);

        this.currentStage.show();
    }

    public PlannerData.Stage stringToStage(String stageString) {
        if (stageString == null || stageString.isEmpty()) {
            return null;
        }
        for (PlannerData.Stage stage : DataController.getPlanner().getStages()) {
            if (stageString.equals(stage.getName())) {
                return stage;
            }
        }
        return null;
    }

    public Artist stringToArtist(String artistString) {
        for (Artist artist : DataController.getPlanner().getArtists()) {
            if (artistString.equals(artist.getName())) {
                return artist;
            }
        }
        return null;
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
        else{
            for (PlannerData.Stage stage : DataController.getPlanner().getStages()) {
                if (stageName.getText().equals(stage.getName())){
                    this.errorList.add("This Stage already exists.");
                }
            }
        }

        if (capacity.getText().length() == 0) {
            this.errorList.add("The capacity has not been filled in.");
        } else {
            try {
                int capacityCheck = Integer.parseInt(capacity.getText());
                if (capacityCheck < 20) {
                    this.errorList.add("The capacity must be at least 20");
                }

            } catch (Exception e) {
                this.errorList.add("The capacity must be a number.");
            }
        }

        if (this.errorList.isEmpty()) {
            this.addedStage = new PlannerData.Stage(Integer.parseInt(capacity.getText()), stageName.getText());
            DataController.getPlanner().addStage(this.addedStage);
            this.stageUpdate = true;
            this.currentStage.close();
        } else {
            new ErrorWindow(this.currentStage, this.errorList);
        }
    }

    public PlannerData.Stage getAddedStage() {
        return addedStage;
    }

    public Boolean stageUpdate(){
        return this.stageUpdate;
    }

    public void resetUpdate(){
        this.stageUpdate = false;
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

        //artist description
        Label artistDescriptionLabel = new Label("Artist's description:");
        TextArea artistDescription = new TextArea();
        artistDescription.setPrefWidth(250);
        newArtistList.getChildren().add(artistDescriptionLabel);
        newArtistList.getChildren().add(artistDescription);

        //buttons
        HBox choice = new HBox();
        Button stop = new Button("Cancel");
        stop.setOnAction(e -> this.currentStage.close());
        choice.getChildren().add(stop);

        Button confirm = new Button("Confirm");
        confirm.setOnAction(e -> newArtistControl(artistName, artistDescription, genreComboBox.getValue().toString(), artistDescription));
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


    public void artistEditWindow() {
        this.currentStage.show();
    }

    public void artistDeleteWindow() {
        this.currentStage.setWidth(275);
        this.currentStage.setHeight(400);

        VBox newArtistList = new VBox();
        newArtistList.setPrefWidth(250);
        HBox startLine = new HBox();
        startLine.getChildren().add(new Label("Choose the artist you want to delete:"));


        ComboBox artistComboBox = new ComboBox();
        artistComboBox.getItems().add("--Select--");
        for (Artist artist : DataController.getPlanner().getArtists()) {
            artistComboBox.getItems().add(artist.getName());
        }

        artistComboBox.getSelectionModel().selectFirst();
        artistComboBox.setOnAction(event -> {
            if (!artistComboBox.getValue().equals("--Select--")) {
                PlannerData.Artist selectedArtist = stringToArtist(artistComboBox.getValue().toString());
                if (selectedArtist != null && !selectedArtist.getName().isEmpty()) {
                    this.information.textProperty().setValue("Do you want to delete the artist: " + selectedArtist.getName() + '\n' + " with the genre of " + selectedArtist.getGenre().getFancyName()+ '\n' + " with the description: " + selectedArtist.getDescription() );
                }
            }
            else{
                this.information.textProperty().setValue("         "+'\n');
                //something here?
            }
        });

        startLine.getChildren().add(artistComboBox);

        //buttons
        HBox choice = new HBox();
        Button stop = new Button("Cancel");
        stop.setOnAction(e -> this.currentStage.close());
        choice.getChildren().add(stop);

        Button confirm = new Button("Confirm");
        confirm.setOnAction(e -> {
            try{
                DataController.getPlanner().deleteArtist(artistComboBox.getValue().toString());
                this.currentStage.close();
            }
            catch(Exception exception){
                this.errorList.clear();
                this.errorList.add("The artist could not be deleted.");
                new ErrorWindow(this.currentStage, this.errorList);
            }

        });
        choice.getChildren().add(confirm);

        choice.setPadding(new Insets(10));
        choice.setSpacing(20);
        newArtistList.getChildren().add(choice);

        BorderPane structure = new BorderPane();
        structure.setTop(startLine);
        structure.setCenter(this.information);
        structure.setBottom(choice);

        Scene artistDeleteScene = new Scene(structure);
        artistDeleteScene.getStylesheets().add("Window-StyleSheet.css");
        this.currentStage.setScene(artistDeleteScene);
        this.currentStage.show();
    }

    public ArrayList<String> getArtistImages() {
        ArrayList<String> imagesStrings = new ArrayList<>();

        File directory = new File("Resources/Artist_Images");
        File[] filesArray = directory.listFiles();

        for (File file : filesArray){
            if (file.isFile() &&
                    (file.getName().contains(".png") ||
                            file.getName().contains(".jpg") ||
                            file.getName().contains(".jpeg"))){
                imagesStrings.add(file.getName());
            }
        }
        return imagesStrings;
    }

    public String getFileName(String fileName){
        String returnString = "Not an imageFile with .jpg, .jpeg or .png!";
        if (fileName.contains(".jpg")){
            return fileName.substring(0,fileName.indexOf(".jpg"));
        } else if (fileName.contains(".jpeg")){
            return fileName.substring(0,fileName.indexOf(".jpeg"));
        } else if (fileName.contains(".png")){
            return fileName.substring(0,fileName.indexOf(".png"));
        }

        return returnString;
    }

    /**
     * This method checks whether the new Artist is valid or not.
     * If it isn't this method will notify the user what he/she needs to repair before submitting again.
     *
     * @param artistName
     * @param artistDescription
     * @param genre
     * @param description
     */
    public void newArtistControl(TextField artistName, TextArea artistDescription, String genre, TextArea description) {
        this.errorList.clear();
        if (artistName.getText().length() == 0) {
            this.errorList.add("The artist's name has not been filled in.");
        }
        else{
            for (Artist artist : DataController.getPlanner().getArtists()) {
                if (artistName.getText().equals(artist.getName())){
                    this.errorList.add("This Artist already exists.");
                }
            }
        }

        if (artistDescription.getText().length() == 0) {
            this.errorList.add("The artist's description has not been filled in.");
        }

        if (this.errorList.isEmpty()) {
            try {
                DataController.getPlanner().addArtist(artistName.getText(), Genres.getGenre(genre), description.getText());
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