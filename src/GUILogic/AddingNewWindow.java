package GUILogic;

import Enumerators.Genres;
import PlannerData.Planner;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class AddingNewWindow {

    private Stage upperStage;
    private Stage currentStage = new Stage();
    private ArrayList<String> errorlist = new ArrayList<>();
    private FileChooser fileChooser = new FileChooser();
    private ImageView ProfielImage = new ImageView();

    public AddingNewWindow(int ScreenNumber, Stage upperStage) {
        this.upperStage = upperStage;
        this.ProfielImage.setFitWidth(100);
        this.ProfielImage.setFitHeight(100);
        if (ScreenNumber ==1){
            stageAddWindow();
        }
        else{
            artistAddWindow();
        }
    }

    public void stageAddWindow() {
        this.currentStage.setWidth(200);
        this.currentStage.setHeight(250);
        this.currentStage.initOwner(this.upperStage);
        this.currentStage.initModality(Modality.WINDOW_MODAL);

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
            this.currentStage.close();
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
        this.currentStage.setScene(artistAddScene);
        this.currentStage.show();
    }

    public void artistAddWindow() {
        this.currentStage.setWidth(200);
        this.currentStage.setHeight(400);
        this.currentStage.initOwner(this.upperStage);
        this.currentStage.initModality(Modality.WINDOW_MODAL);

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
        newArtistList.getChildren().add(this.ProfielImage);
        Button PictureInput = new Button();
        newArtistList.getChildren().add(PictureInput);
        PictureInput.setOnAction(event -> {
            File selectedFile = fileChooser.showOpenDialog(this.currentStage);
            fileChooser.setInitialDirectory(new File("Resources"+DataController.getPlanner().getArtists().size()));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PNG Files", "*.png")
                    ,new FileChooser.ExtensionFilter("Jpg Files", "*.jpg")
                    , new FileChooser.ExtensionFilter("Jpeg Files", "*.jpeg")
            );
            try{
                BufferedImage bufferedImage = ImageIO.read(selectedFile);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                ProfielImage.setImage(image);
            }catch(Exception e){
                System.out.println("failed image");
                System.out.println("Exception:" + e);
            }

        });








        HBox choice = new HBox();
        Button stop = new Button("Cancel");
        stop.setOnAction(event -> {
            this.currentStage.close();
        });
        choice.getChildren().add(stop);
        Button confirm = new Button("Confirm");
        confirm.setOnAction(event -> {
            newArtistControl(artistName, artistDescription);
            Planner planner = DataController.getPlanner();
            ArrayList<Enumerators.Genres> genres = new ArrayList<>();
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
        this.currentStage.setScene(artistAddScene);
        this.currentStage.show();
    }

    public Genres stringToGenre(String genreString){
        for(Genres genre : Genres.values()){
            if(genre.getFancyName().equals(genreString)){
                return genre;
            }
        }
        return null;
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
            new ErrorWindow(this.currentStage, this.errorlist);
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
            new ErrorWindow(this.currentStage, this.errorlist);
        }
    }

}
