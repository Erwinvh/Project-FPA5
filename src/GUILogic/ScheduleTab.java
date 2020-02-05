package GUILogic;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;

public class ScheduleTab {
    private Tab scheduleTab;
    private TableView table = new TableView();
    private VBox description = new VBox();
    private HBox controls = new HBox();
    private Stage primaryStage;
    private Stage popUp = new Stage();
    private Button confirm = new Button("Confirm");
    private Button cancel = new Button("Cancel");
    private String selected = "test";
    private ArrayList<String> errorlist = new ArrayList<>();

    public ScheduleTab(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.scheduleTab = new Tab("Schedule");
        layout1();
        this.popUp.setWidth(400);
        this.popUp.setHeight(450);
        this.popUp.initOwner(this.primaryStage);
        this.popUp.initModality(Modality.WINDOW_MODAL);
    }

    public Tab getScheduleTab() {
        return scheduleTab;
    }

    public void table(){
        this.table.setEditable(false);

        TableColumn beginTimeCol = new TableColumn("Begin time");
        beginTimeCol.setPrefWidth(100);
        TableColumn endTimeCol = new TableColumn("End time");
        endTimeCol.setPrefWidth(100);
        TableColumn stageCol = new TableColumn("Stage");
        stageCol.setPrefWidth(100);
        TableColumn artistCol = new TableColumn("Artist");
        artistCol.setPrefWidth(100);
        TableColumn genreCol = new TableColumn("Genre");
        genreCol.setPrefWidth(100);
        TableColumn popularityCol = new TableColumn("Popularity");
        popularityCol.setPrefWidth(100);
        this.table.setPrefWidth(875);

        this.table.getColumns().addAll(beginTimeCol, endTimeCol, stageCol, artistCol, genreCol, popularityCol);
    }

    public void testsetup(){
        this.errorlist.add("hello");
        this.errorlist.add("this is an error");
    }

    public void desciption(){
        Image baseImage = new Image("file:Resources/PersonImageBase.jpg");
        ImageView Artistpicture = new ImageView(baseImage);
        Artistpicture.setFitHeight(200);
        Artistpicture.setFitWidth(200);

        TextArea artistDescription = new TextArea("Description of artist 1");
        artistDescription.setEditable(false);


        this.description.getChildren().add(Artistpicture);
        this.description.getChildren().add(artistDescription);
    }

    public void layout1(){
        HBox baseLayer = new HBox();
        baseLayer.setSpacing(10);
        table();
        desciption();
        cancelsetup();

        testsetup();
        
        baseLayer.getChildren().add(this.table);
        baseLayer.getChildren().add(this.description);

        VBox base = new VBox();
        base.getChildren().add(baseLayer);
        Controls();
        base.getChildren().add(this.controls);

        this.scheduleTab.setContent(base);
    }

    public void cancelsetup(){
        this.cancel.setOnAction(event -> {
            this.popUp.close();
        });
    }

