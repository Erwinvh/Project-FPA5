package GUILogic;

import Enumerators.Genres;
import PlannerData.Artist;
import PlannerData.Show;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.util.ArrayList;

class ShowWindow {

    private Show selectedShow;
    private ArrayList<String> errorList;
    private ArrayList<String> timeList;
    private Button cancelButton;
    private Stage popUp;
    private ScheduleTab ST;

    private ComboBox startingTime;
    private ComboBox endingTime;
    private ComboBox stageBox;
    private ComboBox genreBox;
    private TextField nameField;
    private int stagePopularity;
    private Slider popularitySlider;
    private Label popularityLabel;
    private TextArea descriptionArea;
    private VBox additionalArtists;
    private ComboBox artistBox;
    private GridPane gridPaneShows;

    /**
     * This is the constructor of the base createLayout of the windows of the three Menus.
     * The method also sends the user to the correct menu window.
     */
    ShowWindow(int screenNumber, Stage currParentStage, ScheduleTab ST, Show selectedShow) {
        this.ST = ST;
        this.selectedShow = selectedShow;

        popUp = new Stage();
        this.popUp.setWidth(400);
        this.popUp.setHeight(450);
        this.popUp.initOwner(currParentStage);
        this.popUp.setResizable(false);
        this.popUp.initModality(Modality.WINDOW_MODAL);
        this.popUp.getIcons().add(new Image("logoA5.jpg"));

        stageBox = getAllStagesComboBox();
        genreBox = getGenreComboBox();
        errorList = new ArrayList<>();
        nameField = new TextField();
        popularitySlider = new Slider();
        popularityLabel = new Label();
        descriptionArea = getShowDescriptionTextArea("", 200, 250);
        additionalArtists = new VBox();
        artistBox = getAllArtistsComboBox();

        setupTimeList();
        endingTime = getTimestampsComboBox(0);
        startingTime = getTimestampsComboBox(0);
        gridPaneShows = new GridPane();

        try {
            this.stagePopularity = this.selectedShow.getStage().getCapacity();
        } catch (Exception e) {
            stagePopularity = 0;
        }

        cancelButton = new Button("Cancel");
        this.cancelButton.setOnAction(event -> this.popUp.close());

        if (screenNumber == 1) addShowWindow();
        else if (screenNumber == 2) editShowWindow();
        else deleteShowWindow();
    }

    /**
     * This methode sets up the base layout of the add and  the edit show window.
     *
     * @return The scrollpane with the base layout
     */
    private ScrollPane AddEditSetup() {
        gridPaneShows.setHgap(10);
        gridPaneShows.setVgap(10);

        // ShowName
        Label showNameLabel = new Label("Show name:");
        gridPaneShows.add(showNameLabel, 1, 1);
        gridPaneShows.add(this.nameField, 2, 1);

        //time
        gridPaneShows.add(new Label("Begin time:"), 1, 2);
        gridPaneShows.add(new Label("End time:"), 1, 3);
        gridPaneShows.add(this.startingTime, 2, 2);
        gridPaneShows.add(this.endingTime, 2, 3);
//        startingTime.valueProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println("listener called");
//            System.out.println(newValue);
//            System.out.println(timeList.indexOf(newValue));
//            ComboBox updatedEndingTime = getTimestampsComboBox((timeList.indexOf(newValue) + 1));
//            gridPaneShows.add(updatedEndingTime, 2, 3);
//        });

        // Stage
        gridPaneShows.add(new Label("Stage:"), 1, 4);
        gridPaneShows.add(this.stageBox, 2, 4);

        // Genre
        gridPaneShows.add(new Label("Genre:"), 1, 5);
        gridPaneShows.add(this.genreBox, 2, 5);

        // Popularity
        gridPaneShows.add(new Label("Popularity:"), 1, 6);
        this.popularitySlider.setMin(0);
        this.popularitySlider.setMax(100);
        this.popularitySlider.setValue(0);
        this.popularitySlider.setShowTickLabels(true);
        this.popularitySlider.setShowTickMarks(true);
        this.popularitySlider.setMajorTickUnit(25);
        this.popularitySlider.setMinorTickCount(5);
        this.popularitySlider.setBlockIncrement(10);

        gridPaneShows.add(this.popularitySlider, 2, 6);
        this.popularitySlider.valueProperty().addListener((observableValue, oldValue, newValue) -> this.popularityLabel.textProperty().setValue(String.valueOf((newValue.intValue()))));
        this.popularityLabel.textProperty().setValue("0");
        gridPaneShows.add(this.popularityLabel, 3, 6);

        //artists
        gridPaneShows.add(new Label("Artists:"), 1, 7);
        this.additionalArtists.getChildren().add(this.artistBox);

        //add more artists button
        Button showArtistAdder = new Button("+");
        gridPaneShows.add(showArtistAdder, 3, 7);
        showArtistAdder.setOnAction(event -> {
            ComboBox artistAdded = getAllArtistsComboBox();
            artistAdded.getItems().add("None");
            this.additionalArtists.getChildren().add(artistAdded);
            artistAdded.setOnAction(e -> {
                if (artistAdded.getValue().equals("None")) {
                    this.additionalArtists.getChildren().remove(artistAdded);
                }
            });
        });
        gridPaneShows.add(this.additionalArtists, 2, 7);

        // Description
        VBox showDescriptionVBox = new VBox();
        showDescriptionVBox.getChildren().add(gridPaneShows);
        showDescriptionVBox.getChildren().add(new Label("Show description:"));
        showDescriptionVBox.getChildren().add(this.descriptionArea);

        ScrollPane artistScrollPane = new ScrollPane();
        artistScrollPane.setContent(showDescriptionVBox);

        return artistScrollPane;
    }

