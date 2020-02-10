package GUILogic;

import Enumerators.Genres;
import PlannerData.Artist;
import PlannerData.Planner;
import PlannerData.Show;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ScheduleTab {
    private Tab scheduleTab;
    private TableView table = new TableView();
    private VBox description = new VBox();
    private ScrollPane allDescriptions = new ScrollPane();
    private HBox controls = new HBox();
    private Stage primaryStage;
    private Stage popUp = new Stage();
    private Button confirm = new Button("Confirm");
    private Button cancel = new Button("Cancel");
    private String selected = "test";
    private ArrayList<String> errorlist = new ArrayList<>();
    private ArrayList<String> timelist = new ArrayList<>();
    private int additionalArtists = 0;
    private ObservableList<Show> data = FXCollections.observableArrayList();

    public ScheduleTab(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.scheduleTab = new Tab("Schedule");
        layout1();
        this.popUp.setWidth(400);
        this.popUp.setHeight(450);
        this.popUp.initOwner(this.primaryStage);
        this.popUp.initModality(Modality.WINDOW_MODAL);
        this.allDescriptions.setPrefWidth(600);
    }

    public Tab getScheduleTab() {
        return scheduleTab;
    }

    public void table() {
        this.table.setEditable(false);
        for (Show show : DataController.getPlanner().getShows()) {
            this.data.add(show);
        }
        TableColumn beginTimeCol = new TableColumn("Begin time");
        beginTimeCol.setPrefWidth(100);
        beginTimeCol.setCellValueFactory(
                new PropertyValueFactory<>("beginTimeString"));
        TableColumn endTimeCol = new TableColumn("End time");
        endTimeCol.setPrefWidth(100);
        endTimeCol.setCellValueFactory(
                new PropertyValueFactory<>("endTimeString"));
        TableColumn stageCol = new TableColumn("Stage");
        stageCol.setPrefWidth(100);
        stageCol.setCellValueFactory(
                new PropertyValueFactory<>("StageName"));
        TableColumn artistCol = new TableColumn("Artists");
        artistCol.setPrefWidth(100);
        artistCol.setCellValueFactory(
                new PropertyValueFactory<>("ArtistsNames"));
        TableColumn genreCol = new TableColumn("Genre");
        genreCol.setPrefWidth(100);
        genreCol.setCellValueFactory(
                new PropertyValueFactory<>("genre"));
        TableColumn popularityCol = new TableColumn("Popularity");
        popularityCol.setPrefWidth(100);
        popularityCol.setCellValueFactory(
                new PropertyValueFactory<>("expectedPopularity"));
        this.table.setPrefWidth(800);

        this.table.getColumns().addAll(beginTimeCol, endTimeCol, stageCol, artistCol, genreCol, popularityCol);


        this.table.setItems(this.data);
    }

    public void desciption() {

        //for each
        for (int i = 0; i < 2; i++) {
            GridPane descriptionStructure = new GridPane();
            TextField artistName = new TextField("get artistname (i)");
            artistName.setEditable(false);
            descriptionStructure.add(artistName, 1, 1);

            TextField artistamount = new TextField("Number 1 out of (i)");
            artistamount.setEditable(false);
            artistamount.setPrefWidth(150);
            descriptionStructure.add(artistamount, 2, 1);

            Image baseImage = new Image("file:Resources/PersonImageBase.jpg");
            ImageView Artistpicture = new ImageView(baseImage);
            Artistpicture.setFitHeight(200);
            Artistpicture.setFitWidth(200);
            descriptionStructure.add(Artistpicture, 1, 2);
            TextArea Genres = new TextArea("Genre #1" + '\n' + "Genre #2");
            Genres.setPrefWidth(150);
            Genres.setEditable(false);
            descriptionStructure.add(Genres, 2, 2);

            TextArea artistDescription = new TextArea("Description of artist 1");
            artistDescription.setEditable(false);
            artistDescription.setPrefWidth(150);
            this.description.getChildren().add(descriptionStructure);
            this.description.getChildren().add(artistDescription);
        }
        this.allDescriptions.setContent(this.description);
    }

    public void layout1() {
        HBox baseLayer = new HBox();
        baseLayer.setSpacing(10);
        table();
        desciption();
        cancelsetup();

        baseLayer.getChildren().add(this.table);
        baseLayer.getChildren().add(this.allDescriptions);

        VBox base = new VBox();
        base.getChildren().add(baseLayer);
        Controls();
        base.getChildren().add(this.controls);

        this.scheduleTab.setContent(base);
    }

    public void cancelsetup() {
        this.cancel.setOnAction(event -> {
            this.popUp.close();
        });
    }

    public void Controls() {
        Button addButton = new Button("Add");
        addButton.setOnAction(event -> {
            additionWindow();
        });
        Button editButton = new Button("Edit");
        editButton.setOnAction(event -> {
            editoryWindow();
        });
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            deletionWindow();
        });

        this.controls.getChildren().add(addButton);
        this.controls.getChildren().add(editButton);
        this.controls.getChildren().add(deleteButton);
        this.controls.setSpacing(20);
        this.controls.setPadding(new Insets(10));
    }

    public void additionWindow() {
        this.additionalArtists = 0;
        BorderPane structure = new BorderPane();

        Label addingNew = new Label("what show do you want to add?");
        structure.setTop(addingNew);

        GridPane inputStructure = new GridPane();
        inputStructure.setHgap(10);
        inputStructure.setVgap(10);
        Label showName = new Label("Show name:");
        TextField inputShowName = new TextField();
        inputStructure.add(showName, 1, 1);
        inputStructure.add(inputShowName, 2, 1);
        inputStructure.add(new Label("Begin time:"), 1, 2);
        inputStructure.add(new Label("End time:"), 1, 3);
        ComboBox beginUur = timeBox();
        ComboBox eindUur = timeBox();
        inputStructure.add(beginUur, 2, 2);
        inputStructure.add(eindUur, 2, 3);
        inputStructure.add(new Label("Stage:"), 1, 4);
        inputStructure.add(new Label("Genre:"), 1, 5);
        inputStructure.add(new Label("Popularity:"), 1, 6);
        inputStructure.add(new Label("Artists:"), 1, 7);

        ComboBox stage = StageBox();
        inputStructure.add(stage, 2, 4);
        ComboBox genre = genreBox();
        inputStructure.add(genre, 2, 5);
        Slider popularity = new Slider();
        popularity.setMin(0);
        popularity.setMax(100);
        popularity.setValue(0);
        popularity.setShowTickLabels(true);
        popularity.setShowTickMarks(true);
        popularity.setMajorTickUnit(50);
        popularity.setMinorTickCount(5);
        popularity.setBlockIncrement(10);
        inputStructure.add(popularity, 2, 6);
        Label PopularityLabel = new Label("");
        popularity.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldValue,
                    Number newValue) {
                PopularityLabel.textProperty().setValue(
                        String.valueOf(newValue.intValue()));
            }
        });

        PopularityLabel.textProperty().setValue("0");

        inputStructure.add(PopularityLabel, 3, 6);
        ComboBox artists = artistBox();
        inputStructure.add(artists, 2, 7);

        Button showArtistAdder = new Button("+");
        inputStructure.add(showArtistAdder, 3, 7);
        showArtistAdder.setOnAction(event -> {
            this.additionalArtists++;
            ComboBox ArtistAdded = artistBox();
            inputStructure.add(ArtistAdded, 2, 7 + this.additionalArtists);
            ArtistAdded.setOnAction(e -> {
                if (ArtistAdded.getValue().equals("None")) {
                    inputStructure.getChildren().remove(ArtistAdded);
                    this.additionalArtists--;
                }
            });
        });

        ScrollPane ArtistScroller = new ScrollPane();
        ArtistScroller.setContent(inputStructure);
        structure.setCenter(ArtistScroller);
        HBox choice = new HBox();
        Button submit = new Button("Submit");
        submit.setOnAction(event -> {

            control(beginUur, eindUur, stage, genre, inputShowName);
            String showNameAdding = inputShowName.getText();
            LocalTime beginTime = indexToLocalTime(this.timelist.indexOf(beginUur.getValue()));
            LocalTime endTime = indexToLocalTime(this.timelist.indexOf(eindUur.getValue()));
            PlannerData.Stage stageAdded = stringToStage((String) stage.getValue());
            ObservableList<ComboBox> artistBox = artists.getItems();
            ArrayList<Artist> addedArtists = new ArrayList<>();
                String comboBoxString =  artists.getSelectionModel().getSelectedItem().toString();
                boolean containsArtist = false;
                for(Artist artist : addedArtists){
                    if(artist.getName().equals(comboBoxString)){
                        containsArtist = true;
                    }
                }
                if(!containsArtist){
                    Artist artist = stringToArtist(comboBoxString);
                    if(artist != null) {
                        addedArtists.add(artist);
                    }
                }


            Genres addedGenre = stringToGenre(genre.getValue().toString());

            int popularityAdded = (int) (stageAdded.getCapacity() * popularity.getValue());

            //if(!addedArtists.isEmpty() && addedArtists.get(0) != null && stageAdded != null && beginTime != null && endTime != null && addedGenre != null && !showNameAdding.isEmpty()){
                ArrayList<Genres> genres = new ArrayList<>();
                genres.add(addedGenre);
                Show show = new Show(beginTime,endTime,addedArtists,showNameAdding,stageAdded,"",genres,popularityAdded);
                DataController.getPlanner().addShow(show);
                data.add(show);
           // }


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

    public void editoryWindow() {
        BorderPane structure = new BorderPane();

        Label editingThis = new Label("Edit this show:");
        structure.setTop(editingThis);

        GridPane inputStructure = new GridPane();
        inputStructure.setHgap(10);
        inputStructure.setVgap(10);
        Label showName = new Label("Show name:");
        TextField inputShowName = new TextField();
        inputStructure.add(showName, 1, 1);
        inputStructure.add(inputShowName, 2, 1);
        inputStructure.add(new Label("Begin time:"), 1, 2);
        inputStructure.add(new Label("End time:"), 1, 3);
        ComboBox beginUur = timeBox();
        ComboBox eindUur = timeBox();
        inputStructure.add(beginUur, 2, 2);
        inputStructure.add(eindUur, 2, 3);
        inputStructure.add(new Label("Stage:"), 1, 4);
        inputStructure.add(new Label("Genre:"), 1, 5);
        inputStructure.add(new Label("Popularity:"), 1, 6);
        inputStructure.add(new Label("Artists:"), 1, 7);

        ComboBox stage = StageBox();
        inputStructure.add(stage, 2, 4);
        ComboBox genre = genreBox();
        inputStructure.add(genre, 2, 5);
        Slider popularity = new Slider();
        popularity.setMin(0);
        popularity.setMax(100);
        popularity.setValue(0);
        popularity.setShowTickLabels(true);
        popularity.setShowTickMarks(true);
        popularity.setMajorTickUnit(50);
        popularity.setMinorTickCount(5);
        popularity.setBlockIncrement(10);
        inputStructure.add(popularity, 2, 6);
        Label PopularityLabel = new Label("");
        popularity.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldValue,
                    Number newValue) {
                PopularityLabel.textProperty().setValue(
                        String.valueOf(newValue.intValue()));
            }
        });

        PopularityLabel.textProperty().setValue("0");

        inputStructure.add(PopularityLabel, 3, 6);
        ComboBox artists = artistBox();
        inputStructure.add(artists, 2, 7);

        Button showArtistAdder = new Button("+");
        inputStructure.add(showArtistAdder, 3, 7);
        showArtistAdder.setOnAction(event -> {
            this.additionalArtists++;
            ComboBox ArtistAdded = artistBox();
            inputStructure.add(ArtistAdded, 2, 7 + this.additionalArtists);
            ArtistAdded.setOnAction(e -> {
                if (ArtistAdded.getValue().equals("None")) {
                    inputStructure.getChildren().remove(ArtistAdded);
                    this.additionalArtists--;
                }
            });
        });

        ScrollPane ArtistScroller = new ScrollPane();
        ArtistScroller.setContent(inputStructure);
        structure.setCenter(ArtistScroller);
        HBox choice = new HBox();
        Button submit = new Button("Submit");
        submit.setOnAction(event -> {

            control(beginUur, eindUur, stage, genre, inputShowName);

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

    public void deletionWindow() {
        BorderPane structure = new BorderPane();

        Label deleteThis = new Label("Are you sure you want to delete this show?");
        structure.setTop(deleteThis);
        Label information = new Label("From " + this.selected + " to " + this.selected + '\n'
                + "By " + this.selected + " in the genre of " + this.selected + '\n' +
                "On stage " + this.selected + '\n' +
                "Expected popularity is " + this.selected + " people.");
        structure.setCenter(information);

        HBox choice = new HBox();

        Button confirm = new Button("Confirm");
        confirm.setOnAction(event -> {
            this.popUp.close();
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

    public void errorWindow() {

        Stage errorPopUp = new Stage();
        errorPopUp.setWidth(500);
        errorPopUp.setResizable(false);
        errorPopUp.setHeight(250);
        errorPopUp.initOwner(this.popUp);
        errorPopUp.initModality(Modality.WINDOW_MODAL);
        HBox baseStructure = new HBox();
        Image error = new Image("file:Resources/alert.png");
        ImageView showError = new ImageView(error);
        showError.setFitWidth(100);
        showError.setFitHeight(100);
        baseStructure.getChildren().add(showError);
        VBox errorList = new VBox();
        for (String Error : this.errorlist) {
            errorList.getChildren().add(new Label(Error));
        }
        errorList.getChildren().add(new Label("Please resolve these errors before submitting."));
        errorList.setAlignment(Pos.CENTER);
        baseStructure.getChildren().add(errorList);
        Scene errorScene = new Scene(baseStructure);
        errorScene.getStylesheets().add("Window-StyleSheet.css");

        errorPopUp.setScene(errorScene);
        errorPopUp.show();
    }

    public void control(ComboBox beginUur, ComboBox eindUur, ComboBox stage, ComboBox genre, TextField showName) {
        this.errorlist.clear();
        int beginIndex = this.timelist.indexOf(beginUur.getValue());
        int endIndex = this.timelist.indexOf(eindUur.getValue());
        if (showName.getText().length() == 0) {
            this.errorlist.add("The show name has not been filled in.");
        }
        if (beginUur.getValue() == null || eindUur.getValue() == null) {
            if (beginUur.getValue() == null) {
                this.errorlist.add("The begintime has not been filled in.");
            }
            if (eindUur.getValue() == null) {
                this.errorlist.add("The endtime has not been filled in.");
            }
        } else {
            if (beginIndex > endIndex) {
                this.errorlist.add("The begintime is later than the endtime.");
            } else if (beginIndex == endIndex) {
                this.errorlist.add("The begintime the same as the endtime.");
            }
        }
        if (stage.getValue() == null) {
            this.errorlist.add("The stage has not been filled in.");
        }
        if (genre.getValue() == null) {
            this.errorlist.add("The genre has not been filled in.");
        }


        if (this.errorlist.isEmpty()) {
//            DataController.getPlanner().addShow(new Show(beginUur.getValue()));
            //TODO: LocalTime stuff when adding new show
            this.popUp.close();
        } else {
            errorWindow();
        }
    }

    public void newArtistControl(TextField ArtistName, TextArea ArtistDescription) {
        this.errorlist.clear();
        if (ArtistName.getText().length() == 0) {
            this.errorlist.add("The artist's name has not been filled in.");
        }
        if (ArtistDescription.getText().length() == 0) {
            this.errorlist.add("The artist's description has not been filled in.");
        }
        if (this.errorlist.isEmpty()) {

        } else {
            errorWindow();
        }
    }

    public void newStageControl(TextField stageName, TextField capacity) {
        this.errorlist.clear();
        if (stageName.getText().length() == 0) {
            this.errorlist.add("The stage name has not been filled in.");
        }
        if (capacity.getText().length() == 0) {
            this.errorlist.add("The capacity has not been filled in.");
        } else {
            try {
                int cap = Integer.parseInt(capacity.getText());
            } catch (Exception e) {
                this.errorlist.add("The capacity must be a Number.");
            }

        }
        if (this.errorlist.isEmpty()) {

        } else {
            errorWindow();
        }
    }

    public void artistAddWindow() {
        Stage artistAddWindow = new Stage();
        artistAddWindow.setWidth(200);
        artistAddWindow.setHeight(400);
        artistAddWindow.initOwner(this.popUp);
        artistAddWindow.initModality(Modality.WINDOW_MODAL);

        VBox newArtistList = new VBox();
        TextField artistName = new TextField();

        TextArea artistDescription = new TextArea();
        Label ArtistNameLabel = new Label("Artist name:");
        newArtistList.getChildren().add(ArtistNameLabel);
        newArtistList.getChildren().add(artistName);

        //genre
        Label ArtistGenreLabel = new Label("Artist's Genres:");
        newArtistList.getChildren().add(ArtistGenreLabel);
        GridPane Genres = new GridPane();
        Genres.setHgap(10);
        Genres.setVgap(10);
//        for (Enumerators.Genres genre: ???) {
//            Genres.add(, , x, y)
//        }


        Genres.add(new CheckBox("Nightcore"), 1, 1);
        Genres.add(new CheckBox("Jazz"), 1, 2);
        Genres.add(new CheckBox("Rock"), 2, 1);
        Genres.add(new CheckBox("K-Pop"), 2, 2);


//        for (Genres genre : Enumerators.Genres.values()) {
//            Genres.add(new CheckBox(),2,2);
//        }


        newArtistList.getChildren().add(Genres);

//        newArtistList.getChildren().add(artistDescription);

//        private Image image;

        Label ArtistpictureLabel = new Label("Artist's picture:");
        newArtistList.getChildren().add(ArtistpictureLabel);

//        FileChooser imageChooser = new FileChooser();
//        imageChooser.getExtensionFilters().addAll(
//                new FileChooser.ExtensionFilter("jpg Files", "*.jpg")
//                ,new FileChooser.ExtensionFilter("png Files", "*.png")
//        );
//        Button openButton = new Button("Choose Image");
//
//        openButton.setOnAction(event -> {
//                        File selectedFile = imageChooser.showOpenDialog(this.popUp);
//
//                });


        HBox choice = new HBox();
        Button stop = new Button("Cancel");
        stop.setOnAction(event -> {
            artistAddWindow.close();
        });
        choice.getChildren().add(stop);
        Button confirm = new Button("Confirm");
        confirm.setOnAction(event -> {
            newArtistControl(artistName, artistDescription);
            Planner planner = DataController.getPlanner();
            ArrayList<Genres> genres = new ArrayList<>();
            ObservableList<Node> checkBoxes = Genres.getChildren();
            for(Node checkbox : checkBoxes){
                CheckBox box = (CheckBox) checkbox;
                if(box.isSelected()){
                    genres.add(stringToGenre(box.getText()));
                }
            }
            String description = artistDescription.getText();
            String name = artistName.getText();
            DataController.getPlanner().addArtist(name,genres.get(0),description);
        });
        choice.getChildren().add(confirm);
        choice.setPadding(new Insets(10));
        choice.setSpacing(20);
        Label ArtistdescriptionLabel = new Label("Artist's description:");
        newArtistList.getChildren().add(ArtistdescriptionLabel);
        newArtistList.getChildren().add(artistDescription);
        newArtistList.getChildren().add(choice);
        Scene artistAddScene = new Scene(newArtistList);
        artistAddScene.getStylesheets().add("Window-StyleSheet.css");
        artistAddWindow.setScene(artistAddScene);
        artistAddWindow.show();
    }

    public ComboBox genreBox() {
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().add("None");
        comboBox.getItems().addAll(Genres.values());
        return comboBox;
    }

    public ComboBox StageBox() {
        ComboBox comboBox = new ComboBox();

        for (PlannerData.Stage stage : DataController.getPlanner().getStages()) {
            comboBox.getItems().add(stage.getName());
        }

        comboBox.getItems().add("Add new Stage");

        comboBox.setOnAction(event -> {
            if (comboBox.getValue().equals("Add new Stage")) {
                stageAddWindow();
            }
        });

        return comboBox;
    }

    public ComboBox artistBox() {
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().add("None");
        for (Artist artist : DataController.getPlanner().getArtists()) {
            comboBox.getItems().add(artist.getName());
        }
        comboBox.getItems().add("Add new Artist");

        comboBox.setOnAction(event -> {
            if (comboBox.getValue().equals("Add new Artist")) {
                artistAddWindow();
            }
        });

        return comboBox;
    }

    public ComboBox timeBox() {
        ComboBox uurBox = new ComboBox();
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
                if (!this.timelist.contains(time)) {
                    this.timelist.add(time);
                }
                uurBox.getItems().add(time);
            }
        }
        return uurBox;
    }

    public void stageAddWindow() {
        Stage stageAddWindow = new Stage();
        stageAddWindow.setWidth(200);
        stageAddWindow.setHeight(250);
        stageAddWindow.initOwner(this.popUp);
        stageAddWindow.initModality(Modality.WINDOW_MODAL);

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
            stageAddWindow.close();
        });
        choice.getChildren().add(stop);
        Button confirm = new Button("Confirm");
        confirm.setOnAction(event -> {
            newStageControl(StageName,InputTextField);
            DataController.getPlanner().addStage(Integer.parseInt(InputTextField.getText()),StageName.getText());
        });
        choice.getChildren().add(confirm);
        choice.setPadding(new Insets(10));
        choice.setSpacing(20);

        newStageList.getChildren().add(choice);
        Scene artistAddScene = new Scene(newStageList);
        artistAddScene.getStylesheets().add("Window-StyleSheet.css");
        stageAddWindow.setScene(artistAddScene);
        stageAddWindow.show();
    }

    public Genres stringToGenre(String genreString){
        for(Genres genre : Genres.values()){
            if(genre.toString().equals(genreString)){
                return genre;
            }
        }
        return null;
    }

    public LocalTime indexToLocalTime(int index){
        LocalTime time = LocalTime.MIDNIGHT;
        int hours = index / 2;
        if(index % 2 == 1){
            time = time.plusMinutes(30);
        }
        time = time.plusHours(hours);
        return time;
    }

    public PlannerData.Stage stringToStage(String stageString){
        for(PlannerData.Stage stage : DataController.getPlanner().getStages()){
            if(stageString.equals(stage.getName())){
                return stage;
            }
        }
        return null;
    }

    public Artist stringToArtist(String artistString){
        for(Artist artist : DataController.getPlanner().getArtists()){
            if(artistString.equals(artist.getName())){
                return artist;
            }
        }
        return null;
    }

}