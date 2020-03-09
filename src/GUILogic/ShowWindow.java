package GUILogic;

import Enumerators.Genres;
import PlannerData.Artist;
import PlannerData.Show;
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

public class ShowWindow {

    private Stage currParentStage;
    private Show selectedShow;
    private ArrayList<String> errorList = new ArrayList<>();
    private ArrayList<String> timeList = new ArrayList<>();
    private Button cancelButton = new Button("Cancel");
    private Stage popUp = new Stage();

    // TODO: Refactor createTable and data
    private TableView<Show> table;
    private ObservableList<Show> data;

    private int stagePopularity = 0;
    private Slider popularitySlider = new Slider();
    private Show addedShow;

    /**
     * This is the constructor of the base createLayout of the windows of the three Menus.
     * The method also sends the user to the correct menu window.
     *
     * @param screenNumber
     * @param currParentStage
     * @param data
     * @param table
     * @param selectedShow
     */
    public ShowWindow(int screenNumber, Stage currParentStage, javafx.collections.ObservableList<Show> data, TableView<Show> table, Show selectedShow) {
        this.currParentStage = currParentStage;
        this.table = table;
        this.data = data;
        this.selectedShow = selectedShow;
        this.popUp.setWidth(400);
        this.popUp.setHeight(450);
        this.popUp.initOwner(this.currParentStage);
        this.popUp.setResizable(false);
        this.popUp.initModality(Modality.WINDOW_MODAL);

        try {
            this.stagePopularity = this.selectedShow.getStage().getCapacity();
        } catch (Exception e) {
            System.out.println("Could not get capacity from selected show.");
        }

        this.cancelButton.setOnAction(event -> this.popUp.close());

        if (screenNumber == 1) addShowWindow();
        else if (screenNumber == 2) editShowWindow();
        else deleteShowWindow();
    }

