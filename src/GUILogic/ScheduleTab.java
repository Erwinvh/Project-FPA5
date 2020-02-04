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
    private Stage popUp = new Stage();
    private Button confirm = new Button("Confirm");
    private Button cancel = new Button("Cancel");

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
        Button submit = new Button("Submit");
        submit.setOnAction(event -> {
            this.popUp.close();
        });

        choice.getChildren().add(this.cancel);
        choice.getChildren().add(submit);
        structure.setBottom(choice);

        Scene adderScene = new Scene(structure);
        this.popUp.setScene(adderScene);
        this.popUp.show();
    }

    public void editoryWindow(){
        BorderPane structure = new BorderPane();

        Label editingThis = new Label("Edit this show:");
        structure.setTop(editingThis);
        Label information = new Label("editable info dump");
        structure.setCenter(information);

        HBox choice = new HBox();

        Button submit = new Button("Submit");
        submit.setOnAction(event -> {
            this.popUp.close();
        });

        choice.getChildren().add(this.cancel);
        choice.getChildren().add(submit);
        structure.setBottom(choice);

        Scene editScene = new Scene(structure);
        this.popUp.setScene(editScene);
        this.popUp.show();
    }

    public void deletionWindow(){
        BorderPane structure = new BorderPane();

        Label deleteThis = new Label("Are you sure you want to delete this show?");
        structure.setTop(deleteThis);
        Label information = new Label("Information...");
        structure.setCenter(information);

        HBox choice = new HBox();

        Button confirm = new Button("Yes");
        confirm.setOnAction(event -> {
            this.popUp.close();
        });
        choice.getChildren().add(this.cancel);
        choice.getChildren().add(confirm);

        structure.setBottom(choice);
        Scene deleteScene = new Scene(structure);
        this.popUp.setScene(deleteScene);
        this.popUp.show();
    }

}