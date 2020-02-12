package GUILogic;

import Enumerators.Genres;
import PlannerData.Planner;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
        this.ProfielImage.setFitWidth(200);
        this.ProfielImage.setFitHeight(200);
        this.currentStage.initOwner(this.upperStage);
        this.currentStage.initModality(Modality.WINDOW_MODAL);
        this.currentStage.setResizable(false);
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
            DataController.getPlanner().addStage(Integer.parseInt(capacity.getText()),stageName.getText());
            this.currentStage.close();
        } else {
            new ErrorWindow(this.currentStage, this.errorlist);
        }
    }

    public void artistAddWindow() {
        this.currentStage.setWidth(275);
        this.currentStage.setHeight(400);

        VBox newArtistList = new VBox();
        newArtistList.setPrefWidth(250);

        //artist name
        Label ArtistNameLabel = new Label("Artist name:");
        TextField artistName = new TextField();
        artistName.setPrefWidth(250);
        newArtistList.getChildren().add(ArtistNameLabel);
        newArtistList.getChildren().add(artistName);

        //genre
        Label ArtistGenreLabel = new Label("Artist's Genres:");
        newArtistList.getChildren().add(ArtistGenreLabel);
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().add("None");
        for(Genres genre : Enumerators.Genres.values()){
            comboBox.getItems().add(genre.getFancyName());
        }
        newArtistList.getChildren().add(comboBox);

        //picture
        Label ArtistpictureLabel = new Label("Artist's picture:");
        newArtistList.getChildren().add(ArtistpictureLabel);
        newArtistList.getChildren().add(this.ProfielImage);
        Button PictureInput = new Button("Add a picture");
        newArtistList.getChildren().add(PictureInput);
        newArtistList.getChildren().add(new Label("the picture wil be resized"));
        newArtistList.getChildren().add(new Label("to 200X200 pixels"));


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
                this.errorlist.add("this is not an image file: choose a Jpeg/Jpg/PNG file");
                new ErrorWindow(this.currentStage,this.errorlist);
                System.out.println("failed image");
                System.out.println("Exception:" + e);
            }
        });

        //artist description
        Label ArtistdescriptionLabel = new Label("Artist's description:");
        TextArea artistDescription = new TextArea();
        artistDescription.setPrefWidth(250);
        newArtistList.getChildren().add(ArtistdescriptionLabel);
        newArtistList.getChildren().add(artistDescription);

        //buttons
        HBox choice = new HBox();
        Button stop = new Button("Cancel");
        stop.setOnAction(event -> {
            this.currentStage.close();
        });
        choice.getChildren().add(stop);
        Button confirm = new Button("Confirm");
        confirm.setOnAction(event -> {
            newArtistControl(artistName, artistDescription, comboBox.getValue().toString(), ProfielImage.getImage(), artistDescription);
        });
        choice.getChildren().add(confirm);
        choice.setPadding(new Insets(10));
        choice.setSpacing(20);
        newArtistList.getChildren().add(choice);
        ScrollPane ArtistScroller = new ScrollPane();
        ArtistScroller.setContent(newArtistList);
        newArtistList.setAlignment(Pos.CENTER);

        Scene artistAddScene = new Scene(ArtistScroller);
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

    public void newArtistControl(TextField ArtistName, TextArea ArtistDescription, String Genre, Image image, TextArea Description) {
        this.errorlist.clear();
        if (ArtistName.getText().length() == 0) {
            this.errorlist.add("The artist's name has not been filled in.");
        }
        if (ArtistDescription.getText().length() == 0) {
            this.errorlist.add("The artist's description has not been filled in.");
        }
        if (this.errorlist.isEmpty()) {
            try{
                DataController.getPlanner().addArtist(ArtistName.getText(), stringToGenre(Genre), image, Description.getText());
                this.currentStage.close();
            }catch(Exception e){

            }
        } else {
            new ErrorWindow(this.currentStage, this.errorlist);
        }
    }

}
