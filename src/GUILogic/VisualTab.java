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

    private int columnWidth = -1;

    public VisualTab() {
        this.planner = new Planner();

        ArrayList<Artist> artists = new ArrayList<>();
        artists.add(new Artist("Arne de Beer", Genres.BLUES, "Smoking hot"));
        artists.add(new Artist("Lars Giskes", Genres.PUNK_ROCK, "The legend of Spoderman"));
        artists.add(new Artist("Henk", Genres.METAL, "Dit is Henk"));

        ArrayList<Stage> stages = new ArrayList<>();
        stages.add(new Stage(500, "Main Stage"));
        stages.add(new Stage(100, "Second Stage"));

        planner.addShow(new Show(LocalTime.now(), LocalTime.now().plusMinutes(30), stages.get(0), artists.get(0), 400));
        planner.addShow(new Show(LocalTime.now().plusMinutes(45), LocalTime.now().plusHours(2), stages.get(0), artists.get(1), 400));
        planner.addShow(new Show(LocalTime.now(), LocalTime.now().plusMinutes(30), stages.get(1), artists.get(2), 75));

        this.visualTab = new Tab("Visual");
        this.canvas = new Canvas(960, 2400);
        this.canvasStages = new Canvas(960, 40);

        ScrollPane scrollPane = new ScrollPane(this.canvas);
        scrollPane.setPrefSize(960, 500);
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
        graphics.translate(60, 40);

        // Add axis lines to stages layout
        graphics.draw(new Line2D.Double(-60, 0, this.canvasStages.getWidth() - 60, 0));
        graphics.draw(new Line2D.Double(0, 0, 0, -40));

        this.columnWidth = (int) ((this.canvas.getWidth() - 60) / this.planner.getStages().size());

        // Add all divider lines to stages layout
        for (int i = 0; i < planner.getStages().size(); i++) {
            graphics.draw(new Line2D.Double(i * this.columnWidth, 0, i * this.columnWidth, -40));
            graphics.drawString(planner.getStages().get(i).getName(), ((i - 1) * this.columnWidth + 10), -15);
        }
    }

    public void drawLayout(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.WHITE);
        graphics.clearRect(0, 0, (int) this.canvas.getWidth(), (int) this.canvas.getHeight());
        graphics.translate(60, 0);

        // Add vertical line to divide time and shows
        graphics.draw(new Line2D.Double(0, 0, 0, this.canvas.getHeight()));

        // Draw vertical lines to divide stages
        for (int i = 0; i < this.planner.getStages().size(); i++) {
            graphics.draw(new Line2D.Double(i * this.columnWidth, 0, i * this.columnWidth, this.canvas.getHeight()));
        }

        // Draw all times to the layout
        for (int j = 1; j < 24; j++) {
            graphics.drawString(j + ":00", -50, (int) (j * this.canvas.getHeight() / 24));
        }
    }

    public void drawPlanning(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.translate(60, 0);

        //System.out.println(this.planner.getShows().size());

        for (Stage stage : this.planner.getStages()) {
            for (Show show : this.planner.getShows()) {
                if (show.getStage().equals(stage)) {
                    double timeDecimalBeginTime = show.getBeginTime().getHour() + (show.getBeginTime().getMinute() / 60.0);
                    double timeDecimalEndTime = show.getEndTime().getHour() + (show.getEndTime().getMinute() / 60.0);
                    graphics.draw(new RoundRectangle2D.Double(((this.planner.getStages().indexOf(stage)) * this.columnWidth) + 4, timeDecimalBeginTime * (this.canvas.getHeight() / 24), this.columnWidth - 8, (timeDecimalEndTime - timeDecimalBeginTime) * (this.canvas.getHeight() / 24.0), 25, 10));
                    String artists = "";
                    for (Artist artist : show.getArtists()) {
                        artists += artist.getName() + ", ";
                    }

                    artists = artists.substring(0, artists.length() - 2);
                    graphics.drawString(show.getBeginTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " - " + show.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")) + "\n" + artists, ((this.planner.getStages().indexOf(stage)) * this.columnWidth) + 10, (int) (timeDecimalBeginTime * (this.canvas.getHeight() / 24) + 20));
                    break;
                }
            }
        }
    }

    public Tab getVisualTab() {
        return visualTab;
    }
}