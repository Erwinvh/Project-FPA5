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

public class ShowWindow {

    private Show selectedShow;
    private ArrayList<String> errorList;
    private ArrayList<String> timeList;
    private Button cancelButton;
    private Stage popUp;
    private ScheduleTab ST;
    private Show addedShow;

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


    /**
     * This is the constructor of the base createLayout of the windows of the three Menus.
     * The method also sends the user to the correct menu window.
     *
     * @param screenNumber
     * @param currParentStage
     * @param ST
     * @param selectedShow
     */
    public ShowWindow(int screenNumber, Stage currParentStage, ScheduleTab ST, Show selectedShow) {
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


    public ScrollPane AddEditSetup(){
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        //showname
        Label showNameLabel = new Label("Show name:");
        gridPane.add(showNameLabel, 1, 1);
        gridPane.add(this.nameField, 2, 1);

        //time
        gridPane.add(new Label("Begin time:"), 1, 2);
        gridPane.add(new Label("End time:"), 1, 3);
        gridPane.add(this.startingTime, 2, 2);
        gridPane.add(this.endingTime, 2, 3);
        startingTime.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("listener called");
            System.out.println(newValue);
            System.out.println(timeList.indexOf(newValue));
            ComboBox updatedEndingTime = getTimestampsComboBox((timeList.indexOf(newValue) + 1));
            gridPane.add(updatedEndingTime, 2, 3);
        });

        //stage
        gridPane.add(new Label("Stage:"), 1, 4);
        gridPane.add(this.stageBox, 2, 4);

        //genre
        gridPane.add(new Label("Genre:"), 1, 5);
        gridPane.add(this.genreBox, 2, 5);

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
        this.popularitySlider.valueProperty().addListener((observableValue, oldValue, newValue) -> this.popularityLabel.textProperty().setValue(String.valueOf((newValue.intValue()))));
        this.popularityLabel.textProperty().setValue("0");
        gridPane.add(this.popularityLabel, 3, 6);

        //artists
        gridPane.add(new Label("Artists:"), 1, 7);
        this.additionalArtists.getChildren().add(this.artistBox);

        //add more artists button
        Button showArtistAdder = new Button("+");
        gridPane.add(showArtistAdder, 3, 7);
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
        gridPane.add(this.additionalArtists, 2, 7);

        //Description
        VBox showDescriptionVBox = new VBox();
        showDescriptionVBox.getChildren().add(gridPane);
        showDescriptionVBox.getChildren().add(new Label("Show description:"));
        showDescriptionVBox.getChildren().add(this.descriptionArea);

