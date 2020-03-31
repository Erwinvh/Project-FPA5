package GUILogic;

import PlannerData.Artist;
import PlannerData.Show;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
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
import javafx.util.Callback;

import java.util.ArrayList;

public class ScheduleTab {
    private Tab scheduleTab;
    private TableView<Show> table = new TableView<>();
    private VBox descriptionVBox = new VBox();
    private ScrollPane allDescriptions = new ScrollPane();
    private HBox controls = new HBox();
    private Stage primaryStage;
    private Show selectedItem = null;
    private ArrayList<String> errorList = new ArrayList<>();
    private ObservableList<Show> data = FXCollections.observableArrayList();

    /**
     * The constructor of the schedule tab
     *
     * @param primaryStage The stage in which the tab is placed
     */
    public ScheduleTab(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.scheduleTab = new Tab("Schedule");
        this.allDescriptions.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.allDescriptions.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaX() != 0) {
                event.consume();
            }
        });

        createLayout();
        int descriptionBoxWidth = 450;
        this.allDescriptions.setPrefWidth(descriptionBoxWidth);
    }

    /**
     * This is the getter of the Schedule tab, it allows the Gui to show the schedule tab in the application.
     *
     * @return The schedule tab
     */
    public Tab getScheduleTab() {
        return this.scheduleTab;
    }

    /**
     * This method creates the createTable which shows the user all shows that are currently planned.
     */
    public void createTable() {
        this.table.setEditable(false);

        TableColumn nameColumn = new TableColumn("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));

        TableColumn beginTimeCol = new TableColumn("Begin time");
        beginTimeCol.setCellValueFactory(new PropertyValueFactory<>("beginTimeString"));

        TableColumn endTimeCol = new TableColumn("End time");
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTimeString"));

        TableColumn stageCol = new TableColumn("Stage");
        stageCol.setCellValueFactory(new PropertyValueFactory<>("StageName"));

        TableColumn artistCol = new TableColumn("Artists");
        artistCol.setCellValueFactory(new PropertyValueFactory<>("ArtistsNames"));

        TableColumn genreCol = new TableColumn("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("GenreFancyName"));

        TableColumn popularityCol = new TableColumn("Popularity");
        popularityCol.setCellValueFactory(new PropertyValueFactory<>("expectedPopularity"));

        this.table.setPrefWidth(800);
        this.table.setPrefHeight(600);

        this.table.getColumns().addAll(nameColumn, beginTimeCol, endTimeCol, stageCol, artistCol, genreCol, popularityCol);

        fillTable();
    }

    /**
     * This method fills the table with information
     */
    public void fillTable() {
        this.table.getItems().clear();
        this.data.addAll(DataController.getPlanner().getShows());
        this.table.getItems().addAll(DataController.getPlanner().getShows());
        this.table.setItems(this.data);
        this.table.getSelectionModel().selectFirst();
        this.table.setOnMouseClicked(event -> getShowDescription());
    }

    /**
     * This method creates the descriptionVBox that shows the Artists of the selected show.
     */
    public void getShowDescription() {
        this.selectedItem = this.table.getSelectionModel().getSelectedItem();
        int numberOfArtists = 0;

        try {
            this.descriptionVBox = new VBox();
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

                ImageView artistPicture = new ImageView(new Image("file:Resources/PersonImageBase.jpg"));

                int artistPicSize = 200;
                artistPicture.setFitHeight(artistPicSize);
                artistPicture.setFitWidth(artistPicSize);

                descriptionStructure.add(artistPicture, 1, 2);
                TextArea genresTextArea = new TextArea(artist.getGenre().getFancyName());

                int genreBoxWidth = 250;
                genresTextArea.setPrefWidth(genreBoxWidth);
                genresTextArea.setEditable(false);
                descriptionStructure.add(genresTextArea, 2, 2);

                TextArea artistDescription = new TextArea(artist.getDescription());
                artistDescription.setEditable(false);
                int artistDescrWidth = 450;
                artistDescription.setMaxWidth(artistDescrWidth);
                this.descriptionVBox.getChildren().add(descriptionStructure);
                this.descriptionVBox.getChildren().add(artistDescription);
            }

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

            descriptionBase.getChildren().addAll(showDescriptionTextFlow, this.descriptionVBox);
            this.allDescriptions.setContent(descriptionBase);

        } catch (Exception e) {
            this.allDescriptions.setContent(new Label("Select a Show for more information"));
        }
    }

    /**
     * This methode resets the data that is shown in the table
     */
    public void resetData() {
        this.data = FXCollections.observableArrayList();
        fillTable();
    }

    /**
     * This method creates the base Layout of the Schedule tab by calling the separate pieces.
     */
    public void createLayout() {
        HBox baseLayer = new HBox();
        baseLayer.setSpacing(10);
        createTable();
        getShowDescription();

        baseLayer.getChildren().add(this.table);
        baseLayer.getChildren().add(this.allDescriptions);

        VBox base = new VBox();
        base.getChildren().add(baseLayer);
        getButtons();
        base.getChildren().add(this.controls);

        this.scheduleTab.setContent(base);
    }

    /**
     * This method creates the Buttons to the Add, Edit and Delete menus.
     */
    private void getButtons() {
        //Add show button
        Button addButton = new Button("Add Show");
        addButton.setOnAction(event -> {
            new ShowWindow(1, this.primaryStage, this, null);
        });

        //Edit show button
        Button editButton = new Button("Edit Show");
        editButton.setOnAction(event -> {
            try {
                this.selectedItem = this.table.getSelectionModel().getSelectedItem();
                new ShowWindow(2, this.primaryStage, this, this.selectedItem);
            } catch (Exception e) {
                this.errorList.clear();
                this.errorList.add("No show has been selected.");
                new ErrorWindow(this.primaryStage, this.errorList);
            }
        });

        //Delete show button
        Button deleteButton = new Button("Delete Show");
        deleteButton.setOnAction(event -> {
            try {
                this.selectedItem = this.table.getSelectionModel().getSelectedItem();
                new ShowWindow(3, this.primaryStage, this, this.selectedItem);
            } catch (Exception e) {
                this.errorList.clear();
                this.errorList.add("No show has been selected.");
                new ErrorWindow(this.primaryStage, this.errorList);
            }
        });

        //Add artist button
        Button addArtistButton = new Button("Add Artist");
        addArtistButton.setOnAction(event -> new ArtistWindow(1, this.primaryStage, this));

        //Edit artist button
        Button editArtistButton = new Button("Edit Artist");
        editArtistButton.setOnAction(event -> {
            if (DataController.getPlanner().getArtists().isEmpty()) {
                this.errorList.clear();
                this.errorList.add("There is no Artist to edit.");
                new ErrorWindow(this.primaryStage, this.errorList);
            } else {
                new ArtistWindow(2, this.primaryStage, this);
            }
        });

        //Delete artist button
        Button deleteArtistButton = new Button("Delete Artist");
        deleteArtistButton.setOnAction(event -> {
            if (DataController.getPlanner().getArtists().isEmpty()) {
                this.errorList.clear();
                this.errorList.add("There is no Artist to delete.");
                new ErrorWindow(this.primaryStage, this.errorList);
            } else {
                new ArtistWindow(3, this.primaryStage, this);
            }
        });

        //Add stage button
        Button addStageButton = new Button("Add Stage");
        addStageButton.setOnAction(event -> {
            if (DataController.getPlanner().getStages().size() <= 5) {
                new StageWindow(4, this.primaryStage, this);
            } else {
                this.errorList.clear();
                this.errorList.add("You cannot exceed the maximum of 6 stages.");
                new ErrorWindow(this.primaryStage, this.errorList);
            }
        });

        //Edit stage button
        Button editStageButton = new Button("Edit Stage");
        editStageButton.setOnAction(event -> {
            if (DataController.getPlanner().getStages().isEmpty()) {
                this.errorList.clear();
                this.errorList.add("There is no stage to edit.");
                new ErrorWindow(this.primaryStage, this.errorList);
            } else {
                new StageWindow(5, this.primaryStage, this);
            }
        });

        //Delete stage button
        Button deleteStageButton = new Button("Delete Stage");
        deleteStageButton.setOnAction(event -> {
            if (DataController.getPlanner().getStages().isEmpty()) {
                this.errorList.clear();
                this.errorList.add("There is no stage to delete.");
                new ErrorWindow(this.primaryStage, this.errorList);
            } else {
                new StageWindow(6, this.primaryStage, this);
            }
        });

        this.controls.getChildren().addAll(addButton, editButton, deleteButton);

        this.controls.getChildren().add(new Label("             "));

        this.controls.getChildren().addAll(addArtistButton, editArtistButton, deleteArtistButton);

        this.controls.getChildren().add(new Label("             "));

        this.controls.getChildren().addAll(addStageButton, editStageButton, deleteStageButton);

        this.controls.setSpacing(20);
        this.controls.setPadding(new Insets(10));
    }
}