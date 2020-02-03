package GUILogic;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
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

        description.getChildren().add(new Image("file:PersonImageBase.jpg"));
        description.getChildren().add(new TextField());

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