package GUILogic;

import Enumerators.Genres;
import PlannerData.Artist;
import PlannerData.Planner;
import PlannerData.Show;
import PlannerData.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;


public class VisualTab {
    private Tab visualTab;
    private Canvas canvas;
    private Canvas canvasStages;
    private int amountOfColumns;
    private int columnWith;
    private int startTime;
    private int endTime;
    private Planner planner;

    public VisualTab(){
        this.planner = new Planner();

        System.out.println(this.planner.getShows());

        this.planner.getStages().add(new Stage(1, "Test"));
        this.planner.getStages().add(new Stage(1, "Barry"));

        this.visualTab = new Tab("Visual");
        this.canvas = new Canvas(960, 2400);
        this.canvasStages = new Canvas(960, 40);

        ScrollPane scrollPane = new ScrollPane(this.canvas);
        scrollPane.setPrefSize(960, 500);
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);

        VBox vBox = new VBox(this.canvasStages, scrollPane);
        this.visualTab.setContent(vBox);
        drawStages(new FXGraphics2D(this.canvasStages.getGraphicsContext2D()));
        drawLayout(new FXGraphics2D(this.canvas.getGraphicsContext2D()));
        drawPlanning(new FXGraphics2D(this.canvas.getGraphicsContext2D()));
    }

    public void drawStages(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.translate(60, 40);

        graphics.draw(new Line2D.Double(-60, 0, this.canvasStages.getWidth()-60, 0));


        graphics.draw(new Line2D.Double(0, 0, 0, -40));
        int i = 0;
        for (Stage stage : this.planner.getStages()) {
            i++;
            graphics.draw(new Line2D.Double(i * ((this.canvasStages.getWidth() - 60) / this.planner.getStages().size()), 0, i * ((this.canvasStages.getWidth() - 60) / this.planner.getStages().size()), -40));
            graphics.drawString(stage.getName(), (int) ((i - 1) * ((this.canvasStages.getWidth() - 60) / this.planner.getStages().size()) + 10), -15);
        }
    }

    public void drawLayout(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.translate(60, 0);

        graphics.draw(new Line2D.Double(0, 0, 0, this.canvas.getHeight()));

        int i = 0;
        for (Stage stage : this.planner.getStages()) {
            i++;
            graphics.draw(new Line2D.Double(i * ((this.canvas.getWidth() - 60) / this.planner.getStages().size()), 0, i * ((this.canvas.getWidth() - 60) / this.planner.getStages().size()), this.canvas.getHeight()));
        }

        for (int j = 1; j < 24; j++) {
            graphics.drawString(j + ":00", -50, (int) (j * this.canvas.getHeight() / 24));
        }
    }

    public void drawPlanning(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.translate(60, 0);

        this.columnWith = (int) ((this.canvas.getWidth() - 60) / this.planner.getStages().size());

        System.out.println(this.planner.getShows().size());

        for (Show show : this.planner.getShows()) {
            int stageCounter = 1;
            for (Stage stage : this.planner.getStages()) {
                if (stage == show.getStage()) {
                    break;
                }
                else
                stageCounter++;
            }
            if (stageCounter <= this.planner.getStages().size()) {
                double timeDecimalBeginTime = show.getBeginTime().minusHours(this.startTime).getHour() + (show.getBeginTime().getMinute()/60);
                double timeDecimalEndTime = show.getEndTime().minusHours(this.startTime).getHour() + (show.getEndTime().getMinute()/60);

                graphics.draw(new RoundRectangle2D.Double(((stageCounter - 1) * this.columnWith) + 4, timeDecimalBeginTime * (this.canvas.getHeight() / 24), this.columnWith - 8, (timeDecimalEndTime - timeDecimalBeginTime) * (this.canvas.getHeight() / 24), 25, 10));
                String artists = "";
                for (Artist artist : show.getArtists()) {
                    artists += artist.getName() + ", ";
                }
                artists = artists.substring(0, artists.length()-2);
                artists += ".";
                graphics.drawString(show.getBeginTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " - " +show.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")) + "\n" + artists, ((stageCounter - 1) * this.columnWith) + 10, (int) (timeDecimalBeginTime * (this.canvas.getHeight() / 24) + 20));

            }
        }
    }

    public Tab getVisualTab(){
        return visualTab;
    }
}
