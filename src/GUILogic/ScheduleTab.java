package GUILogic;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ScheduleTab {
    private Tab scheduleTab;
    private TableView table = new TableView();

    public ScheduleTab() {
        this.scheduleTab = new Tab("Schedule");
        layout1();
    }

    public Tab getScheduleTab() {
        return scheduleTab;
    }

    public void layout1(){

        HBox baseLayer = new HBox();
        baseLayer.setSpacing(10);

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
        this.table.setPrefWidth(735);

        this.table.getColumns().addAll(beginTimeCol, endTimeCol, stageCol, artistCol, genreCol, popularityCol);

        VBox description = new VBox();

        Image baseImage = new Image("file:Resources/PersonImageBase.jpg");
        ImageView Artistpicture = new ImageView(baseImage);
        Artistpicture.setFitHeight(200);
        Artistpicture.setFitWidth(200);
//        Artistpicture.setScaleY(0.5);
        description.getChildren().add(Artistpicture);
        TextArea artistDescription = new TextArea("Description of artist 1");
        artistDescription.setEditable(false);

        description.getChildren().add(artistDescription);

        baseLayer.getChildren().add(this.table);
        baseLayer.getChildren().add(description);

        HBox Controls = new HBox();

        Button addButton = new Button("Add");
        addButton.setOnAction(event -> {
            Stage adderStage = new Stage();

            Label addingNew = new Label("Add a new show");

            Scene adderScene = new Scene(addingNew);
            adderStage.setScene(adderScene);
            adderStage.show();

        });
        Button editButton = new Button("Edit");
        editButton.setOnAction(event -> {
            Stage editStage = new Stage();

            Label editingThis = new Label("Edit this show");

            Scene editScene = new Scene(editingThis);
            editStage.setScene(editScene);
            editStage.show();

        });
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            Stage deleteStage = new Stage();

            Label deleteThis = new Label("delete this show");

            Scene deleteScene = new Scene(deleteThis);
            deleteStage.setScene(deleteScene);
            deleteStage.show();

        });

        Controls.getChildren().add(addButton);
        Controls.getChildren().add(editButton);
        Controls.getChildren().add(deleteButton);
        Controls.setSpacing(20);

        VBox base = new VBox();

        base.getChildren().add(baseLayer);
        base.getChildren().add(Controls);

        this.scheduleTab.setContent(base);
    }
}