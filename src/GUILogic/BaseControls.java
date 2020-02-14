package GUILogic;

import Enumerators.Genres;
import PlannerData.Artist;
import PlannerData.Show;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.util.ArrayList;

public class BaseControls {

    private Stage upperStage;
    private Show selectedShow;
    private ArrayList<String> errorList = new ArrayList<>();
    private ArrayList<String> timeList = new ArrayList<>();
    private int additionalArtists = 0;
    private Button cancel = new Button("Cancel");
    private Stage popUp = new Stage();
    private TableView<Show> table;
    private ObservableList<Show> data;
    private int stagePopularity = 0;
    private Slider popularitySlider = new Slider();
    private Show addedShow;

    /**
     * This is the constructor of the base layout of the windows of the three Menus.
     * The method also sends the user to the correct menu window.
     *
     * @param screenNumber
     * @param upperStage
     * @param data
     * @param table
     * @param selectedShow
     */
    public BaseControls(int screenNumber, Stage upperStage, javafx.collections.ObservableList<Show> data, TableView<Show> table, Show selectedShow) {
        this.upperStage = upperStage;
        this.table = table;
        this.data = data;
        this.selectedShow = selectedShow;
        this.popUp.setWidth(400);
        this.popUp.setHeight(450);
        this.popUp.initOwner(this.upperStage);
        this.popUp.initModality(Modality.WINDOW_MODAL);

        try {
            this.stagePopularity = this.selectedShow.getStage().getCapacity();
        } catch (Exception e) {

        }

        cancelsetup();
        if (screenNumber == 1) {
            additionWindow();
        } else if (screenNumber == 2) {
            editoryWindow();
        } else {
            deletionWindow();
        }
    }