    /**
     * This method allows the user to Add a new Show.
     */
    private void addShowWindow() {
        BorderPane structure = new BorderPane();
        this.popUp.setTitle("Add Show");

        Label addingNew = new Label("What show do you want to add?");
        structure.setTop(addingNew);

        structure.setCenter(AddEditSetup());

        HBox cancelConfirmHBox = new HBox();
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> {
            Show addedShow = checkInput();
            if (addedShow != null && this.errorList.isEmpty()) {
                DataController.getPlanner().addShow(addedShow);
                DataController.getPlanner().savePlanner();
                ST.resetData();
                this.popUp.close();
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
    private void editShowWindow() {
        BorderPane borderPane = new BorderPane();
        this.popUp.setTitle("Edit Show");

        Label editShowLabel = new Label("Edit this show:");
        borderPane.setTop(editShowLabel);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        ScrollPane artistScroller = new ScrollPane();
        artistScroller.setContent(AddEditSetup());

        this.nameField.setText(this.selectedShow.getName());
        this.startingTime.getSelectionModel().select(localTimeToIndex(this.selectedShow.getBeginTime()));
        this.endingTime.getSelectionModel().select(localTimeToIndex(this.selectedShow.getEndTime()));

        this.stageBox.setValue(this.selectedShow.getStage().getName());
        this.genreBox.setValue(this.selectedShow.getGenre().getFancyName());

        this.popularitySlider.setMax(this.stagePopularity);
        this.popularitySlider.setValue(this.selectedShow.getExpectedPopularity());
        popularityLabel.textProperty().setValue(String.valueOf(this.selectedShow.getExpectedPopularity()));
        this.artistBox.setValue(this.selectedShow.getArtists().get(0).getName());

        for (int i = 1; i < this.selectedShow.getArtists().size(); i++) {
            ComboBox addedArtist = getAllArtistsComboBox();
            addedArtist.setValue(this.selectedShow.getArtists().get(i).getName());
            this.additionalArtists.getChildren().add(addedArtist);
            addedArtist.getItems().add("None");
            addedArtist.setOnAction(e -> {
                if (addedArtist.getValue().equals("None")) {
                    this.additionalArtists.getChildren().remove(addedArtist);
                }
            });
        }
        this.descriptionArea.setText(this.selectedShow.getDescription());

        borderPane.setCenter(artistScroller);
        HBox choice = new HBox();
        Button submit = new Button("Submit");
        submit.setOnAction(event -> {
            Show editedShow = checkInput();
            if (editedShow != null && this.errorList.isEmpty()) {
                int index = DataController.getPlanner().getShows().indexOf(selectedShow);

                DataController.getPlanner().getShows().get(index).setName(editedShow.getName());
                DataController.getPlanner().getShows().get(index).setExpectedPopularity(editedShow.getExpectedPopularity());
                DataController.getPlanner().getShows().get(index).setDescription(editedShow.getDescription());
                DataController.getPlanner().getShows().get(index).setBeginTime(editedShow.getBeginTime());
                DataController.getPlanner().getShows().get(index).setEndTime(editedShow.getEndTime());
                DataController.getPlanner().getShows().get(index).setArtists(editedShow.getArtists());
                DataController.getPlanner().getShows().get(index).setGenre(editedShow.getGenre());
                DataController.getPlanner().getShows().get(index).setStage(editedShow.getStage());
                DataController.getPlanner().savePlanner();
                this.ST.resetData();
                this.popUp.close();
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
     * This method checks the input of the added and edited show and returns the show that needs to be set in the planner
     *
     * @return the Show that has been verified
     */
    private Show checkInput() {
//        ObservableList<Node> childrens = this.gridPaneShows.getChildren();
//
//        for (Node node : childrens) {
//            if (gridPaneShows.getRowIndex(node) == 3 && gridPaneShows.getColumnIndex(node) == 2) {
//                this.endingTime = (ComboBox) node;
//                System.out.println(((ComboBox) node).getValue());
//                break;
//            }
//        }
        System.out.println(this.endingTime.getValue());
        errorList.clear();

        //name
        String showName = null;
        LocalTime beginTime = null;
        LocalTime endTime = null;
        PlannerData.Stage addedStage = null;
        Genres addedGenre = null;
        int popularityAdded = -2;
        String descriptionShow = null;

        if (this.nameField.getText().length() == 0) {
            this.errorList.add("The show name has not been filled in.");
        } else {
            showName = this.nameField.getText();
            for (Show show : DataController.getPlanner().getShows()) {
                if (this.selectedShow == null) {
                    if (showName.equals(show.getName())) {
                        this.errorList.add("There is already a show with that name");
                    }
                } else {
                    if (!this.selectedShow.getName().equals(show.getName()) && showName.equals(show.getName())) {
                        this.errorList.add("There is already a show with that name");
                    }
                }
            }
        }

        //startime & endtime
        if (startingTime.getValue() == null || endingTime.getValue() == null || startingTime.getValue().equals("Select") || endingTime.getValue().equals("Select")) {
            if (startingTime.getValue() == null || startingTime.getValue().equals("Select")) {
                this.errorList.add("The begintime has not been filled in.");
            }
            if (endingTime.getValue() == null || endingTime.getValue().equals("Select")) {
                this.errorList.add("The endtime has not been filled in.");
            }
        } else {
            beginTime = indexToLocalTime(this.timeList.indexOf(startingTime.getValue()));
            endTime = indexToLocalTime(this.timeList.indexOf(endingTime.getValue()));
        }

        //stage
        if (this.stageBox.getValue() == null || this.stageBox.getValue().equals("Select")) {
            this.errorList.add("The stage has not been filled in.");
        } else {
            addedStage = DataController.getPlanner().getStage((String) stageBox.getValue());
        }

        //genre
        if (genreBox.getValue() == null || genreBox.getValue().equals("Select")) {
            this.errorList.add("The genre has not been filled in.");
        } else {
            addedGenre = Genres.getGenre(genreBox.getValue().toString());
        }

        //artist
        if (artistBox.getValue() == null || artistBox.getValue().equals("Select")) {
            this.errorList.add("An artist has not been chosen yet");
        }

        ArrayList<Artist> addedArtists = new ArrayList<>();
        for (Node artistBox : additionalArtists.getChildren()) {
            ComboBox artistCombo = (ComboBox) artistBox;
            if (artistCombo.getSelectionModel().getSelectedItem() != null) {
                String comboBoxString = artistCombo.getSelectionModel().getSelectedItem().toString();

                for (Artist artist : addedArtists) {
                    if (artist.getName().equals(comboBoxString)) {
                        this.errorList.add("you have a duplicate artist in the artists.");
                    }
                }

                Artist artist = DataController.getPlanner().getArtist(comboBoxString);
                if (artist != null) {
                    addedArtists.add(artist);
                }
            }
        }

        //pop
        if ((int) this.popularitySlider.getValue() == 0) {
            this.errorList.add("The popularity cannot be zero.");
        } else {
            popularityAdded = (int) this.popularitySlider.getValue();
        }
        //description
        if (this.descriptionArea.getText().length() == 0) {
            this.errorList.add("No Description ahs been added.");
        } else {
            descriptionShow = descriptionArea.getText();
        }

        Show newShow;
        if (errorList.isEmpty()) {
            newShow = new Show(beginTime, endTime, addedArtists, showName, addedStage, descriptionShow, addedGenre, popularityAdded);
            for (Show existingShow : DataController.getPlanner().getShows()) {
                if (selectedShow == null) {
                    if (existingShow.getStage().getName().equals(newShow.getStage().getName())) {
                        if (newShow.getBeginTime().isAfter(existingShow.getBeginTime()) && newShow.getBeginTime().isBefore(existingShow.getEndTime()) || newShow.getBeginTime().equals(existingShow.getBeginTime())) {
                            this.errorList.add("A show cannot begin at the same time or during another show on the same stage.");
                        }
                        if (newShow.getEndTime().isAfter(existingShow.getBeginTime()) && newShow.getEndTime().isBefore(existingShow.getEndTime()) || newShow.getEndTime().equals(existingShow.getEndTime()) || (newShow.getBeginTime().isBefore(existingShow.getBeginTime()) && newShow.getEndTime().isAfter(existingShow.getEndTime()))) {
                            this.errorList.add("A show cannot end after another show has begun or end at the same time as another ends on the same stage");
                        }
                    }

                    for (Artist existingArtist : existingShow.getArtists()) {
                        for (Artist showArtist : newShow.getArtists()) {
                            if (existingArtist.getName().equals(showArtist.getName())) {
                                if (newShow.getBeginTime().isAfter(existingShow.getBeginTime()) && newShow.getBeginTime().isBefore(existingShow.getEndTime()) || newShow.getBeginTime().equals(existingShow.getBeginTime())) {
                                    this.errorList.add("An artist cannot be at two shows at the same time");
                                }

                                if (newShow.getEndTime().isAfter(existingShow.getBeginTime()) && newShow.getEndTime().isBefore(existingShow.getEndTime()) || newShow.getEndTime().equals(existingShow.getEndTime())) {
                                    this.errorList.add("An artist cannot be at two shows at the same time");
                                }
                            }
                        }
                    }
                } else {
                    if (!existingShow.getName().equals(selectedShow.getName())) {
                        if (existingShow.getStage().getName().equals(newShow.getStage().getName())) {
                            if (newShow.getBeginTime().isAfter(existingShow.getBeginTime()) && newShow.getBeginTime().isBefore(existingShow.getEndTime()) || newShow.getBeginTime().equals(existingShow.getBeginTime())) {
                                this.errorList.add("A show cannot begin at the same time or during another show on the same stage.");
                            }
                            if (newShow.getEndTime().isAfter(existingShow.getBeginTime()) && newShow.getEndTime().isBefore(existingShow.getEndTime()) || newShow.getEndTime().equals(existingShow.getEndTime()) || (newShow.getBeginTime().isBefore(existingShow.getBeginTime()) && newShow.getEndTime().isAfter(existingShow.getEndTime()))) {
                                this.errorList.add("A show cannot end after another show has begun or end at the same time as another ends on the same stage");
                            }
                        }

                        for (Artist existingArtist : existingShow.getArtists()) {
                            for (Artist showArtist : newShow.getArtists()) {
                                if (existingArtist.getName().equals(showArtist.getName())) {
                                    if (newShow.getBeginTime().isAfter(existingShow.getBeginTime()) && newShow.getBeginTime().isBefore(existingShow.getEndTime()) || newShow.getBeginTime().equals(existingShow.getBeginTime())) {
                                        this.errorList.add("An artist cannot be at two shows at the same time");
                                    }

                                    if (newShow.getEndTime().isAfter(existingShow.getBeginTime()) && newShow.getEndTime().isBefore(existingShow.getEndTime()) || newShow.getEndTime().equals(existingShow.getEndTime())) {
                                        this.errorList.add("An artist cannot be at two shows at the same time");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            newShow = null;
        }

        return newShow;
    }

    /**
     * This method allows the user to see the selected show that they wish to delete.
     */
    private void deleteShowWindow() {
        BorderPane borderPane = new BorderPane();
        this.popUp.setTitle("Delete Show");

        //title
        Label deleteShowLabel = new Label("Are you sure you want to delete this show?");
        borderPane.setTop(deleteShowLabel);

        //information
        Label information = new Label("Show: " + this.selectedShow.getName() + '\n'
                + "From " + this.selectedShow.getBeginTimeString() + " to " + this.selectedShow.getEndTimeString() + '\n'
                + "By " + this.selectedShow.getArtistsNames() + " in the genre of " + this.selectedShow.getGenre().getFancyName() + '\n'
                + "On stage " + this.selectedShow.getStageName() + '\n'
                + "Expected popularity is " + this.selectedShow.getExpectedPopularity() + " people."
                + "with the description: " + '\n' + this.selectedShow.getDescription());

        borderPane.setCenter(information);

        //buttons
        HBox cancelConfirmButton = new HBox();
        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(event -> {
            if (DataController.getPlanner().deleteShow(this.selectedShow)) {
                DataController.getPlanner().savePlanner();
                this.ST.resetData();
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
     * This method makes a ComboBox with all the known Genres. it is not possible to add a Genre.
     * This method is only used once in the Adding menu and once in the Edit menu.
     *
     * @return ComboBox
     */
    private ComboBox getGenreComboBox() {
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
    private ComboBox getAllStagesComboBox() {
        ComboBox stageBox = new ComboBox();
        stageBox.getItems().add("Select stage");
        stageBox.getSelectionModel().selectFirst();
        for (PlannerData.Stage stage : DataController.getPlanner().getStages()) {
            stageBox.getItems().add(stage.getName());
        }

        stageBox.setOnAction(event -> {
            int stageCapacity = 100;
            if (!stageBox.getValue().equals("Select stage")) {
                PlannerData.Stage selectedStage = DataController.getPlanner().getStage(stageBox.getValue().toString());
                if (selectedStage != null && !selectedStage.getName().isEmpty() && selectedStage.getCapacity() > 0) {
                    stageCapacity = selectedStage.getCapacity();
                }
            }
            this.popularitySlider.setMax(stageCapacity);
            this.popularitySlider.setMajorTickUnit((stageCapacity * 0.25));
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
    private ComboBox getAllArtistsComboBox() {
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
    private ComboBox getTimestampsComboBox(int startingIndex) {
        ComboBox timeBox = new ComboBox();
        timeBox.getItems().add("Select");
        timeBox.getItems().addAll(timeList.subList(startingIndex, timeList.size()));
        timeBox.getSelectionModel().selectFirst();

        return timeBox;
    }

    /**
     * This method sets up the time list with all the possible times for the adding or editing.
     * This method is necessary for both begin and end time ComboBoxes
     */
    private void setupTimeList() {
        timeList = new ArrayList<>();
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
            }
        }
    }

    /**
     * This method creates the description text area
     *
     * @param presetText this text is used in edit and is how the textarea shows the already set text.
     * @param width      the width of the area
     * @param height     the height of the area
     * @return the fully made area.
     */
    private TextArea getShowDescriptionTextArea(String presetText, int width, int height) {
        TextArea description = new TextArea(presetText);
        if (height != 0) description.setPrefHeight(height);

        if (width != 0) description.setPrefWidth(width);
        return description;
    }

    /**
     * converts the index of the time Combobox to the corresponding time
     *
     * @param index the index of the time selected
     * @return the time at the given index
     */
    private LocalTime indexToLocalTime(int index) {
        LocalTime time = LocalTime.MIDNIGHT;
        int hours = index / 2;
        if (index % 2 == 1) {
            time = time.plusMinutes(30);
        }
        time = time.plusHours(hours);
        return time;
    }

    /**
     * converts the time to the index of the Time Combobox
     *
     * @param time the selected time
     * @return the index of the given time
     */
    private int localTimeToIndex(LocalTime time) {
        int index = 1;
        index += time.getHour() * 2;
        if (time.getMinute() == 30) {
            index++;
        }
        return index;
    }
}