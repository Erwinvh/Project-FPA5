package GUILogic;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ScheduleTab {
    private Tab scheduleTab;
    private TableView table = new TableView();
    private VBox description = new VBox();
    private HBox controls = new HBox();
    private Stage primaryStage;

    public ScheduleTab(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.scheduleTab = new Tab("Schedule");
        layout1();
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

        baseLayer.getChildren().add(this.table);
        baseLayer.getChildren().add(this.description);

        VBox base = new VBox();
        base.getChildren().add(baseLayer);
        Controls();
        base.getChildren().add(this.controls);

        this.scheduleTab.setContent(base);
    }

    public void Controls(){
       Button addButton = new Button("Add");
        addButton.setOnAction(event -> {
            Stage adderStage = new Stage();
            adderStage.setWidth(400);
            adderStage.setHeight(450);
            adderStage.initOwner(this.primaryStage);
            adderStage.initModality(Modality.WINDOW_MODAL);

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
            TextField stage = new TextField();
            TextField genre = new TextField();
            TextField popularity = new TextField();

            inputFields.getChildren().add(beginTime);
            inputFields.getChildren().add(endTime);
            inputFields.getChildren().add(stage);
            inputFields.getChildren().add(genre);
            inputFields.getChildren().add(popularity);

            HBox inputsystem = new HBox();
            inputsystem.getChildren().add(inputID);
            inputsystem.getChildren().add(inputFields);

            structure.setCenter(inputsystem);
            HBox choice = new HBox();
            Button cancel = new Button("Cancel");
            Button submit = new Button("Submit");
            choice.getChildren().add(cancel);
            choice.getChildren().add(submit);
            structure.setBottom(choice);

            Scene adderScene = new Scene(structure);
            adderStage.setScene(adderScene);
            adderStage.show();

        });
        Button editButton = new Button("Edit");
        editButton.setOnAction(event -> {
            Stage editStage = new Stage();
            editStage.setWidth(250);
            editStage.setHeight(250);
            editStage.initOwner(this.primaryStage);
            editStage.initModality(Modality.WINDOW_MODAL);

            BorderPane structure = new BorderPane();

            Label editingThis = new Label("Edit this show:");
            structure.setTop(editingThis);
            Label information = new Label("editable info dump");
            structure.setCenter(information);

            HBox choice = new HBox();
            Button cancel = new Button("Cancel");
            Button submit = new Button("Submit");
            choice.getChildren().add(cancel);
            choice.getChildren().add(submit);
            structure.setBottom(choice);

            Scene editScene = new Scene(structure);
            editStage.setScene(editScene);
            editStage.show();

        });
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            Stage deleteStage = new Stage();
            deleteStage.setWidth(250);
            deleteStage.setHeight(250);
            deleteStage.initOwner(this.primaryStage);
            deleteStage.initModality(Modality.WINDOW_MODAL);
            BorderPane structure = new BorderPane();

            Label deleteThis = new Label("Are you sure you want to delete this show?");
            structure.setTop(deleteThis);
            Label information = new Label("Information...");
            structure.setCenter(information);

            HBox choice = new HBox();

            Button cancel = new Button("No");
//            cancel.setOnAction(event -> {
//                //cancel option
//                    });
            Button confirm = new Button("Yes");
//            confirm.setOnAction(event -> {
//                //delete file
//            });
            choice.getChildren().add(cancel);
            choice.getChildren().add(confirm);

            structure.setBottom(choice);
            Scene deleteScene = new Scene(structure);
            deleteStage.setScene(deleteScene);
            deleteStage.show();
        });

        this.controls.getChildren().add(addButton);
        this.controls.getChildren().add(editButton);
        this.controls.getChildren().add(deleteButton);
        this.controls.setSpacing(20);
    }

}