    /**
     * This method allows the user to Add a new Show.
     */
    public void additionWindow() {
        this.additionalArtists = 0;
        BorderPane structure = new BorderPane();

        Label addingNew = new Label("What show do you want to add?");
        structure.setTop(addingNew);

        GridPane inputStructure = new GridPane();
        inputStructure.setHgap(10);
        inputStructure.setVgap(10);

        //showname
        Label showName = new Label("Show name:");
        TextField inputShowName = new TextField();
        inputStructure.add(showName, 1, 1);
        inputStructure.add(inputShowName, 2, 1);

        //time
        inputStructure.add(new Label("Begin time:"), 1, 2);
        inputStructure.add(new Label("End time:"), 1, 3);
        ComboBox startingTime = timeBox();
        ComboBox endingTime = timeBox();
        inputStructure.add(startingTime, 2, 2);
        inputStructure.add(endingTime, 2, 3);

        //stage
        inputStructure.add(new Label("Stage:"), 1, 4);
        ComboBox stage = StageBox();
        inputStructure.add(stage, 2, 4);

        //genre
        inputStructure.add(new Label("Genre:"), 1, 5);
        ComboBox genre = genreBox();
        inputStructure.add(genre, 2, 5);

        //popularity
        inputStructure.add(new Label("Popularity:"), 1, 6);
        this.popularitySlider.setMin(0);
        this.popularitySlider.setMax(100);
        this.popularitySlider.setValue(0);
        this.popularitySlider.setShowTickLabels(true);
        this.popularitySlider.setShowTickMarks(true);
        this.popularitySlider.setMajorTickUnit(25);
        this.popularitySlider.setMinorTickCount(5);
        this.popularitySlider.setBlockIncrement(10);
        inputStructure.add(this.popularitySlider, 2, 6);
        Label PopularityLabel = new Label("");
        this.popularitySlider.valueProperty().addListener((observableValue, oldValue, newValue) -> PopularityLabel.textProperty().setValue(String.valueOf((newValue.intValue()))));
        PopularityLabel.textProperty().setValue("0");
        inputStructure.add(PopularityLabel, 3, 6);

        //artists
        inputStructure.add(new Label("Artists:"), 1, 7);
        ComboBox artists = artistBox();
        //add more artists
        Button showArtistAdder = new Button("+");
        VBox ArtistAddList = new VBox();
        ArtistAddList.getChildren().add(artists);
        inputStructure.add(showArtistAdder, 3, 7);
        showArtistAdder.setOnAction(event -> {
            ComboBox artistAdded = artistBox();
            artistAdded.getItems().add("None");
            ArtistAddList.getChildren().add(artistAdded);
            artistAdded.setOnAction(e -> {
                if (artistAdded.getValue().equals("Add new Artist")) {
                    new AddingNewWindow(2, this.popUp);
                    artistAdded.getSelectionModel().selectFirst();
                } else if (artistAdded.getValue().equals("None")) {
                    ArtistAddList.getChildren().remove(artistAdded);
                }
            });
        });
        inputStructure.add(ArtistAddList, 2, 7);

        //Description
        VBox StructureTwo = new VBox();
        StructureTwo.getChildren().add(inputStructure);
        StructureTwo.getChildren().add(new Label("Show description:"));
        TextArea descriptionTextArea = ShowDescription("", 200, 250);
        StructureTwo.getChildren().add(descriptionTextArea);

        ScrollPane artistScroller = new ScrollPane();
        artistScroller.setContent(StructureTwo);
        structure.setCenter(artistScroller);
        //buttons
        HBox choice = new HBox();
        Button submit = new Button("Submit");
        submit.setOnAction(event -> {
            ArrayList<Artist> addedArtists = new ArrayList<>();
//            if (artists.getSelectionModel().getSelectedItem() != null) {
//                String comboBoxString = artists.getSelectionModel().getSelectedItem().toString();
//                boolean containsArtist = false;
//                for (Artist artist : addedArtists) {
//                    if (artist.getName().equals(comboBoxString)) {
//                        containsArtist = true;
//                    }
//                }
//                if (!containsArtist) {
//                    Artist artist = stringToArtist(comboBoxString);
//                    if (artist != null) {
//                        addedArtists.add(artist);
//                    }
//                }
//            } else {
//                addedArtists = null;
//            }
            for (Node artistBox : ArtistAddList.getChildren()) {
                ComboBox artistCombo = (ComboBox) artistBox;
                if (artistCombo.getSelectionModel().getSelectedItem() != null) {
                    String comboBoxString = artistCombo.getSelectionModel().getSelectedItem().toString();
                    boolean containsArtist = false;
                    for (Artist artist : addedArtists) {
                        if (artist.getName().equals(comboBoxString)) {
                            containsArtist = true;
                        }
                    }
                    if (!containsArtist) {
                        Artist artist = stringToArtist(comboBoxString);
                        if (artist != null) {
                            addedArtists.add(artist);
                        }
                    }
                } else {
                    addedArtists = null;
                }
            }

            if (control(startingTime, endingTime, stage, genre, inputShowName, artists)) {
                String showNameAdding = inputShowName.getText();
                LocalTime beginTime = indexToLocalTime(this.timeList.indexOf(startingTime.getValue()));
                LocalTime endTime = indexToLocalTime(this.timeList.indexOf(endingTime.getValue()));
                PlannerData.Stage stageAdded = stringToStage((String) stage.getValue());
                Genres addedGenre = Genres.getGenre(genre.getValue().toString());
                ArrayList<Genres> genres = new ArrayList<>();
                genres.add(addedGenre);
                String descriptionShow = descriptionTextArea.getText();
                int popularityAdded = (int) this.popularitySlider.getValue();

                Show show = new Show(beginTime, endTime, addedArtists, showNameAdding, stageAdded, descriptionShow, genres, popularityAdded);
                if (!DataController.getPlanner().getShows().contains(show)) {
                    for (Show existingShow : DataController.getPlanner().getShows()) {
                        if (existingShow.getStage().getName().equals(show.getStage().getName())) {
                            if (show.getBeginTime().isAfter(existingShow.getBeginTime()) && show.getBeginTime().isBefore(existingShow.getEndTime()) || show.getBeginTime().equals(existingShow.getBeginTime())) {
                                return;
                            }

                            if (show.getEndTime().isAfter(existingShow.getBeginTime()) && show.getEndTime().isBefore(existingShow.getEndTime()) || show.getEndTime().equals(existingShow.getEndTime())) {
                                return;
                            }
                        }

                        for (Artist existingArtist : existingShow.getArtists()) {
                            for (Artist showArtist : show.getArtists()) {
                                if (existingArtist.getName().equals(showArtist.getName())) {
                                    if (show.getBeginTime().isAfter(existingShow.getBeginTime()) && show.getBeginTime().isBefore(existingShow.getEndTime()) || show.getBeginTime().equals(existingShow.getBeginTime())) {
                                        return;
                                    }

                                    if (show.getEndTime().isAfter(existingShow.getBeginTime()) && show.getEndTime().isBefore(existingShow.getEndTime()) || show.getEndTime().equals(existingShow.getEndTime())) {
                                        return;
                                    }
                                }
                            }
                        }
                    }

                    DataController.getPlanner().addShow(show);
                    this.data.add(show);
                    this.popUp.close();
                }

            } else {
                new ErrorWindow(this.popUp, this.errorList);
            }
        });

        choice.getChildren().add(this.cancel);
        choice.getChildren().add(submit);
        choice.setPadding(new Insets(10));
        choice.setSpacing(20);
        structure.setBottom(choice);

        Scene adderScene = new Scene(structure);
        adderScene.getStylesheets().add("Window-StyleSheet.css");
        this.popUp.setScene(adderScene);
        this.popUp.show();
    }