    /**
     * This method allows the user to Add a new Show.
     */
    public void addShowWindow() {
        BorderPane structure = new BorderPane();

        Label addingNew = new Label("What show do you want to add?");
        structure.setTop(addingNew);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        //showname
        Label showNameLabel = new Label("Show name:");
        TextField inputShowName = new TextField();
        gridPane.add(showNameLabel, 1, 1);
        gridPane.add(inputShowName, 2, 1);

        //time
        gridPane.add(new Label("Begin time:"), 1, 2);
        gridPane.add(new Label("End time:"), 1, 3);
        ComboBox startingTime = getTimestampsComboBox();
        ComboBox endingTime = getTimestampsComboBox();
        gridPane.add(startingTime, 2, 2);
        gridPane.add(endingTime, 2, 3);

        //stage
        gridPane.add(new Label("Stage:"), 1, 4);
        ComboBox stage = getAllStagesComboBox();
        gridPane.add(stage, 2, 4);

        //genre
        gridPane.add(new Label("Genre:"), 1, 5);
        ComboBox genreComboBox = getGenreComboBox();
        gridPane.add(genreComboBox, 2, 5);

        //popularity
        gridPane.add(new Label("Popularity:"), 1, 6);
        this.popularitySlider.setMin(0);
        this.popularitySlider.setMax(100);
        this.popularitySlider.setValue(0);

        this.popularitySlider.setShowTickLabels(true);
        this.popularitySlider.setShowTickMarks(true);
        this.popularitySlider.setMajorTickUnit(25);
        this.popularitySlider.setMinorTickCount(5);
        this.popularitySlider.setBlockIncrement(10);

        gridPane.add(this.popularitySlider, 2, 6);
        Label currPopulatityLabel = new Label("");
        this.popularitySlider.valueProperty().addListener((observableValue, oldValue, newValue) -> currPopulatityLabel.textProperty().setValue(String.valueOf((newValue.intValue()))));
        currPopulatityLabel.textProperty().setValue("0");
        gridPane.add(currPopulatityLabel, 3, 6);

        //artists
        gridPane.add(new Label("Artists:"), 1, 7);
        ComboBox artistsComboBox = getAllArtistsComboBox();

        //add more artists
        Button showArtistAdder = new Button("+");

        VBox artistAddList = new VBox();
        artistAddList.getChildren().add(artistsComboBox);
        gridPane.add(showArtistAdder, 3, 7);
        showArtistAdder.setOnAction(event -> {
            ComboBox artistAdded = getAllArtistsComboBox();
            artistAdded.getItems().add("None");
            artistAddList.getChildren().add(artistAdded);
            artistAdded.setOnAction(e -> {
                if (artistAdded.getValue().equals("None")) {
                    artistAddList.getChildren().remove(artistAdded);
                }
            });
        });
        gridPane.add(artistAddList, 2, 7);

        //Description
        VBox showDescriptionVBox = new VBox();
        showDescriptionVBox.getChildren().add(gridPane);
        showDescriptionVBox.getChildren().add(new Label("Show description:"));
        TextArea descriptionTextArea = getShowDescriptionTextArea("", 200, 250);
        showDescriptionVBox.getChildren().add(descriptionTextArea);

        ScrollPane artistScrollPane = new ScrollPane();
        artistScrollPane.setContent(showDescriptionVBox);
        structure.setCenter(artistScrollPane);

        //buttons
        HBox cancelConfirmHBox = new HBox();
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> {
            ArrayList<Artist> addedArtists = new ArrayList<>();

            for (Node artistBox : artistAddList.getChildren()) {
                ComboBox artistCombo = (ComboBox) artistBox;
                if (artistCombo.getSelectionModel().getSelectedItem() != null) {
                    String comboBoxString = artistCombo.getSelectionModel().getSelectedItem().toString();

                    for (Artist artist : addedArtists) {
                        if (artist.getName().equals(comboBoxString)) {
                            return;
                        }
                    }

                    Artist artist = stringToArtist(comboBoxString);
                    if (artist != null) {
                        addedArtists.add(artist);
                    }
                } else {
                    addedArtists = null;
                    // TODO: This is not necessary, so dubble check if it is removable
                }
            }

            if (verifyInput(startingTime, endingTime, stage, genreComboBox, inputShowName, artistsComboBox)) {
                String showNameAdding = inputShowName.getText();
                LocalTime beginTime = indexToLocalTime(this.timeList.indexOf(startingTime.getValue()));
                LocalTime endTime = indexToLocalTime(this.timeList.indexOf(endingTime.getValue()));
                PlannerData.Stage stageAdded = stringToStage((String) stage.getValue());
                Genres addedGenre = Genres.getGenre(genreComboBox.getValue().toString());
                ArrayList<Genres> genresList = new ArrayList<>();
                genresList.add(addedGenre);

                String descriptionShow = descriptionTextArea.getText();
                int popularityAdded = (int) this.popularitySlider.getValue();

                Show show = new Show(beginTime, endTime, addedArtists, showNameAdding, stageAdded, descriptionShow, genresList, popularityAdded);
                if (!DataController.getPlanner().getShows().contains(show)) {
                    for (Show existingShow : DataController.getPlanner().getShows()) {
                        if (existingShow.getStage().getName().equals(show.getStage().getName())) {
                            if (show.getBeginTime().isAfter(existingShow.getBeginTime()) && show.getBeginTime().isBefore(existingShow.getEndTime()) || show.getBeginTime().equals(existingShow.getBeginTime())) {
                                this.errorList.clear();
                                this.errorList.add("A show cannot begin at the same time or during another show on the same stage.");
                                new ErrorWindow(this.popUp, this.errorList);
                                return;
                            }

                            if (show.getEndTime().isAfter(existingShow.getBeginTime()) && show.getEndTime().isBefore(existingShow.getEndTime()) || show.getEndTime().equals(existingShow.getEndTime()) || (show.getBeginTime().isBefore(existingShow.getBeginTime()) && show.getEndTime().isAfter(existingShow.getEndTime()))) {
                                this.errorList.clear();
                                this.errorList.add("A show cannot end after another show has begun or end at the same time as another ends on the same stage");
                                new ErrorWindow(this.popUp, this.errorList);
                                return;
                            }
                        }

                        for (Artist existingArtist : existingShow.getArtists()) {
                            for (Artist showArtist : show.getArtists()) {
                                if (existingArtist.getName().equals(showArtist.getName())) {
                                    if (show.getBeginTime().isAfter(existingShow.getBeginTime()) && show.getBeginTime().isBefore(existingShow.getEndTime()) || show.getBeginTime().equals(existingShow.getBeginTime())) {
                                        this.errorList.clear();
                                        this.errorList.add("An artist cannot be at two shows at the same time");
                                        new ErrorWindow(this.popUp, this.errorList);
                                        return;
                                    }

                                    if (show.getEndTime().isAfter(existingShow.getBeginTime()) && show.getEndTime().isBefore(existingShow.getEndTime()) || show.getEndTime().equals(existingShow.getEndTime())) {
                                        this.errorList.clear();
                                        this.errorList.add("An artist cannot be at two shows at the same time");
                                        new ErrorWindow(this.popUp, this.errorList);
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

        cancelConfirmHBox.getChildren().add(this.cancelButton);
        cancelConfirmHBox.getChildren().add(submitButton);
        cancelConfirmHBox.setPadding(new Insets(10));
        cancelConfirmHBox.setSpacing(20);
        structure.setBottom(cancelConfirmHBox);

        Scene adderScene = new Scene(structure);
        adderScene.getStylesheets().add("Window-StyleSheet.css");
        this.popUp.setScene(adderScene);
        this.popUp.show();
    }

    /**
     * This method allows the user to edit the selected show.
     */
    public void editShowWindow() {
        BorderPane borderPane = new BorderPane();

        // Title
        Label editShowLabel = new Label("Edit this show:");
        borderPane.setTop(editShowLabel);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Show name
        Label showNameLabel = new Label("Show name:");
        TextField inputShowName = new TextField(this.selectedShow.getName());
        gridPane.add(showNameLabel, 1, 1);
        gridPane.add(inputShowName, 2, 1);

        //time
        gridPane.add(new Label("Begin time:"), 1, 2);
        gridPane.add(new Label("End time:"), 1, 3);

        ComboBox startingTime = getTimestampsComboBox();
        ComboBox endingTime = getTimestampsComboBox();

        startingTime.getSelectionModel().select(localTimeToIndex(this.selectedShow.getBeginTime()));
        endingTime.getSelectionModel().select(localTimeToIndex(this.selectedShow.getEndTime()));
        gridPane.add(startingTime, 2, 2);
        gridPane.add(endingTime, 2, 3);

        //stage
        gridPane.add(new Label("Stage:"), 1, 4);
        ComboBox stage = getAllStagesComboBox();
        stage.setValue(this.selectedShow.getStage().getName());
        gridPane.add(stage, 2, 4);

        //Genre
        gridPane.add(new Label("Genre:"), 1, 5);
        ComboBox genre = getGenreComboBox();
        genre.setValue(this.selectedShow.getGenre().get(0).getFancyName());
        gridPane.add(genre, 2, 5);

        //popularity
        gridPane.add(new Label("Popularity:"), 1, 6);
        this.popularitySlider.setMin(0);
        this.popularitySlider.setMax(this.stagePopularity);
        this.popularitySlider.setValue(this.selectedShow.getExpectedPopularity());
        this.popularitySlider.setShowTickLabels(true);
        this.popularitySlider.setShowTickMarks(true);
        this.popularitySlider.setMajorTickUnit(25);
        this.popularitySlider.setMinorTickCount(5);
        this.popularitySlider.setBlockIncrement(10);
        gridPane.add(this.popularitySlider, 2, 6);

        Label popularityLabel = new Label();
        this.popularitySlider.valueProperty().addListener((observableValue, oldValue, newValue) -> popularityLabel.textProperty().setValue(
                String.valueOf(newValue.intValue())));
        popularityLabel.textProperty().setValue(String.valueOf(this.selectedShow.getExpectedPopularity()));
        gridPane.add(popularityLabel, 3, 6);

        //artists
        gridPane.add(new Label("Artists:"), 1, 7);
        ComboBox artistsComboBox = getAllArtistsComboBox();
        artistsComboBox.setValue(this.selectedShow.getArtists().get(0).getName());

        //add more artists
        Button showArtistAdder = new Button("+");
        VBox artistAddList = new VBox();
        artistAddList.getChildren().add(artistsComboBox);
        for (int i = 1; i < this.selectedShow.getArtists().size(); i++) {
            ComboBox addedArtist = getAllArtistsComboBox();
//            addedArtist.
            addedArtist.setValue(this.selectedShow.getArtists().get(i).getName());
            artistAddList.getChildren().add(addedArtist);
            addedArtist.getItems().add("None");
            addedArtist.setOnAction(e -> {
                if (addedArtist.getValue().equals("None")) {
                    artistAddList.getChildren().remove(addedArtist);
                }
            });
        }

        gridPane.add(showArtistAdder, 3, 7);
        showArtistAdder.setOnAction(event -> {
            ComboBox artistAdded = getAllArtistsComboBox();
            artistAdded.getItems().add("None");
            artistAddList.getChildren().add(artistAdded);
            artistAdded.setOnAction(e -> {
                if (artistAdded.getValue().equals("None")) {
                    artistAddList.getChildren().remove(artistAdded);
                }
            });
        });
        gridPane.add(artistAddList, 2, 7);

        ScrollPane artistScroller = new ScrollPane();
        VBox editArtistVBox = new VBox();
        editArtistVBox.getChildren().add(gridPane);

        //Description
        editArtistVBox.getChildren().add(new Label("Show Description:"));
        TextArea showDescription = getShowDescriptionTextArea(this.selectedShow.getDescription(), 360, 0);
        editArtistVBox.getChildren().add(showDescription);
        editArtistVBox.setSpacing(10);

        // Buttons
        artistScroller.setContent(editArtistVBox);
        borderPane.setCenter(artistScroller);
        HBox choice = new HBox();
        Button submit = new Button("Submit");
        submit.setOnAction(event -> {
            ArrayList<Artist> addedArtists = new ArrayList<>();
            for (Node artistBox : artistAddList.getChildren()) {
                ComboBox artistCombo = (ComboBox) artistBox;
                if (artistCombo.getSelectionModel().getSelectedItem() != null) {
                    String comboBoxString = artistCombo.getSelectionModel().getSelectedItem().toString();

                    for (Artist artist : addedArtists) {
                        if (artist.getName().equals(comboBoxString)) {
                            return;
                        }
                    }

                    Artist artist = stringToArtist(comboBoxString);
                    if (artist != null) {
                        addedArtists.add(artist);
                    }
                } else {
                    addedArtists = null;
                    // TODO: Check whether this is obsolete
                }
            }

            if (verifyInput(startingTime, endingTime, stage, genre, inputShowName, artistsComboBox)) {
                String showNameAdding = inputShowName.getText();
                LocalTime beginTime = indexToLocalTime(this.timeList.indexOf(startingTime.getValue()));
                LocalTime endTime = indexToLocalTime(this.timeList.indexOf(endingTime.getValue()));
                PlannerData.Stage stageAdded = stringToStage((String) stage.getValue());
                Genres addedGenre = Genres.getGenre(genre.getValue().toString());
                ArrayList<Genres> genres = new ArrayList<>();
                genres.add(addedGenre);
                String descriptionShow = showDescription.getText();
                int popularityAdded = (int) this.popularitySlider.getValue();

                this.addedShow = new Show(beginTime, endTime, addedArtists, showNameAdding, stageAdded, descriptionShow, genres, popularityAdded);
                if (!this.addedShow.equals(this.selectedShow)) {
                    DataController.getPlanner().deleteShow(this.selectedShow);
                    this.table.getItems().remove(this.selectedShow);
                    DataController.getPlanner().savePlanner();

                    for (Show existingShow : DataController.getPlanner().getShows()) {
                        if (existingShow.getStage().getName().equals(addedShow.getStage().getName())) {
                            if (addedShow.getBeginTime().isAfter(existingShow.getBeginTime()) && addedShow.getBeginTime().isBefore(existingShow.getEndTime()) || addedShow.getBeginTime().equals(existingShow.getBeginTime()) || (addedShow.getBeginTime().isBefore(existingShow.getBeginTime()) && addedShow.getEndTime().isAfter(existingShow.getEndTime()))) {
                                DataController.getPlanner().addShow(this.selectedShow);
                                this.data.add(this.selectedShow);
                                DataController.getPlanner().savePlanner();
                                this.errorList.clear();
                                this.errorList.add("A show cannot begin at the same time or during another show on the same stage.");
                                new ErrorWindow(this.popUp, this.errorList);
                                return;
                            }

                            if (addedShow.getEndTime().isAfter(existingShow.getBeginTime()) && addedShow.getEndTime().isBefore(existingShow.getEndTime()) || addedShow.getEndTime().equals(existingShow.getEndTime())) {
                                DataController.getPlanner().addShow(this.selectedShow);
                                this.data.add(this.selectedShow);
                                DataController.getPlanner().savePlanner();
                                this.errorList.clear();
                                this.errorList.add("A show cannot end after another show has begun or end at the same time as another ends on the same stage");
                                new ErrorWindow(this.popUp, this.errorList);
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
                                        this.errorList.clear();
                                        this.errorList.add("An artist cannot be at two shows at the same time");
                                        new ErrorWindow(this.popUp, this.errorList);
                                        return;
                                    }

                                    if (addedShow.getEndTime().isAfter(existingShow.getBeginTime()) && addedShow.getEndTime().isBefore(existingShow.getEndTime()) || addedShow.getEndTime().equals(existingShow.getEndTime())) {
                                        DataController.getPlanner().addShow(this.selectedShow);
                                        this.data.add(this.selectedShow);
                                        DataController.getPlanner().savePlanner();
                                        this.errorList.clear();
                                        this.errorList.add("An artist cannot be at two shows at the same time");
                                        new ErrorWindow(this.popUp, this.errorList);
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

        choice.getChildren().add(this.cancelButton);
        choice.getChildren().add(submit);
        choice.setPadding(new Insets(10));
        choice.setSpacing(20);
        borderPane.setBottom(choice);

        Scene editScene = new Scene(borderPane);
        editScene.getStylesheets().add("Window-StyleSheet.css");
        this.popUp.setScene(editScene);
        this.popUp.show();
    }

    /**
     * This method allows the user to see the selected show that they wish to delete.
     */
    public void deleteShowWindow() {
        BorderPane borderPane = new BorderPane();

        //title
        Label deleteShowLabel = new Label("Are you sure you want to delete this show?");
        borderPane.setTop(deleteShowLabel);

        //information
        Label information = new Label("Show: " + this.selectedShow.getName() + '\n'
                + "From " + this.selectedShow.getBeginTimeString() + " to " + this.selectedShow.getEndTimeString() + '\n'
                + "By " + this.selectedShow.getArtistsNames() + " in the genre of " + this.selectedShow.getGenre() + '\n'
                + "On stage " + this.selectedShow.getStageName() + '\n'
                + "Expected popularity is " + this.selectedShow.getExpectedPopularity() + " people."
                + "with the description: " + '\n' + this.selectedShow.getDescription());

        borderPane.setCenter(information);

        //buttons
        HBox cancelConfirmButton = new HBox();
        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(event -> {
            if (DataController.getPlanner().deleteShow(this.selectedShow)) {
                this.table.getItems().remove(this.selectedShow);
                DataController.getPlanner().savePlanner();
                this.popUp.close();
            } else {
                this.errorList.add("The show was not deleted, please try again later.");
                new ErrorWindow(this.popUp, this.errorList);
            }
        });

        cancelConfirmButton.getChildren().add(this.cancelButton);
        cancelConfirmButton.getChildren().add(confirmButton);
        cancelConfirmButton.setPadding(new Insets(10));
        cancelConfirmButton.setSpacing(20);

        borderPane.setBottom(cancelConfirmButton);
        Scene deleteScene = new Scene(borderPane);
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
    public boolean verifyInput(ComboBox startingTime, ComboBox endingTime, ComboBox stage, ComboBox genre, TextField showName, ComboBox artist) {
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

        return this.errorList.isEmpty();
    }

    /**
     * This method makes a ComboBox with all the known Genres. it is not possible to add a Genre.
     * This method is only used once in the Adding menu and once in the Edit menu.
     *
     * @return ComboBox
     */
    public ComboBox getGenreComboBox() {
        ComboBox genreBox = new ComboBox();
        genreBox.getItems().add("Select");
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
    public ComboBox getAllStagesComboBox() {
        ComboBox stageBox = new ComboBox();
        stageBox.getItems().add("Select stage");
        stageBox.getSelectionModel().selectFirst();
        for (PlannerData.Stage stage : DataController.getPlanner().getStages()) {
            stageBox.getItems().add(stage.getName());
        }
        stageBox.setOnAction(event -> {
            int stageCapacity = 100;
            if (!stageBox.getValue().equals("Select stage")) {
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
    public ComboBox getAllArtistsComboBox() {
        ComboBox artistBox = new ComboBox();
        artistBox.getItems().add("Select");
        artistBox.getSelectionModel().selectFirst();
        for (Artist artist : DataController.getPlanner().getArtists()) {
            artistBox.getItems().add(artist.getName());
        }

        return artistBox;
    }

    /**
     * In the method of getTimestampsComboBox we can make a getTimestampsComboBox which contains all half and whole hours to plan a show.
     * This box will be used twice in the Adding menu as in the Edit menu.
     *
     * @return ComboBox
     */
    public ComboBox getTimestampsComboBox() {
        ComboBox timeBox = new ComboBox();
        timeBox.getItems().add("Select");
        timeBox.getSelectionModel().selectFirst();
        String time;
        String halftime = "";

        for (int i = 0; i <= 23; i++) {
            if (i < 10) time = "0" + i;
            else time = "" + i;

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

    public TextArea getShowDescriptionTextArea(String presetText, int width, int height) {
        TextArea description = new TextArea(presetText);
        if (height != 0) description.setPrefHeight(height);

        if (width != 0) description.setPrefWidth(width);
        return description;
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

    // TODO: Make stringToStage logic in Stage
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

    // TODO: Move logic to Artist
    public Artist stringToArtist(String artistString) {
        for (Artist artist : DataController.getPlanner().getArtists()) {
            if (artistString.equals(artist.getName())) {
                return artist;
            }
        }
        return null;
    }

    public int localTimeToIndex(LocalTime time) {
        int index = 1;
        index += time.getHour() * 2;
        if (time.getMinute() == 30) {
            index++;
        }
        return index;
    }
}