    public void Controls(){
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

    public void additionWindow(){
        BorderPane structure = new BorderPane();

        Label addingNew = new Label("what show do you want to add?");
        structure.setTop(addingNew);

        GridPane inputStructure = new GridPane();
        inputStructure.setHgap(10);
        inputStructure.setVgap(10);
        inputStructure.add(new Label("Begin time:"),1,1);
        inputStructure.add(new Label("End time:"),1,2);
        ComboBox beginUur = uurBox();
        ComboBox eindUur = uurBox();
        inputStructure.add(beginUur,2,1);
        inputStructure.add(eindUur,2,2);
        ComboBox beginMinuut = minuutBox();
        ComboBox eindMinuut = minuutBox();
        inputStructure.add(beginMinuut,3,1);
        inputStructure.add(eindMinuut,3,2);
        inputStructure.add(new Label("Stage:"),1,3);
        inputStructure.add(new Label("Genre:"),1,4);
        inputStructure.add(new Label("Popularity:"),1,5);
        inputStructure.add(new Label("Artists:"),1,6);

        ComboBox stage = StageBox();
        inputStructure.add(stage,2,3);
        ComboBox genre = genreBox();
        inputStructure.add(genre,2,4);
        Slider popularity = new Slider();
        popularity.setMin(0);
        popularity.setMax(100);
        popularity.setValue(0);
        popularity.setShowTickLabels(true);
        popularity.setShowTickMarks(true);
        popularity.setMajorTickUnit(50);
        popularity.setMinorTickCount(5);
        popularity.setBlockIncrement(10);
        inputStructure.add(popularity,2,5);
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

inputStructure.add(PopularityLabel,3,5);
        ComboBox artists = artistBox();
        inputStructure.add(artists,2,6);

        structure.setCenter(inputStructure);
        HBox choice = new HBox();
        Button submit = new Button("Submit");
        submit.setOnAction(event -> {
            artistAddWindow();
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

    public void editoryWindow(){
        BorderPane structure = new BorderPane();

        Label editingThis = new Label("Edit this show:");
        structure.setTop(editingThis);

        GridPane inputStructure = new GridPane();
        inputStructure.setHgap(10);
        inputStructure.setVgap(10);
        inputStructure.add(new Label("Begin time:"),1,1);
        inputStructure.add(new Label("End time:"),1,2);
        ComboBox beginUur = uurBox();
        ComboBox eindUur = uurBox();
        inputStructure.add(beginUur,2,1);
        inputStructure.add(eindUur,2,2);
        ComboBox beginMinuut = minuutBox();
        ComboBox eindMinuut = minuutBox();
        inputStructure.add(beginMinuut,3,1);
        inputStructure.add(eindMinuut,3,2);
        inputStructure.add(new Label("Stage:"),1,3);
        inputStructure.add(new Label("Genre:"),1,4);
        inputStructure.add(new Label("Popularity:"),1,5);
        inputStructure.add(new Label("Artists:"),1,6);

        ComboBox stage = StageBox();
        inputStructure.add(stage,2,3);
        ComboBox genre = genreBox();
        inputStructure.add(genre,2,4);
        Slider popularity = new Slider();
        popularity.setMin(0);
        popularity.setMax(100);
        popularity.setValue(0);
        popularity.setShowTickLabels(true);
        popularity.setShowTickMarks(true);
        popularity.setMajorTickUnit(50);
        popularity.setMinorTickCount(5);
        popularity.setBlockIncrement(10);
        inputStructure.add(popularity,2,5);
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

        inputStructure.add(PopularityLabel,3,5);
        ComboBox artists = artistBox();
        inputStructure.add(artists,2,6);

        structure.setCenter(inputStructure);

        HBox choice = new HBox();

        Button submit = new Button("Submit");
        submit.setOnAction(event -> {
            this.popUp.close();
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

    public void deletionWindow(){
        BorderPane structure = new BorderPane();

        Label deleteThis = new Label("Are you sure you want to delete this show?");
        structure.setTop(deleteThis);
        Label information = new Label("From "+ this.selected + " to " + this.selected + '\n'
                + "By " + this.selected + " in the genre of " + this.selected + '\n' +
                "On stage " + this.selected + '\n'+
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

    public void errorWindow(){

        Stage errorPopUp = new Stage();
        errorPopUp.setWidth(200);
        errorPopUp.setHeight(250);
        errorPopUp.initOwner(this.popUp);
        errorPopUp.initModality(Modality.WINDOW_MODAL);
        VBox errorList = new VBox();
        for (String Error : this.errorlist) {
            errorList.getChildren().add(new Label(Error));
        }
        Scene errorScene = new Scene(errorList);
        errorScene.getStylesheets().add("Window-StyleSheet.css");

        errorPopUp.setScene(errorScene);
        errorPopUp.show();
    }

    public void control(){
        try{
            Integer.parseInt(this.selected);
        }
        catch(Exception e){

        }



    }

    public void artistAddWindow(){
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
        Genres.add(new CheckBox("Nightcore"),1,1);
        Genres.add(new CheckBox("Jazz"), 1,2);
        Genres.add(new CheckBox("Rock"),2,1);
        Genres.add(new CheckBox("K-Pop"),2,2);
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
            artistAddWindow.close();
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

    public ComboBox genreBox (){
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().add("None");
//        for (Planner.genre genre:Planner) {
//            comboBox.getItems().add(genre);
//        }
        return comboBox;
    }

    public ComboBox StageBox (){
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().add("None");
        comboBox.getItems().add("Add new Stage");
        comboBox.setOnAction(event -> {
            if (comboBox.getValue().equals("Add new Stage")){
                stageAddWindow();
            }
        });

        return comboBox;
    }

    public ComboBox artistBox(){
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().add("None");
        comboBox.getItems().add("Add new Artist");

        comboBox.setOnAction(event -> {
            if (comboBox.getValue().equals("Add new Artist")){
                artistAddWindow();
            }
        });





        return comboBox;
    }

public ComboBox uurBox (){
        ComboBox uurBox = new ComboBox();
    for (int i=0; i<=23; i++){
        if (i<10){
            uurBox.getItems().add("0"+i);
        }
        else {
            uurBox.getItems().add(""+i);
        }
    }
    return uurBox;
}
    public ComboBox minuutBox (){
        ComboBox minuutBox = new ComboBox();
        for (int i=0; i<=55; i+=5){
            if (i<10){
                minuutBox.getItems().add("0"+i);
            }
            else {
                minuutBox.getItems().add(""+i);
            }
        }
        return minuutBox;
    }

    public void stageAddWindow(){
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
            stageAddWindow.close();
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

}