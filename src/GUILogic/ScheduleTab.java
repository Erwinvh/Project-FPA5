package GUILogic;

import PlannerData.Artist;
import PlannerData.Show;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ScheduleTab {
    private Tab scheduleTab;
    private TableView<Show> table = new TableView();
    private VBox description = new VBox();
    private ScrollPane allDescriptions = new ScrollPane();
    private HBox controls = new HBox();
    private Stage primaryStage;
    private Show selectedItem = this.table.getSelectionModel().getSelectedItem();
    private TableViewSelectionModel selectionModel = this.table.getSelectionModel();
    private ObservableList<Show> selectedItems = this.selectionModel.getSelectedItems();
    private ArrayList<String> errorList = new ArrayList<>();
    private ObservableList<Show> data = FXCollections.observableArrayList();

    public ScheduleTab(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.scheduleTab = new Tab("Schedule");
        this.allDescriptions.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.allDescriptions.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaX() != 0){
                    event.consume();
                }
            }
        });
        layout();
        int descriptionBoxWidth = 450;
        this.allDescriptions.setPrefWidth(descriptionBoxWidth);
    }

    /**
     * This is the getter of the Schedule tab, it allows the Gui to show the schedule tab in the application.
     *
     * @return Tab
     */
    public Tab getScheduleTab() {
        return scheduleTab;
    }

    /**
     * This method creates the table which shows the user all shows that are currently planned.
     */
    public void table() {
        this.table.setEditable(false);
        for (Show show : DataController.getPlanner().getShows()) {
            this.data.add(show);
        }

        TableColumn nameColumn = new TableColumn("Name");
        nameColumn.setPrefWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));

        TableColumn beginTimeCol = new TableColumn("Begin time");
        beginTimeCol.setPrefWidth(100);
        beginTimeCol.setCellValueFactory(new PropertyValueFactory<>("beginTimeString"));

        TableColumn endTimeCol = new TableColumn("End time");
        endTimeCol.setPrefWidth(100);
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTimeString"));

        TableColumn stageCol = new TableColumn("Stage");
        stageCol.setPrefWidth(100);
        stageCol.setCellValueFactory(new PropertyValueFactory<>("StageName"));

        TableColumn artistCol = new TableColumn("Artists");
        artistCol.setPrefWidth(300);
        artistCol.setCellValueFactory(new PropertyValueFactory<>("ArtistsNames"));

        TableColumn genreCol = new TableColumn("Genre");
        genreCol.setPrefWidth(100);
        genreCol.setCellValueFactory(new PropertyValueFactory<>("GenreFancyName"));

        TableColumn popularityCol = new TableColumn("Popularity");
        popularityCol.setPrefWidth(100);
        popularityCol.setCellValueFactory(new PropertyValueFactory<>("expectedPopularity"));
        this.table.setPrefWidth(800);

        this.table.getColumns().addAll(nameColumn, beginTimeCol, endTimeCol, stageCol, artistCol, genreCol, popularityCol);

        this.table.getItems().addAll(DataController.getPlanner().getShows());

        this.table.setItems(this.data);
        this.table.getSelectionModel().selectFirst();
        this.table.setOnMouseClicked(event->{
            description();
        });
    }

    /**
     * This method creates the description that shows the Artists of the selected show.
     */
    public void description() {
        this.selectedItem = this.table.getSelectionModel().getSelectedItem();
        int numberOfArtists = 0;

        try {
            this.description = new VBox();
            for (Artist artist : this.selectedItem.getArtists()) {
                numberOfArtists++;
                GridPane descriptionStructure = new GridPane();
                TextField artistName = new TextField(artist.getName());
                artistName.setEditable(false);
                descriptionStructure.add(artistName, 1, 1);

                TextField artistAmount = new TextField("Number " + numberOfArtists + "out of " + this.selectedItem.getArtists().size());
                artistAmount.setEditable(false);
                artistAmount.setPrefWidth(150);
                descriptionStructure.add(artistAmount, 2, 1);

                Image artistImage = new Image("file:Resources/PersonImageBase.jpg");
                ImageView Artistpicture = new ImageView(artistImage);

                int artistPicSize = 200;
                Artistpicture.setFitHeight(artistPicSize);
                Artistpicture.setFitWidth(artistPicSize);

                descriptionStructure.add(Artistpicture, 1, 2);
                TextArea Genres = new TextArea(artist.getGenre().getFancyName());

                int genreBoxWidth = 250;
                Genres.setPrefWidth(genreBoxWidth);
                Genres.setEditable(false);
                descriptionStructure.add(Genres, 2, 2);

                TextArea artistDescription = new TextArea(artist.getDescription());
                artistDescription.setEditable(false);
                int artistDescrWidth = 450;
                artistDescription.setMaxWidth(artistDescrWidth);
                this.description.getChildren().add(descriptionStructure);
                this.description.getChildren().add(artistDescription);
            }

//            VBox descriptionBase = new VBox();
//            descriptionBase.getChildren().add(new Label("Show:"));
//
//            TextArea showDescription = new TextArea(this.selectedItem.getName().toUpperCase() + "\n\n\t\tShow description:\n" + this.selectedItem.getDescription());
//            showDescription.setEditable(false);
//            descriptionBase.getChildren().add(showDescription);
//            descriptionBase.getChildren().add(this.description);
//            this.allDescriptions.setContent(descriptionBase);

            VBox descriptionBase = new VBox();
            descriptionBase.getChildren().add(new Label("Show:"));
            TextFlow showDescriptionTextFlow = new TextFlow();


            Text textTitle = new Text(this.selectedItem.getName());
            textTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 40");

            Text textDescrTitle = new Text("\n\n Show Description:");
            textDescrTitle.setStyle("-fx-font-weight: bold;");

            Text textDescription = new Text("\n" + this.selectedItem.getDescription());
            showDescriptionTextFlow.getChildren().addAll(textTitle, textDescrTitle, textDescription);
            showDescriptionTextFlow.setMaxWidth(450);

            descriptionBase.getChildren().addAll(showDescriptionTextFlow, this.description);
            this.allDescriptions.setContent(descriptionBase);

        } catch (Exception e) {
            this.allDescriptions.setContent(new Label("Select a Show for more information"));
        }
    }

    /**
     * This method creates the base Layout of the Schedule tab by calling the separate pieces.
     */
    public void layout() {
        HBox baseLayer = new HBox();
        baseLayer.setSpacing(10);
        table();
        description();

        baseLayer.getChildren().add(this.table);
        baseLayer.getChildren().add(this.allDescriptions);

        VBox base = new VBox();
        base.getChildren().add(baseLayer);
        Controls();
        base.getChildren().add(this.controls);

        this.scheduleTab.setContent(base);
    }

    /**
     * This method creates the Buttons to the Add, Edit and Delete menus.
     */
    private void Controls() {
        Button addButton = new Button("Add Show");
        addButton.setOnAction(event -> {
            new BaseControls(1, this.primaryStage, this.data, this.table, this.selectedItem);
        });

        Button editButton = new Button("Edit Show");
        editButton.setOnAction(event -> {
            try {
                this.selectedItem = this.table.getSelectionModel().getSelectedItem();
                new BaseControls(2, this.primaryStage, this.data, this.table, this.selectedItem);
            } catch (Exception e) {
                this.errorList.clear();
                this.errorList.add("No show has been selected.");
                new ErrorWindow(this.primaryStage, this.errorList);
            }
        });

        Button deleteButton = new Button("Delete Show");
        deleteButton.setOnAction(event -> {
            try {
                this.selectedItem = this.table.getSelectionModel().getSelectedItem();
                new BaseControls(3, this.primaryStage, this.data, this.table, this.selectedItem);
            } catch (Exception e) {
                this.errorList.clear();
                this.errorList.add("No show has been selected.");
                new ErrorWindow(this.primaryStage, this.errorList);
            }
        });

        Button addArtistButton = new Button("Add Artist");
        addArtistButton.setOnAction(event -> {
                new ArtistWindow(1, this.primaryStage);
        });

        Button editArtistButton = new Button("Edit Artist");
        editArtistButton.setOnAction(event -> {
            if (DataController.getPlanner().getArtists().isEmpty()){
                this.errorList.clear();
                this.errorList.add("There is no Artist to edit.");
                new ErrorWindow(this.primaryStage,this.errorList);
            }else{
                new ArtistWindow(2, this.primaryStage);
            }
        });

        Button deleteArtistButton = new Button("Delete Artist");
        deleteArtistButton.setOnAction(event -> {
            if (DataController.getPlanner().getArtists().isEmpty()){
                this.errorList.clear();
                this.errorList.add("There is no Artist to delete.");
                new ErrorWindow(this.primaryStage,this.errorList);
            }else{
                new ArtistWindow(3, this.primaryStage);
            }
        });

        Button addStageButton = new Button("Add Stage");
        addStageButton.setOnAction(event -> {
            if (DataController.getPlanner().getStages().size()<=5){
                new StageWindow(4, this.primaryStage);
            }
            else{
                this.errorList.clear();
                this.errorList.add("You cannot exceed the maximum of 6 stages.");
                new ErrorWindow(this.primaryStage,this.errorList);
            }
        });

        Button editStageButton = new Button("Edit Stage");
        editStageButton.setOnAction(event -> {
            if (DataController.getPlanner().getStages().isEmpty()){
                this.errorList.clear();
                this.errorList.add("There is no stage to edit.");
                new ErrorWindow(this.primaryStage,this.errorList);
            }else{
                new StageWindow(5, this.primaryStage);
            }
        });

        Button deleteStageButton = new Button("Delete Stage");
        deleteStageButton.setOnAction(event -> {
            if (DataController.getPlanner().getStages().isEmpty()){
                this.errorList.clear();
                this.errorList.add("There is no stage to delete.");
                new ErrorWindow(this.primaryStage,this.errorList);
            }else{
                new StageWindow(6, this.primaryStage);
            }
        });

        this.controls.getChildren().add(addButton);
        this.controls.getChildren().add(editButton);
        this.controls.getChildren().add(deleteButton);

        this.controls.getChildren().add(new Label("             "));

        this.controls.getChildren().add(addArtistButton);
        this.controls.getChildren().add(editArtistButton);
        this.controls.getChildren().add(deleteArtistButton);

        this.controls.getChildren().add(new Label("             "));

        this.controls.getChildren().add(addStageButton);
        this.controls.getChildren().add(editStageButton);
        this.controls.getChildren().add(deleteStageButton);

        this.controls.setSpacing(20);
        this.controls.setPadding(new Insets(10));
    }
}