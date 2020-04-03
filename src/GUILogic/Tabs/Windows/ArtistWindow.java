package GUILogic.Tabs.Windows;

import Enumerators.Genres;
import GUILogic.DataController;
import GUILogic.Tabs.Windows.ErrorWindow;
import GUILogic.Tabs.ScheduleTab;
import PlannerData.Artist;
import PlannerData.Planner;
import PlannerData.Show;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ArtistWindow {

    private Stage currStage = new Stage();
    private ArrayList<String> errorList = new ArrayList<>();
    private Label artistDeleteText = new Label();
    private Artist selectedArtist;
    private ScheduleTab ST;
    private VBox WindowStructure;
    private TextField artistName;
    private Label artistGenreLabel = new Label("Artist genre:");
    private ComboBox genreComboBox = new ComboBox();
    private Planner plannerReference;
    private TextArea artistDescription;
    private Button Cancel;

    /**
     * The constructor of the artist submenu windows
     * This is also where the specific submenu is chosen
     *
     * @param screenNumber    submenu selection number
     * @param currParentStage Current parent stage
     * @param ST              The schedule tab
     */
    public ArtistWindow(int screenNumber, Stage currParentStage, ScheduleTab ST) {
        this.ST = ST;
        this.currStage.initOwner(currParentStage);
        this.currStage.initModality(Modality.WINDOW_MODAL);
        this.currStage.setResizable(false);
        this.currStage.getIcons().add(new Image("logoA5.jpg"));

        this.currStage.setWidth(275);
        this.currStage.setHeight(450);
        this.plannerReference = DataController.getInstance().getPlanner();
        this.WindowStructure = new VBox();
        this.WindowStructure.setPrefWidth(250);
        this.WindowStructure.setAlignment(Pos.CENTER);
        this.Cancel = new Button("Cancel");
        this.Cancel.setOnAction(e -> this.currStage.close());
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
     * This methode sets up the base for the add and edit artist window.
     */
    public void InitialSetup(){
        Label artistNameLabel = new Label("Artist name:");
        this.artistName = new TextField();
        genreComboBox.getItems().add("Select");
        artistName.setPrefWidth(250);
        genreComboBox.getSelectionModel().selectFirst();
        for (Genres genre : Enumerators.Genres.values()) {
            genreComboBox.getItems().add(genre.getFancyName());
        }
        Label artistDescriptionLabel = new Label("Artist's description:");
        artistDescription = new TextArea();
        artistDescription.setPrefWidth(250);
        artistDescription.setPrefHeight(200);
        this.WindowStructure.getChildren().addAll(artistNameLabel, artistName, artistGenreLabel, genreComboBox, artistDescriptionLabel, artistDescription);

    }

    /**
     * This method sets the submenu to the Add Artist submenu.
     * The user will be able to fill in all the required fields for an unknown artist.
     */
    private void artistAddWindow() {
        this.currStage.setTitle("Add artist");

InitialSetup();

        //buttons
        HBox cancelConfirmButtons = new HBox();
        cancelConfirmButtons.getChildren().add(this.Cancel);

        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {
            if (canAddArtist(artistName, artistDescription, genreComboBox.getValue().toString())) {
                try {
                    plannerReference.addArtist(artistName.getText(), Genres.getGenre(genreComboBox.getValue().toString()), artistDescription.getText());
                    plannerReference.savePlanner();
                    this.currStage.close();
                } catch (Exception event) {
                    this.errorList.add("Failed to add the artist.");
                    new ErrorWindow(this.currStage, this.errorList);
                }
            }
        });

        ScrollPane artistScroller = new ScrollPane();
        artistScroller.setContent(this.WindowStructure);
        finalizeSetup(cancelConfirmButtons, confirmButton);
    }

    /**
     * The further setup of the submenu for editing an artist
     */
    private void artistEditWindow() {
        this.currStage.setTitle("Edit artist");

        Label startEdit = new Label("Which artist do you want to edit?");
        this.WindowStructure.getChildren().add(startEdit);
        ComboBox artistComboBox = new ComboBox();
        artistComboBox.getItems().add("Select artist");
        for (Artist artist : plannerReference.getArtists()) {
            artistComboBox.getItems().add(artist.getName());
        }
        artistComboBox.getSelectionModel().selectFirst();
        this.WindowStructure.getChildren().add(artistComboBox);

InitialSetup();

        artistComboBox.setOnAction(event -> {
            if (!artistComboBox.getValue().equals("Select artist")) {
                this.selectedArtist = plannerReference.getArtist(artistComboBox.getValue().toString());
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
            if (!artistComboBox.getValue().toString().equals("Select artist")) {
                if (canAddArtist(artistName, artistDescription, genreComboBox.getValue().toString())) {
                    try {
                        for (Show show : plannerReference.getShows()) {
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

                        plannerReference.savePlanner();
                        ST.resetData();
                        this.currStage.close();
                    } catch (Exception event) {
                        this.errorList.add("Failed to edit the artist.");
                        new ErrorWindow(this.currStage, this.errorList);
                    }
                }
            } else {
                this.errorList.clear();
                this.errorList.add("No artist has been selected.");
                new ErrorWindow(this.currStage, this.errorList);
            }
        });

        choice.setPadding(new Insets(10));
        choice.setSpacing(20);

        ScrollPane artistScrollPane = new ScrollPane();
        artistScrollPane.setContent(this.WindowStructure);
        this.WindowStructure.setAlignment(Pos.CENTER);


        finalizeSetup(choice,confirmButton);
    }

    /**
     * The further setup for the submenu to delete an artist
     */
    private void artistDeleteWindow() {
        this.currStage.setTitle("Delete artist");

        HBox header = new HBox();
        header.getChildren().add(new Label("Choose the artist you want to delete:"));

        ComboBox artistComboBox = new ComboBox();
        artistComboBox.getItems().add("Select artist");
        for (Artist artist : plannerReference.getArtists()) {
            artistComboBox.getItems().add(artist.getName());
        }

        artistComboBox.getSelectionModel().selectFirst();
        artistComboBox.setOnAction(event -> {
            if (!artistComboBox.getValue().equals("Select artist")) {
                this.selectedArtist = plannerReference.getArtist(artistComboBox.getValue().toString());
                if (selectedArtist != null && !selectedArtist.getName().isEmpty()) {
                    this.artistDeleteText.textProperty().setValue("Do you want to delete the artist: " + selectedArtist.getName() + '\n' + " with the genre of " + selectedArtist.getGenre().getFancyName() + '\n' + " with the description: " + selectedArtist.getDescription());
                }
            } else {
                this.artistDeleteText.textProperty().setValue("         " + '\n');
            }
        });

        header.getChildren().add(artistComboBox);

        //buttons
        HBox cancelConfirmButtons = new HBox();
        cancelConfirmButtons.getChildren().add(this.Cancel);
        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {
            if (!artistComboBox.getValue().toString().equals("Select artist")) {
                if (canDeleteArtist()) {
                    try {
                        plannerReference.deleteArtist(artistComboBox.getValue().toString());
                        plannerReference.savePlanner();
                        this.currStage.close();
                    } catch (Exception exception) {
                        this.errorList.clear();
                        this.errorList.add("The artist could not be deleted.");
                        new ErrorWindow(this.currStage, this.errorList);
                    }
                }
            } else {
                this.errorList.clear();
                this.errorList.add("No artist has been set to delete");
                new ErrorWindow(this.currStage, this.errorList);
            }

        });

        this.WindowStructure.getChildren().add(header);
        this.WindowStructure.getChildren().add(this.artistDeleteText);

        finalizeSetup(cancelConfirmButtons, confirmButton );
    }

    /**
     * This method finishes the artist windows and allow the user to see them.
     * @param choices The HBox with the cancel and confirm buttons
     * @param confirm The confirm button
     */
    public void finalizeSetup(HBox choices,Button confirm){
        choices.setPadding(new Insets(10));
        choices.setSpacing(20);
        choices.getChildren().add(confirm);
        this.WindowStructure.getChildren().add(choices);
        Scene artistScene = new Scene(this.WindowStructure);
        artistScene.getStylesheets().add("Window-StyleSheet.css");
        this.currStage.setScene(artistScene);
        this.currStage.show();
    }

    /**
     * This method checks whether the new Artist is valid or not.
     * If it isn't this method will notify the user what he/she needs to repair before submitting again.
     *
     * @param artistName
     * @param artistDescription
     * @param genre
     */
    private boolean canAddArtist(TextField artistName, TextArea artistDescription, String genre) {
        this.errorList.clear();

        if (artistName.getText().length() == 0) {
            this.errorList.add("The artist's name has not been filled in.");
        } else {
            for (Artist artist : plannerReference.getArtists()) {
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
        if (genre == null || genre.equals("Select")) {
            this.errorList.add("The genre has not been filled in.");
        }

        if (artistDescription.getText().length() == 0)
            this.errorList.add("The artist's description has not been filled in.");

        if (this.errorList.isEmpty())
            return true;

        new ErrorWindow(this.currStage, this.errorList);
        return false;
    }

    /**
     * A method that checks whether an artist can be deleted or if it is being used in a show
     * If it is used in a show it will return false, else it will return true
     *
     * @return A true or false value
     */
    private boolean canDeleteArtist() {
        this.errorList.clear();
        for (Show show : plannerReference.getShows()) {
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