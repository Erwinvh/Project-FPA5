package GUILogic;

import PlannerData.Artist;
import PlannerData.Planner;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jdk.internal.util.xml.impl.Input;

import java.io.File;
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
        this.cancel.setStyle("-fx-background-color: FF0000; ");
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
    }

    public void additionWindow(){
        BorderPane structure = new BorderPane();

        Label addingNew = new Label("what show do you want to add?");
        structure.setTop(addingNew);
        Label information = new Label("info dump");

        VBox inputID = new VBox();
        inputID.getChildren().add(new Label("Begin time:"));
        inputID.getChildren().add(new Label("End time:"));
        inputID.getChildren().add(new Label("Stage:"));
        inputID.getChildren().add(new Label("Genre:"));
        inputID.getChildren().add(new Label("popularity:"));
        inputID.getChildren().add(new Label("Artists:"));
        inputID.setSpacing(10);

        VBox inputFields = new VBox();
        TextField beginTime = new TextField();
        TextField endTime = new TextField();
        ComboBox stage = StageBox();
        ComboBox genre = genreBox();
        Slider popularity = new Slider();
        popularity.setMin(0);
        popularity.setMax(100);
        popularity.setValue(0);
        popularity.setShowTickLabels(true);
        popularity.setShowTickMarks(true);
        popularity.setMajorTickUnit(50);
        popularity.setMinorTickCount(5);
        popularity.setBlockIncrement(10);
        ComboBox artists = artistBox();

        inputFields.getChildren().add(beginTime);
        inputFields.getChildren().add(endTime);
        inputFields.getChildren().add(stage);
        inputFields.getChildren().add(genre);
        inputFields.getChildren().add(popularity);
        inputFields.getChildren().add(artists);


        HBox inputsystem = new HBox();
        inputsystem.getChildren().add(inputID);
        inputsystem.getChildren().add(inputFields);

        structure.setCenter(inputsystem);
        HBox choice = new HBox();
        Button submit = new Button("Submit");
        submit.setOnAction(event -> {
            artistAddWindow();
        });
        submit.setStyle("-fx-background-color: #228B22; ");

        choice.getChildren().add(this.cancel);
        choice.getChildren().add(submit);
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

        VBox inputID = new VBox();
        inputID.getChildren().add(new Label("Begin time:"));
        inputID.getChildren().add(new Label("End time:"));
        inputID.getChildren().add(new Label("Stage:"));
        inputID.getChildren().add(new Label("Genre:"));
        inputID.getChildren().add(new Label("popularity:"));
        inputID.getChildren().add(new Label("Artists:"));
        inputID.setSpacing(10);

        VBox inputFields = new VBox();
        TextField beginTime = new TextField();
        TextField endTime = new TextField();
        ComboBox stage = StageBox();
        ComboBox genre = genreBox();
        Slider popularity = new Slider();
        popularity.setMin(0);
        popularity.setMax(100);
        popularity.setValue(40);
        popularity.setShowTickLabels(true);
        popularity.setShowTickMarks(true);
        popularity.setMajorTickUnit(50);
        popularity.setMinorTickCount(5);
        popularity.setBlockIncrement(10);
        beginTime.setText(this.selected);
        endTime.setText(this.selected);
        ComboBox artists = artistBox();

        inputFields.getChildren().add(beginTime);
        inputFields.getChildren().add(endTime);
        inputFields.getChildren().add(stage);
        inputFields.getChildren().add(genre);
        inputFields.getChildren().add(popularity);
        inputFields.getChildren().add(artists);

        HBox inputsystem = new HBox();
        inputsystem.getChildren().add(inputID);
        inputsystem.getChildren().add(inputFields);

        structure.setCenter(inputsystem);

        HBox choice = new HBox();

        Button submit = new Button("Submit");
        submit.setOnAction(event -> {
            this.popUp.close();
        });
        submit.setStyle("-fx-background-color: #228B22; ");

        choice.getChildren().add(this.cancel);
        choice.getChildren().add(submit);
        structure.setBottom(choice);

        Scene editScene = new Scene(structure);
        editScene.getStylesheets().add("Main-StyleSheet.css");
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
        confirm.setStyle("-fx-background-color: #228B22; ");
        confirm.setOnAction(event -> {
            this.popUp.close();
        });

        choice.getChildren().add(this.cancel);
        choice.getChildren().add(confirm);
        choice.setAlignment(Pos.CENTER);
        choice.setSpacing(20);

        structure.setBottom(choice);
        Scene deleteScene = new Scene(structure);
        deleteScene.getStylesheets().add("Main-StyleSheet.css");
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
        artistAddWindow.setHeight(250);
        artistAddWindow.initOwner(this.popUp);
        artistAddWindow.initModality(Modality.WINDOW_MODAL);

        VBox newArtistList = new VBox();
        TextField artistName = new TextField();

        TextArea artistDescription = new TextArea();

        newArtistList.getChildren().add(artistName);

        //genre

//        newArtistList.getChildren().add(artistDescription);

//        private Image image;
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
        stop.setStyle("-fx-background-color: FF0000; ");
        choice.getChildren().add(stop);
        Button confirm = new Button("Confirm");
        confirm.setOnAction(event -> {
            this.popUp.close();
        });
        confirm.setStyle("-fx-background-color: #228B22; ");
        choice.getChildren().add(confirm);

        newArtistList.getChildren().add(artistDescription);
        newArtistList.getChildren().add(choice);
        Scene artistAddScene = new Scene(newArtistList);
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




}