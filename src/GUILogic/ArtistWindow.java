package GUILogic;

import Enumerators.Genres;
import PlannerData.Artist;
import PlannerData.Show;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ArtistWindow {

    private Stage currParentStage;
    private Stage currStage = new Stage();
    private ArrayList<String> errorList = new ArrayList<>();
    private Label artistDeleteText = new Label();
    private Artist selectedArtist;

    public ArtistWindow(int screenNumber, Stage currParentStage) {
        this.currParentStage = currParentStage;
        this.currStage.initOwner(this.currParentStage);
        this.currStage.initModality(Modality.WINDOW_MODAL);
        this.currStage.setResizable(false);

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
        this.currStage.setWidth(275);
        this.currStage.setHeight(400);

        VBox newArtistList = new VBox();
        newArtistList.setPrefWidth(250);

        // Artist name
        Label artistNameLabel = new Label("Artist name:");
        TextField artistName = new TextField();
        artistName.setPrefWidth(250);

        //genre
        Label artistGenreLabel = new Label("Artist's genres:");
        ComboBox genreComboBox = new ComboBox();
        genreComboBox.getItems().add("None");
        for (Genres genre : Enumerators.Genres.values()) {
            genreComboBox.getItems().add(genre.getFancyName());
        }

        //artist getShowDescription
        Label artistDescriptionLabel = new Label("Artist's description:");
        TextArea artistDescription = new TextArea();
        artistDescription.setPrefWidth(250);

        newArtistList.getChildren().addAll(artistNameLabel, artistName, artistGenreLabel, genreComboBox, artistDescriptionLabel, artistDescription);

        //buttons
        HBox cancelConfirmButtons = new HBox();
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> this.currStage.close());
        cancelConfirmButtons.getChildren().add(cancelButton);

        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {
            if (canAddArtist(artistName, artistDescription, genreComboBox.getValue().toString(), artistDescription)) {
                try {
                    DataController.getPlanner().addArtist(artistName.getText(), Genres.getGenre(genreComboBox.getValue().toString()), artistDescription.getText());
                    this.currStage.close();
                } catch (Exception event) {
                    this.errorList.add("Failed to add the artist.");
                    new ErrorWindow(this.currStage, this.errorList);
                }
            }
        });

        cancelConfirmButtons.getChildren().add(confirmButton);

        cancelConfirmButtons.setPadding(new Insets(10));
        cancelConfirmButtons.setSpacing(20);

        newArtistList.getChildren().add(cancelConfirmButtons);

        ScrollPane artistScroller = new ScrollPane();
        artistScroller.setContent(newArtistList);
        newArtistList.setAlignment(Pos.CENTER);

        Scene artistAddScene = new Scene(artistScroller);
        artistAddScene.getStylesheets().add("Window-StyleSheet.css");
        this.currStage.setScene(artistAddScene);
        this.currStage.show();
    }

    public void artistEditWindow() {
        this.currStage.setWidth(275);
        this.currStage.setHeight(400);

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
        Label artistGenreLabel = new Label("Artist's genres:");
        newArtistList.getChildren().add(artistGenreLabel);
        ComboBox genreComboBox = new ComboBox();
        genreComboBox.getItems().add("None");

        for (Genres genre : Enumerators.Genres.values()) {
            genreComboBox.getItems().add(genre.getFancyName());
        }

        newArtistList.getChildren().add(genreComboBox);

        //artist getShowDescription
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
        cancelButton.setOnAction(e -> this.currStage.close());
        choice.getChildren().add(cancelButton);

        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {
            if (canAddArtist(artistName, artistDescription, genreComboBox.getValue().toString(), artistDescription)) {
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
                    this.currStage.close();
                } catch (Exception event) {
                    this.errorList.add("Failed to edit the artist.");
                    new ErrorWindow(this.currStage, this.errorList);
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
        this.currStage.setScene(artistAddScene);

        this.currStage.show();
    }

    public void artistDeleteWindow() {
        this.currStage.setWidth(275);
        this.currStage.setHeight(400);

        VBox newArtistList = new VBox();
        newArtistList.setPrefWidth(250);
        HBox header = new HBox();
        header.getChildren().add(new Label("Choose the artist you want to delete:"));

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
                    this.artistDeleteText.textProperty().setValue("Do you want to delete the artist: " + selectedArtist.getName() + '\n' + " with the genre of " + selectedArtist.getGenre().getFancyName() + '\n' + " with the description: " + selectedArtist.getDescription());
                }
            } else {
                this.artistDeleteText.textProperty().setValue("         " + '\n');
                // TODO: Fix deleting artist when nothing is selected
            }
        });

        header.getChildren().add(artistComboBox);

        //buttons
        HBox cancelConfirmButtons = new HBox();
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> this.currStage.close());
        cancelConfirmButtons.getChildren().add(cancelButton);

        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {
            if (canDeleteArtist()) {
                try {
                    DataController.getPlanner().deleteArtist(artistComboBox.getValue().toString());
                    DataController.getPlanner().savePlanner();
                    this.currStage.close();
                } catch (Exception exception) {
                    this.errorList.clear();
                    this.errorList.add("The artist could not be deleted.");
                    new ErrorWindow(this.currStage, this.errorList);
                }
            }
        });

        cancelConfirmButtons.getChildren().add(confirmButton);

        cancelConfirmButtons.setPadding(new Insets(10));
        cancelConfirmButtons.setSpacing(20);
        newArtistList.getChildren().add(cancelConfirmButtons);

        BorderPane structure = new BorderPane();
        structure.setTop(header);
        structure.setCenter(this.artistDeleteText);
        structure.setBottom(cancelConfirmButtons);

        Scene artistDeleteScene = new Scene(structure);
        artistDeleteScene.getStylesheets().add("Window-StyleSheet.css");
        this.currStage.setScene(artistDeleteScene);
        this.currStage.show();
    }

    // TODO: Move this logic perhaps to a static function in the artist class itself
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
    public boolean canAddArtist(TextField artistName, TextArea artistDescription, String genre, TextArea description) {
        this.errorList.clear();

        if (artistName.getText().length() == 0) {
            this.errorList.add("The artist's name has not been filled in.");
        } else {

            for (Artist artist : DataController.getPlanner().getArtists()) {
                if (this.selectedArtist != null) {
                    if (!this.selectedArtist.equals(artist) && artistName.getText().equals(artist.getName())) {
                        this.errorList.add("This artist already exists.");
                    }
                } else {
                    if (artistName.getText().equals(artist.getName())) {
                        this.errorList.add("This artist already exists.");
                    }
                }
            }
        }

        if (artistDescription.getText().length() == 0)
            this.errorList.add("The artist's description has not been filled in.");

        if (this.errorList.isEmpty())
            return true;

        new ErrorWindow(this.currStage, this.errorList);
        return false;
    }

    public boolean canDeleteArtist() {
        this.errorList.clear();
        for (Show show : DataController.getPlanner().getShows()) {
            for (Artist artist : show.getArtists()) {
                if (artist.getName().equals(this.selectedArtist.getName())) {
                    this.errorList.add("Artists who are performing cannot be removed from the event.");
                }
            }
        }

        if (this.errorList.isEmpty())
            return true;

        new ErrorWindow(this.currStage, this.errorList);
        return false;
    }
}