    /**
     * This method allows the user to edit the selected show.
     */
    public void editoryWindow() {
        BorderPane structure = new BorderPane();

        //titel
        Label editingThis = new Label("Edit this show:");
        structure.setTop(editingThis);

        GridPane inputStructure = new GridPane();
        inputStructure.setHgap(10);
        inputStructure.setVgap(10);

        //show name
        Label showName = new Label("Show name:");
        TextField inputShowName = new TextField(this.selectedShow.getName());
        inputStructure.add(showName, 1, 1);
        inputStructure.add(inputShowName, 2, 1);

        //time
        inputStructure.add(new Label("Begin time:"), 1, 2);
        inputStructure.add(new Label("End time:"), 1, 3);
        ComboBox startingTime = timeBox();
        ComboBox endingTime = timeBox();
        startingTime.getSelectionModel().select(LocalTimeToindex(this.selectedShow.getBeginTime()));
        endingTime.getSelectionModel().select(LocalTimeToindex(this.selectedShow.getEndTime()));
        inputStructure.add(startingTime, 2, 2);
        inputStructure.add(endingTime, 2, 3);

        //stage
        inputStructure.add(new Label("Stage:"), 1, 4);
        ComboBox stage = StageBox();
        stage.setValue(this.selectedShow.getStage().getName());
        inputStructure.add(stage, 2, 4);

        //Genre
        inputStructure.add(new Label("Genre:"), 1, 5);
        ComboBox genre = genreBox();
        genre.setValue(this.selectedShow.getGenre().get(0).getFancyName());
        inputStructure.add(genre, 2, 5);

        //popularity
        inputStructure.add(new Label("Popularity:"), 1, 6);
        this.popularitySlider.setMin(0);
        this.popularitySlider.setMax(this.stagePopularity);
        this.popularitySlider.setValue(this.selectedShow.getExpectedPopularity());
        this.popularitySlider.setShowTickLabels(true);
        this.popularitySlider.setShowTickMarks(true);
        this.popularitySlider.setMajorTickUnit(50);
        this.popularitySlider.setMinorTickCount(5);
        this.popularitySlider.setBlockIncrement(10);
        inputStructure.add(this.popularitySlider, 2, 6);
        Label PopularityLabel = new Label("");
        this.popularitySlider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldValue,
                    Number newValue) {
                PopularityLabel.textProperty().setValue(
                        String.valueOf(newValue.intValue()));
            }
        });
        PopularityLabel.textProperty().setValue("" + this.selectedShow.getExpectedPopularity());
        inputStructure.add(PopularityLabel, 3, 6);

        //artists
        inputStructure.add(new Label("Artists:"), 1, 7);
        ComboBox artists = artistBox();
        artists.setValue(this.selectedShow.getArtists().get(0).getName());
        //add more artists
        Button showArtistAdder = new Button("+");
        VBox ArtistAddList = new VBox();
        ArtistAddList.getChildren().add(artists);
        for (int i = 1; i < this.selectedShow.getArtists().size(); i++) {
            ComboBox addedArtist = artistBox();
//            addedArtist.
            addedArtist.setValue(this.selectedShow.getArtists().get(i).getName());
            ArtistAddList.getChildren().add(addedArtist);
            addedArtist.getItems().add("None");
            addedArtist.setOnAction(e -> {
                if (addedArtist.getValue().equals("Add new Artist")) {
                    new AddingNewWindow(2, this.popUp);
                    addedArtist.getSelectionModel().selectFirst();
                }

                if (addedArtist.getValue().equals("None")) {
                    ArtistAddList.getChildren().remove(addedArtist);
                }
            });
        }

        inputStructure.add(showArtistAdder, 3, 7);
        showArtistAdder.setOnAction(event -> {
            ComboBox artistAdded = artistBox();
            artistAdded.getItems().add("None");
            ArtistAddList.getChildren().add(artistAdded);
            artistAdded.setOnAction(e -> {
                if (artistAdded.getValue().equals("Add new Artist")) {
                    new AddingNewWindow(2, this.popUp);
                    artistAdded.getSelectionModel().selectFirst();
                }
                if (artistAdded.getValue().equals("None")) {
                    ArtistAddList.getChildren().remove(artistAdded);
                }
            });
        });
        inputStructure.add(ArtistAddList, 2, 7);


        ScrollPane artistScroller = new ScrollPane();
        VBox scrutcureTwo = new VBox();
        scrutcureTwo.getChildren().add(inputStructure);

        //Description
        scrutcureTwo.getChildren().add(new Label("Show Description:"));
        TextArea showDesciption = ShowDescription(this.selectedShow.getDescription(), 360, 0);
        scrutcureTwo.getChildren().add(showDesciption);
        scrutcureTwo.setSpacing(10);

        //buttons
        artistScroller.setContent(scrutcureTwo);
        structure.setCenter(artistScroller);
        HBox choice = new HBox();
        Button submit = new Button("Submit");
        submit.setOnAction(event -> {
            ArrayList<Artist> addedArtists = new ArrayList<>();
            for (Node artistBox : ArtistAddList.getChildren()) {
                ComboBox artistCombo = (ComboBox) artistBox;
                if (artistCombo.getSelectionModel().getSelectedItem() != null) {
                    String comboBoxString = artistCombo.getSelectionModel().getSelectedItem().toString();
                    boolean containsArtist = false;
                    for (Artist artist : addedArtists) {
                        if (artist.getName().equals(comboBoxString)) {
                            containsArtist = true;
                        }
                    }
                    if (!containsArtist) {
                        Artist artist = stringToArtist(comboBoxString);
                        if (artist != null) {
                            addedArtists.add(artist);
                        }
                    }
                } else {
                    addedArtists = null;
                }
            }

            if (control(startingTime, endingTime, stage, genre, inputShowName, artists)) {
                String showNameAdding = inputShowName.getText();
                LocalTime beginTime = indexToLocalTime(this.timeList.indexOf(startingTime.getValue()));
                LocalTime endTime = indexToLocalTime(this.timeList.indexOf(endingTime.getValue()));
                PlannerData.Stage stageAdded = stringToStage((String) stage.getValue());
                Genres addedGenre = Genres.getGenre(genre.getValue().toString());
                ArrayList<Genres> genres = new ArrayList<>();
                genres.add(addedGenre);
                String descriptionShow = showDesciption.getText();
                int popularityAdded = (int) this.popularitySlider.getValue();

                this.addedShow = new Show(beginTime, endTime, addedArtists, showNameAdding, stageAdded, descriptionShow, genres, popularityAdded);
                if (!this.addedShow.equals(this.selectedShow)) {
                    DataController.getPlanner().deleteShow(this.selectedShow);
                    this.table.getItems().remove(this.selectedShow);
                    DataController.getPlanner().savePlanner();

                    for (Show existingShow : DataController.getPlanner().getShows()) {
                        if (existingShow.getStage().getName().equals(addedShow.getStage().getName())) {
                            if (addedShow.getBeginTime().isAfter(existingShow.getBeginTime()) && addedShow.getBeginTime().isBefore(existingShow.getEndTime()) || addedShow.getBeginTime().equals(existingShow.getBeginTime())) {
                                DataController.getPlanner().addShow(this.selectedShow);
                                this.data.add(this.selectedShow);
                                DataController.getPlanner().savePlanner();
                                return;
                            }

                            if (addedShow.getEndTime().isAfter(existingShow.getBeginTime()) && addedShow.getEndTime().isBefore(existingShow.getEndTime()) || addedShow.getEndTime().equals(existingShow.getEndTime())) {
                                DataController.getPlanner().addShow(this.selectedShow);
                                this.data.add(this.selectedShow);
                                DataController.getPlanner().savePlanner();
                                return;
                            }
                        }
                        for (Artist existingArtist : existingShow.getArtists()) {
                            for (Artist showArtist : addedShow.getArtists()) {
                                if (existingArtist.getName().equals(showArtist.getName())) {
                                    if (addedShow.getBeginTime().isAfter(existingShow.getBeginTime()) && addedShow.getBeginTime().isBefore(existingShow.getEndTime()) || addedShow.getBeginTime().equals(existingShow.getBeginTime())) {
                                        DataController.getPlanner().addShow(this.selectedShow);
                                        this.data.add(this.selectedShow);
                                        DataController.getPlanner().savePlanner();
                                        return;
                                    }

                                    if (addedShow.getEndTime().isAfter(existingShow.getBeginTime()) && addedShow.getEndTime().isBefore(existingShow.getEndTime()) || addedShow.getEndTime().equals(existingShow.getEndTime())) {
                                        DataController.getPlanner().addShow(this.selectedShow);
                                        this.data.add(this.selectedShow);
                                        DataController.getPlanner().savePlanner();
                                        return;
                                    }
                                }
                            }
                        }
                    }
                    this.table.getItems().remove(this.selectedShow);
                    DataController.getPlanner().addShow(this.addedShow);
                    this.data.add(addedShow);
                    DataController.getPlanner().savePlanner();
                    this.popUp.close();
                }
            } else {
                new ErrorWindow(this.popUp, this.errorList);
            }

        });
        choice.getChildren().add(this.cancel);
        choice.getChildren().add(submit);
        choice.setPadding(new Insets(10));
        choice.setSpacing(20);
        structure.setBottom(choice);

        Scene editScene = new Scene(structure);
        editScene.getStylesheets().add("Window-StyleSheet.css");
        this.popUp.setScene(editScene);
        this.popUp.show();
    }

    /**
     * This method allows the user to see the selected show that they wish to delete.
     */
    public void deletionWindow() {
        BorderPane structure = new BorderPane();

        //title
        Label deleteThis = new Label("Are you sure you want to delete this show?");
        structure.setTop(deleteThis);

        //information
        Label information = new Label("Show: " + this.selectedShow.getName() + '\n'
                + "From " + this.selectedShow.getBeginTimeString() + " to " + this.selectedShow.getEndTimeString() + '\n'
                + "By " + this.selectedShow.getArtistsNames() + " in the genre of " + this.selectedShow.getGenre() + '\n'
                + "On stage " + this.selectedShow.getStageName() + '\n'
                + "Expected popularity is " + this.selectedShow.getExpectedPopularity() + " people."
                + "with the desciption: " + '\n' + this.selectedShow.getDescription());

        structure.setCenter(information);

        //buttons
        HBox choice = new HBox();
        Button confirm = new Button("Confirm");
        confirm.setOnAction(event -> {
            if (DataController.getPlanner().deleteShow(this.selectedShow)) {
                this.table.getItems().remove(this.selectedShow);
                DataController.getPlanner().savePlanner();
                this.popUp.close();
            } else {
                this.errorList.add("The Show was not deleted, please try again later.");
                new ErrorWindow(this.popUp, this.errorList);
            }
        });
        choice.getChildren().add(this.cancel);
        choice.getChildren().add(confirm);
        choice.setPadding(new Insets(10));
        choice.setSpacing(20);

        structure.setBottom(choice);
        Scene deleteScene = new Scene(structure);
        deleteScene.getStylesheets().add("Window-StyleSheet.css");
        this.popUp.setScene(deleteScene);
        this.popUp.show();
    }

    /**
     * This method checks whether the input is valid or not.
     *
     * @param startingTime
     * @param endingTime
     * @param stage
     * @param genre
     * @param showName
     */
    public Boolean control(ComboBox startingTime, ComboBox endingTime, ComboBox stage, ComboBox genre, TextField showName, ComboBox artist) {
        this.errorList.clear();
        int startIndex = this.timeList.indexOf(startingTime.getValue());
        int endIndex = this.timeList.indexOf(endingTime.getValue());
        if (showName.getText().length() == 0) {
            this.errorList.add("The show name has not been filled in.");
        }
        if (startingTime.getValue() == null || endingTime.getValue() == null) {
            if (startingTime.getValue() == null || startingTime.getValue().equals("Select")) {
                this.errorList.add("The begintime has not been filled in.");
            }
            if (endingTime.getValue() == null || endingTime.getValue().equals("Select")) {
                this.errorList.add("The endtime has not been filled in.");
            }
        } else {
            if (startIndex > endIndex) {
                this.errorList.add("The begintime is later than the endtime.");
            } else if (startIndex == endIndex) {
                this.errorList.add("The begintime the same as the endtime.");
            }
        }
        if (stage.getValue() == null || stage.getValue().equals("--Select--")) {
            this.errorList.add("The stage has not been filled in.");
        }
        if (genre.getValue() == null || genre.getValue().equals("--Select--")) {
            this.errorList.add("The genre has not been filled in.");
        }
        if (artist.getValue() == null || artist.getValue().equals("--Select--")) {
            this.errorList.add("An artist has not been added yet");
        }
        if ((int) this.popularitySlider.getValue() == 0) {
            this.errorList.add("The popularity cannot be zero.");
        }

        if (this.errorList.isEmpty()) {
            return true;
        } else {
            return false;

        }
    }

    /**
     * This method sets the action of the Cancel button.
     */
    public void cancelsetup() {
        this.cancel.setOnAction(event -> {
            this.popUp.close();
        });
    }

    /**
     * This method makes a ComboBox with all the known Genres. it is not possible to add a Genre.
     * This method is only used once in the Adding menu and once in the Edit menu.
     *
     * @return ComboBox
     */
    public ComboBox genreBox() {
        ComboBox genreBox = new ComboBox();
        genreBox.getItems().add("--Select--");
        genreBox.getSelectionModel().selectFirst();
        for (Genres genre : Genres.values()) {
            genreBox.getItems().add(genre.getFancyName());
        }
        return genreBox;
    }

    /**
     * This method makes a ComboBox with all the known stages and allows for the user to create a new Stage.
     * This method is only used once in the Adding Menu and once in the Edit Menu.
     *
     * @return ComboBox
     */
    public ComboBox StageBox() {
        ComboBox stageBox = new ComboBox();
        stageBox.getItems().add("--Select--");
        stageBox.getSelectionModel().selectFirst();
        for (PlannerData.Stage stage : DataController.getPlanner().getStages()) {
            stageBox.getItems().add(stage.getName());
        }
        stageBox.getItems().add("Add new Stage");
        stageBox.setOnAction(event -> {
            int stageCapacity = 100;
            if (stageBox.getValue().equals("Add new Stage")) {
                AddingNewWindow addstage = new AddingNewWindow(1, this.popUp);
                if (addstage.stageUpdate()) {
                    addstage.resetUpdate();
                    stageBox.getItems().clear();
                    stageBox.getItems().add("--Select--");
                    for (PlannerData.Stage stage : DataController.getPlanner().getStages()) {
                        stageBox.getItems().add(stage.getName());
                    }
                    stageBox.getItems().add("Add new Stage");
                }
                stageBox.getSelectionModel().selectFirst();
            } else if (!stageBox.getValue().equals("--Select--")) {
                PlannerData.Stage selectedStage = stringToStage(stageBox.getValue().toString());
                if (selectedStage != null && !selectedStage.getName().isEmpty() && selectedStage.getCapacity() > 0) {
                    stageCapacity = selectedStage.getCapacity();
                }
            }
            this.popularitySlider.setMax(stageCapacity);
            this.popularitySlider.setMajorTickUnit((stageCapacity / 4));
            this.popularitySlider.setMinorTickCount(stageCapacity / 20);

        });
        return stageBox;
    }

    /**
     * This method allows a ComboBox to be made with the total amount of artists that are known and to add an unknown Artist to the Show.
     * This ComboBox will be at least used once in the Adding menu as in the Edit Menu.
     *
     * @return ComboBox
     */
    public ComboBox artistBox() {
        ComboBox artistBox = new ComboBox();

        artistBox.getItems().add("--Select--");
        artistBox.getSelectionModel().selectFirst();
        for (Artist artist : DataController.getPlanner().getArtists()) {
            artistBox.getItems().add(artist.getName());
        }
        artistBox.getItems().add("Add new Artist");

        artistBox.setOnAction(event -> {
            if (artistBox.getValue().equals("Add new Artist")) {
                new AddingNewWindow(2, this.popUp);
                artistBox.getSelectionModel().selectFirst();
            }
        });

        return artistBox;
    }

    /**
     * In the method of timeBox we can make a timeBox which contains all half and whole hours to plan a show.
     * This box will be used twice in the Adding menu as in the Edit menu.
     *
     * @return ComboBox
     */
    public ComboBox timeBox() {
        ComboBox timeBox = new ComboBox();
        timeBox.getItems().add("Select");
        timeBox.getSelectionModel().selectFirst();
        String time = "";
        String halftime = "";
        for (int i = 0; i <= 23; i++) {
            if (i < 10) {
                time = "0" + i;
            } else {
                time = "" + i;
            }
            for (int j = 0; j <= 1; j++) {
                if (j == 0) {
                    halftime = time;
                    time += ":00";
                } else {
                    halftime += ":30";
                    time = halftime;
                }
                if (!this.timeList.contains(time)) {
                    this.timeList.add(time);
                }
                timeBox.getItems().add(time);
            }
        }
        return timeBox;
    }

    public TextArea ShowDescription(String presetText, int width, int heigth) {
        TextArea description = new TextArea(presetText);
        if (heigth != 0) {
            description.setPrefHeight(heigth);
        }
        if (width != 0) {
            description.setPrefWidth(width);
        }
        return description;
    }

    public Genres stringToGenre(String genreString) {
        for (Genres genre : Genres.values()) {
            if (genre.getFancyName().equals(genreString)) {
                return genre;
            }
        }
        return null;
    }

    public LocalTime indexToLocalTime(int index) {
        LocalTime time = LocalTime.MIDNIGHT;
        int hours = index / 2;
        if (index % 2 == 1) {
            time = time.plusMinutes(30);
        }
        time = time.plusHours(hours);
        return time;
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

    public int LocalTimeToindex(LocalTime time) {
        int index = 1;
        index += time.getHour() * 2;
        if (time.getMinute() == 30) {
            index++;
        }
        return index;
    }

}