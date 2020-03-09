package GUILogic;

import Enumerators.Genres;
import PlannerData.Artist;
import PlannerData.Show;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.io.File;
import java.util.ArrayList;

public class ArtistWindow {

    private Stage upperStage;
    private Stage currentStage = new Stage();
    private Artist addedSartist;
    private ArrayList<String> errorList = new ArrayList<>();
    private Label information = new Label();
    private Artist selectedArtist;

    public ArtistWindow(int screenNumber, Stage upperStage) {
        this.upperStage = upperStage;
        this.currentStage.initOwner(this.upperStage);
        this.currentStage.initModality(Modality.WINDOW_MODAL);
        this.currentStage.setResizable(false);

        switch (screenNumber) {
            case 1:
                artistAddWindow();
                break;
            case 2:
                artistEditWindow();
                break;
            case 3:
                artistDeleteWindow();
                break;
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

        // Artist name
        Label artistNameLabel = new Label("Artist name:");
        TextField artistName = new TextField();
        artistName.setPrefWidth(250);
        newArtistList.getChildren().addAll(artistNameLabel, artistName);
        //newArtistList.getChildren().add(artistName);

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
        newArtistList.getChildren().addAll(artistDescriptionLabel, artistDescription);

        //buttons
        HBox choice = new HBox();
        Button stop = new Button("Cancel");
        stop.setOnAction(e -> this.currentStage.close());
        choice.getChildren().add(stop);

        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {
            if (newArtistControl(artistName, artistDescription, genreComboBox.getValue().toString(), artistDescription)) {
                try {
                    DataController.getPlanner().addArtist(artistName.getText(), Genres.getGenre(genreComboBox.getValue().toString()), artistDescription.getText());
                    this.currentStage.close();
                } catch (Exception event) {
                    this.errorList.add("Failed to add the artist.");
                    new ErrorWindow(this.currentStage, this.errorList);
                }
            }
        });

        choice.getChildren().add(confirmButton);

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
        this.currentStage.setWidth(275);
        this.currentStage.setHeight(400);

        VBox newArtistList = new VBox();
        Label startEdit = new Label("Which artist do you want to edit?");
        newArtistList.getChildren().add(startEdit);
        ComboBox artistComboBox = new ComboBox();

        artistComboBox.getItems().add("Select artist");
        for (Artist artist : DataController.getPlanner().getArtists()) {
            artistComboBox.getItems().add(artist.getName());
        }

        artistComboBox.getSelectionModel().selectFirst();

        newArtistList.getChildren().add(artistComboBox);
        newArtistList.setPrefWidth(250);

        //artist name
        Label artistNameLabel = new Label("Artist name:");
        TextField artistName = new TextField();
        artistName.setPrefWidth(250);
        newArtistList.getChildren().addAll(artistNameLabel, artistName);

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
        newArtistList.getChildren().addAll(artistDescriptionLabel, artistDescription);

        artistComboBox.setOnAction(event -> {
            if (!artistComboBox.getValue().equals("Select artist")) {
                this.selectedArtist = stringToArtist(artistComboBox.getValue().toString());
                artistName.setText(this.selectedArtist.getName());
                artistDescription.setText(this.selectedArtist.getDescription());
                genreComboBox.setValue(this.selectedArtist.getGenre().getFancyName());
            } else {
                artistName.setText("");
                artistDescription.setText("");
                genreComboBox.getSelectionModel().selectFirst();

            }
        });

        //buttons
        HBox choice = new HBox();
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> this.currentStage.close());
        choice.getChildren().add(cancelButton);

        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {
            if (newArtistControl(artistName, artistDescription, genreComboBox.getValue().toString(), artistDescription)) {
                try {
                    for (Show show : DataController.getPlanner().getShows()) {
                        for (Artist artist : show.getArtists()) {
                            if (artist.getName().equals(this.selectedArtist.getName())) {
                                artist.setName(artistName.getText());
                                artist.setDescription(artistDescription.getText());
                                artist.setGenre(Genres.getGenre(genreComboBox.getValue().toString()));
                            }
                        }
                    }

                    this.selectedArtist.setName(artistName.getText());
                    this.selectedArtist.setDescription(artistDescription.getText());
                    this.selectedArtist.setGenre(Genres.getGenre(genreComboBox.getValue().toString()));

                    DataController.getPlanner().savePlanner();
                    this.currentStage.close();
                } catch (Exception event) {
                    this.errorList.add("Failed to edit the artist.");
                    new ErrorWindow(this.currentStage, this.errorList);
                }
            }
        });

        choice.getChildren().add(confirmButton);

        choice.setPadding(new Insets(10));
        choice.setSpacing(20);
        newArtistList.getChildren().add(choice);

        ScrollPane artistScrollPane = new ScrollPane();
        artistScrollPane.setContent(newArtistList);
        newArtistList.setAlignment(Pos.CENTER);

        Scene artistAddScene = new Scene(artistScrollPane);
        artistAddScene.getStylesheets().add("Window-StyleSheet.css");
        this.currentStage.setScene(artistAddScene);

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
        artistComboBox.getItems().add("Select artist");
        for (Artist artist : DataController.getPlanner().getArtists()) {
            artistComboBox.getItems().add(artist.getName());
        }

        artistComboBox.getSelectionModel().selectFirst();
        artistComboBox.setOnAction(event -> {
            if (!artistComboBox.getValue().equals("Select artist")) {
                this.selectedArtist = stringToArtist(artistComboBox.getValue().toString());
                if (selectedArtist != null && !selectedArtist.getName().isEmpty()) {
                    this.information.textProperty().setValue("Do you want to delete the artist: " + selectedArtist.getName() + '\n' + " with the genre of " + selectedArtist.getGenre().getFancyName() + '\n' + " with the description: " + selectedArtist.getDescription());
                }
            } else {
                this.information.textProperty().setValue("         " + '\n');
                //something here?
            }
        });

        startLine.getChildren().add(artistComboBox);

        //buttons
        HBox choice = new HBox();
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> this.currentStage.close());
        choice.getChildren().add(cancelButton);

        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {
            if (ArtistDeleteChecker()) {
                try {
                    DataController.getPlanner().deleteArtist(artistComboBox.getValue().toString());
                    DataController.getPlanner().savePlanner();
                    this.currentStage.close();
                } catch (Exception exception) {
                    this.errorList.clear();
                    this.errorList.add("The artist could not be deleted.");
                    new ErrorWindow(this.currentStage, this.errorList);
                }
            }
        });

        choice.getChildren().add(confirmButton);

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

    public Artist stringToArtist(String artistString) {
        for (Artist artist : DataController.getPlanner().getArtists()) {
            if (artistString.equals(artist.getName())) {
                return artist;
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
     * @param description
     */
    public Boolean newArtistControl(TextField artistName, TextArea artistDescription, String genre, TextArea description) {
        this.errorList.clear();
        if (artistName.getText().length() == 0) {
            this.errorList.add("The artist's name has not been filled in.");
        } else {
            for (Artist artist : DataController.getPlanner().getArtists()) {
                if (this.selectedArtist != null) {
                    if (!this.selectedArtist.equals(artist) && artistName.getText().equals(artist.getName())) {
                        this.errorList.add("This Artist already exists.");
                    }
                } else {
                    if (artistName.getText().equals(artist.getName())) {
                        this.errorList.add("This Artist already exists.");
                    }
                }
            }
        }

        if (artistDescription.getText().length() == 0) {
            this.errorList.add("The artist's description has not been filled in.");
        }

        if (this.errorList.isEmpty()) {
            return true;
        }

        new ErrorWindow(this.currentStage, this.errorList);
        return false;
    }

    public Boolean ArtistDeleteChecker() {
        this.errorList.clear();
        for (Show show : DataController.getPlanner().getShows()) {
            for (Artist artist : show.getArtists()) {
                if (artist.getName().equals(this.selectedArtist.getName())) {
                    this.errorList.add("performing Artists cannot be removed from the event.");
                }
            }
        }
        if (this.errorList.isEmpty()) {
            return true;
        }

        new ErrorWindow(this.currentStage, this.errorList);
        return false;
    }
}