package GUILogic;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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

        table.setEditable(false);

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

        table.getColumns().addAll(beginTimeCol, endTimeCol, stageCol, artistCol, genreCol, popularityCol);

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

        baseLayer.getChildren().add(table);
        baseLayer.getChildren().add(description);

        HBox Controls = new HBox();

        Controls.getChildren().add(new Button("Add"));
        Controls.getChildren().add(new Button("Edit"));
        Controls.getChildren().add(new Button("Delete"));
        Controls.setSpacing(10);

        VBox base = new VBox();

        base.getChildren().add(baseLayer);
        base.getChildren().add(Controls);

        this.scheduleTab.setContent(base);
    }
}