        ScrollPane artistScrollPane = new ScrollPane();
        artistScrollPane.setContent(showDescriptionVBox);
        return artistScrollPane;
    }

    /**
     * This method allows the user to Add a new Show.
     */
    public void addShowWindow() {
        BorderPane structure = new BorderPane();
        this.popUp.setTitle("Add Show");

        Label addingNew = new Label("What show do you want to add?");
        structure.setTop(addingNew);


        structure.setCenter(AddEditSetup());

        //buttons
        HBox cancelConfirmHBox = new HBox();

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> {
            ArrayList<Artist> addedArtists = new ArrayList<>();
            for (Node artistBox : this.additionalArtists.getChildren()) {
                ComboBox artistCombo = (ComboBox) artistBox;
                if (artistCombo.getSelectionModel().getSelectedItem() != null) {
                    String comboBoxString = artistCombo.getSelectionModel().getSelectedItem().toString();

                    for (Artist artist : addedArtists) {
                        if (artist.getName().equals(comboBoxString)) {
                            return;
                        }
                    }

                    Artist artist = DataController.getPlanner().getArtist(comboBoxString);
                    if (artist != null) {
                        addedArtists.add(artist);
                    }
                } else {
                    addedArtists = null;
                    // TODO: This is not necessary, so dubble check if it is removable
                }
            }

            if (verifyInput(this.startingTime, this.endingTime, this.stageBox, this.genreBox, this.nameField, this.artistBox)) {
                String showNameAdding = this.nameField.getText();
                LocalTime beginTime = indexToLocalTime(this.timeList.indexOf(startingTime.getValue()));
                LocalTime endTime = indexToLocalTime(this.timeList.indexOf(endingTime.getValue()));
                PlannerData.Stage stageAdded = DataController.getPlanner().getStage((String) stageBox.getValue());



                Show show = new Show(beginTime, endTime, addedArtists, showNameAdding, stageAdded, descriptionShow, addedGenre, popularityAdded);
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
                    DataController.getPlanner().savePlanner();
                    ST.resetData();
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
        this.popUp.setTitle("Edit Show");

        // Title
        Label editShowLabel = new Label("Edit this show:");
        borderPane.setTop(editShowLabel);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        ScrollPane artistScroller = new ScrollPane();
        artistScroller.setContent(AddEditSetup());
        // Show name
        this.nameField.setText(this.selectedShow.getName());

        //time
        this.startingTime.getSelectionModel().select(localTimeToIndex(this.selectedShow.getBeginTime()));
        this.endingTime.getSelectionModel().select(localTimeToIndex(this.selectedShow.getEndTime()));

        //stage
        this.stageBox.setValue(this.selectedShow.getStage().getName());

        //Genre
        this.genreBox.setValue(this.selectedShow.getGenre().getFancyName());

        //popularity
        this.popularitySlider.setMax(this.stagePopularity);
        this.popularitySlider.setValue(this.selectedShow.getExpectedPopularity());
        popularityLabel.textProperty().setValue(String.valueOf(this.selectedShow.getExpectedPopularity()));

        //artists
        this.artistBox.setValue(this.selectedShow.getArtists().get(0).getName());

        //add more artists
        for (int i = 1; i < this.selectedShow.getArtists().size(); i++) {
            ComboBox addedArtist = getAllArtistsComboBox();
//            addedArtist.
            addedArtist.setValue(this.selectedShow.getArtists().get(i).getName());
            this.additionalArtists.getChildren().add(addedArtist);
            addedArtist.getItems().add("None");
            addedArtist.setOnAction(e -> {
                if (addedArtist.getValue().equals("None")) {
                    this.additionalArtists.getChildren().remove(addedArtist);
                }
            });
        }

        //Description
        this.descriptionArea.setText(this.selectedShow.getDescription());

        // Buttons

        borderPane.setCenter(artistScroller);
        HBox choice = new HBox();
        Button submit = new Button("Submit");
        submit.setOnAction(event -> {
            ArrayList<Artist> addedArtists = new ArrayList<>();
            for (Node artistBox : additionalArtists.getChildren()) {
                ComboBox artistCombo = (ComboBox) artistBox;
                if (artistCombo.getSelectionModel().getSelectedItem() != null) {
                    String comboBoxString = artistCombo.getSelectionModel().getSelectedItem().toString();

                    for (Artist artist : addedArtists) {
                        if (artist.getName().equals(comboBoxString)) {
                            return;
                        }
                    }

                    Artist artist = DataController.getPlanner().getArtist(comboBoxString);
                    if (artist != null) {
                        addedArtists.add(artist);
                    }
                } else {
                    addedArtists = null;
                    // TODO: Check whether this is obsolete
                }
            }

            if (verifyInput(startingTime, endingTime, this.stageBox, this.genreBox, this.nameField, this.artistBox)) {
                LocalTime beginTime = indexToLocalTime(this.timeList.indexOf(startingTime.getValue()));
                LocalTime endTime = indexToLocalTime(this.timeList.indexOf(endingTime.getValue()));
                PlannerData.Stage stageAdded = DataController.getPlanner().getStage((String) this.stageBox.getValue());


                this.addedShow = new Show(beginTime, endTime, addedArtists, showNameAdding, stageAdded, descriptionShow, addedGenre, popularityAdded);
                if (!this.addedShow.equals(this.selectedShow)) {
                    DataController.getPlanner().deleteShow(this.selectedShow);
                    DataController.getPlanner().savePlanner();

                    for (Show existingShow : DataController.getPlanner().getShows()) {
                        if (existingShow.getStage().getName().equals(addedShow.getStage().getName())) {
                            if (addedShow.getBeginTime().isAfter(existingShow.getBeginTime()) && addedShow.getBeginTime().isBefore(existingShow.getEndTime()) || addedShow.getBeginTime().equals(existingShow.getBeginTime()) || (addedShow.getBeginTime().isBefore(existingShow.getBeginTime()) && addedShow.getEndTime().isAfter(existingShow.getEndTime()))) {
                                DataController.getPlanner().addShow(this.selectedShow);
                                DataController.getPlanner().savePlanner();
                                this.errorList.clear();
                                this.errorList.add("A show cannot begin at the same time or during another show on the same stage.");
                                new ErrorWindow(this.popUp, this.errorList);
                                return;
                            }

                            if (addedShow.getEndTime().isAfter(existingShow.getBeginTime()) && addedShow.getEndTime().isBefore(existingShow.getEndTime()) || addedShow.getEndTime().equals(existingShow.getEndTime())) {
                                DataController.getPlanner().addShow(this.selectedShow);
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
                                        DataController.getPlanner().savePlanner();
                                        this.errorList.clear();
                                        this.errorList.add("An artist cannot be at two shows at the same time");
                                        new ErrorWindow(this.popUp, this.errorList);
                                        return;
                                    }

                                    if (addedShow.getEndTime().isAfter(existingShow.getBeginTime()) && addedShow.getEndTime().isBefore(existingShow.getEndTime()) || addedShow.getEndTime().equals(existingShow.getEndTime())) {
                                        DataController.getPlanner().addShow(this.selectedShow);
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

                    DataController.getPlanner().addShow(this.addedShow);
                    DataController.getPlanner().savePlanner();
                    this.ST.resetData();
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
     *
     * @return
     */
    public Show inputcheck(){
        errorList.clear();
        //name
        if (this.nameField.getText().length() == 0) {
            this.errorList.add("The show name has not been filled in.");
        }
        else {
            String showName = this.nameField.getText();
            for (Show show : DataController.getPlanner().getShows()) {
                if (!this.selectedShow.equals(show) && showName.equals(show.getName())) {
                    this.errorList.add("There is already a show with that name");
                }
            }
        }
            //startime & endtime
            if (startingTime.getValue() == null || endingTime.getValue() == null) {
            if (startingTime.getValue() == null || startingTime.getValue().equals("Select")) {
                this.errorList.add("The begintime has not been filled in.");
            }
            if (endingTime.getValue() == null || endingTime.getValue().equals("Select")) {
                this.errorList.add("The endtime has not been filled in.");
            }
        }

        //stage
        if (this.stageBox.getValue() == null || this.stageBox.getValue().equals("Select")) {
            this.errorList.add("The stage has not been filled in.");
        }
        else {



        }
//genre
        if (genreBox.getValue() == null || genreBox.getValue().equals("Select")) {
            this.errorList.add("The genre has not been filled in.");
        }
        else{
            Genres addedGenre = Genres.getGenre(genreBox.getValue().toString());
        }
        //artist
        if (artist.getValue() == null || artist.getValue().equals("Select")) {
            this.errorList.add("An artist has not been added yet");
        }

        //pop
        if ((int) this.popularitySlider.getValue() == 0) {
            this.errorList.add("The popularity cannot be zero.");
        }
        else {
            int popularityAdded = (int) this.popularitySlider.getValue();
        }
        //description
        if (this.descriptionArea.getText().length() == 0){
            this.errorList.add("No Description ahs been added.");
        }
        else {
            String descriptionShow = descriptionArea.getText();
        }
        Show newshow;
        if (errorList.isEmpty()){
            newshow = new Show();
        }
else {
    newshow = null;
        }
        return newshow;
    }

    /**
     * This method allows the user to see the selected show that they wish to delete.
     */
    public void deleteShowWindow() {
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
                PlannerData.Stage selectedStage = DataController.getPlanner().getStage(stageBox.getValue().toString());
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
    public ComboBox getTimestampsComboBox(int startingIndex) {
        ComboBox timeBox = new ComboBox();
        timeBox.getItems().add("Select");
        timeBox.getItems().addAll(timeList.subList(startingIndex, timeList.size()));
        timeBox.getSelectionModel().selectFirst();

        return timeBox;
    }

    private void setupTimeList(){
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

               // timeBox.getItems().add(time);
            }
        }
    }

    public TextArea getShowDescriptionTextArea(String presetText, int width, int height) {
        TextArea description = new TextArea(presetText);
        if (height != 0) description.setPrefHeight(height);

        if (width != 0) description.setPrefWidth(width);
        return description;
    }

    /**
     * converts the index of the time Combobox to the corresponding time
     * @param index the index of the time selected
     * @return
     */
    public LocalTime indexToLocalTime(int index) {
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
     * @param time
     * @return
     */
    public int localTimeToIndex(LocalTime time) {
        int index = 1;
        index += time.getHour() * 2;
        if (time.getMinute() == 30) {
            index++;
        }
        return index;
    }
}