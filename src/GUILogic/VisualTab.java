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
import java.awt.geom.RoundRectangle2D;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class VisualTab {

    private Tab visualTab;
    private Canvas canvas;
    private Canvas canvasStages;
    private Planner planner;

    private int columnWidth = -1; //TODO: Add check whether this is still -1 when using this variable

    private static final int CANVAS_WIDTH = 960;
    private static final int CANVAS_HEIGHT = 2400;
    private static final int STAGE_HEIGHT = 40;
    private static final int TIME_COLUMN_WIDTH = 60;

    public VisualTab() {
        this.planner = DataController.getPlanner();

        this.visualTab = new Tab("Visual");
        this.canvas = new Canvas(CANVAS_WIDTH-6, CANVAS_HEIGHT);
        this.canvasStages = new Canvas(CANVAS_WIDTH, STAGE_HEIGHT);

        ScrollPane scrollPane = new ScrollPane(this.canvas);
        scrollPane.setPrefSize(CANVAS_WIDTH, 500);
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVvalue(LocalTime.now().getHour() / 24f);

        VBox vBox = new VBox(this.canvasStages, scrollPane);
        this.visualTab.setContent(vBox);

        drawStages(new FXGraphics2D(this.canvasStages.getGraphicsContext2D()));
        drawLayout(new FXGraphics2D(this.canvas.getGraphicsContext2D()));
        drawPlanning(new FXGraphics2D(this.canvas.getGraphicsContext2D()));
    }

    public void drawStages(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.WHITE);
        graphics.clearRect(0, 0, (int) this.canvasStages.getWidth(), (int) this.canvasStages.getHeight());
        graphics.translate(TIME_COLUMN_WIDTH, STAGE_HEIGHT);

        // Add axis lines to stages layout
        graphics.draw(new Line2D.Double(-TIME_COLUMN_WIDTH, 0, this.canvasStages.getWidth() - TIME_COLUMN_WIDTH, 0));

        this.columnWidth = (int) ((this.canvas.getWidth() - TIME_COLUMN_WIDTH) / this.planner.getStages().size());

        // Add all divider lines to stages layout
        for (int i = 0; i < planner.getStages().size(); i++) {
            graphics.draw(new Line2D.Double(i * this.columnWidth + 1, 0, i * this.columnWidth + 1, -STAGE_HEIGHT));
            graphics.drawString(this.planner.getStages().get(i).getName() + "\n" + "(cap. " + this.planner.getStages().get(i).getCapacity() + ")", (i * this.columnWidth + 10), -STAGE_HEIGHT + 15);
        }
    }

    public void drawLayout(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.WHITE);
        graphics.clearRect(0, 0, (int) this.canvas.getWidth(), (int) this.canvas.getHeight());
        graphics.translate(TIME_COLUMN_WIDTH, 0);

        // Add vertical line to divide time and shows
        graphics.draw(new Line2D.Double(0, 0, 0, this.canvas.getHeight()));

        // Draw vertical lines to divide stages
        for (int i = 0; i < this.planner.getStages().size(); i++) {
            graphics.draw(new Line2D.Double(i * this.columnWidth, 0, i * this.columnWidth, this.canvas.getHeight()));
        }

        // Draw all times to the layout
        for (int j = 1; j < 24; j++) {
            graphics.drawString(j + ":00", -50, (int) (j * this.canvas.getHeight() / 24));
            graphics.setStroke(new BasicStroke(5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 25));
            graphics.draw(new Line2D.Double());
        }
        graphics.setStroke(new BasicStroke(10f));
    }

    public void drawPlanning(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.translate(TIME_COLUMN_WIDTH, 0);

        //System.out.println(this.planner.getShows().size());

        for (Stage stage : this.planner.getStages()) {
            for (Show show : this.planner.getShows()) {
                if (show.getStage().equals(stage)) {
                    double timeDecimalBeginTime = show.getBeginTime().getHour() + (show.getBeginTime().getMinute() / 60.0);
                    double timeDecimalEndTime = show.getEndTime().getHour() + (show.getEndTime().getMinute() / 60.0);
                    graphics.draw(new RoundRectangle2D.Double(((this.planner.getStages().indexOf(stage)) * this.columnWidth) + 5, timeDecimalBeginTime * (this.canvas.getHeight() / 24.0), this.columnWidth - 10, (timeDecimalEndTime - timeDecimalBeginTime) * (this.canvas.getHeight() / 24.0), 25, 10));
                    String artists = "";
                    for (Artist artist : show.getArtists()) {
                        artists += artist.getName() + ", ";
                    }

                    artists = artists.substring(0, artists.length() - 2);
                    graphics.drawString(show.getBeginTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " - " + show.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")) + "\n" + artists, ((this.planner.getStages().indexOf(stage)) * this.columnWidth) + 10, (int) (timeDecimalBeginTime * (this.canvas.getHeight() / 24) + 20));
                }
            }
        }
    }

    public Tab getVisualTab() {
        return visualTab;
    }
}