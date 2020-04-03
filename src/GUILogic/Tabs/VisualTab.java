package GUILogic.Tabs;

import GUILogic.DataController;
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
import java.time.format.DateTimeFormatter;

public class VisualTab {

    private final ScrollPane scrollPane;
    private Tab visualTab;
    private Canvas canvas;
    private Canvas canvasStages;
    private Planner planner;

    private int columnWidth = -1;

    private static final int CANVAS_WIDTH = 1280;
    private static final int CANVAS_HEIGHT = 2400;
    private static final int STAGE_HEIGHT = 40;
    private static final int TIME_COLUMN_WIDTH = 60;
    private static final Color BACKGROUND_COLOR = Color.decode("#d9e2ea");
    private static final Color SECONDARY_COLOR = Color.decode("#b5c2d2");

    /**
     * The constructor for the visual tab
     */
    public VisualTab() {
        this.planner = DataController.getInstance().getPlanner();

        this.visualTab = new Tab("Visual");
        this.canvas = new Canvas(CANVAS_WIDTH - 6, CANVAS_HEIGHT);
        this.canvasStages = new Canvas(CANVAS_WIDTH, STAGE_HEIGHT);

        scrollPane = new ScrollPane(this.canvas);
        scrollPane.setPrefSize(CANVAS_WIDTH, 680);
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVvalue(DataController.getInstance().getClock().getHours() / 24f);

        VBox vBox = new VBox(this.canvasStages, scrollPane);
        this.visualTab.setContent(vBox);

        drawStages(new FXGraphics2D(this.canvasStages.getGraphicsContext2D()));
        drawLayout(new FXGraphics2D(this.canvas.getGraphicsContext2D()));
        drawPlanning(new FXGraphics2D(this.canvas.getGraphicsContext2D()));
    }

    /**
     * This method draws the stages at their correct position at the top bar
     */
    private void drawStages(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(BACKGROUND_COLOR);
        graphics.clearRect(0, 0, (int) this.canvasStages.getWidth(), (int) this.canvasStages.getHeight());
        graphics.translate(TIME_COLUMN_WIDTH, STAGE_HEIGHT);

        // Add axis lines to stages createLayout
        graphics.draw(new Line2D.Double(-TIME_COLUMN_WIDTH, 0, this.canvasStages.getWidth() - TIME_COLUMN_WIDTH, 0));

        this.columnWidth = (int) ((this.canvas.getWidth() - TIME_COLUMN_WIDTH) / this.planner.getStages().size());

        // Add all divider lines to stages createLayout
        for (int i = 0; i < planner.getStages().size(); i++) {
            graphics.draw(new Line2D.Double(i * this.columnWidth + 1, 0, i * this.columnWidth + 1, -STAGE_HEIGHT));
            graphics.drawString(this.planner.getStages().get(i).getName() + "\n" + "(cap. " + this.planner.getStages().get(i).getCapacity() + ")", (i * this.columnWidth + 10), -STAGE_HEIGHT + 15);
        }
    }

    /**
     * This is the method that draws all the lines to better visually separate all shows and stages
     */
    private void drawLayout(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(BACKGROUND_COLOR);
        graphics.clearRect(0, 0, (int) this.canvas.getWidth(), (int) this.canvas.getHeight());
        graphics.translate(TIME_COLUMN_WIDTH, 0);

        // Add vertical line to divide time and shows
        graphics.draw(new Line2D.Double(0, 0, 0, this.canvas.getHeight()));

        // Draw vertical lines to divide stages
        for (int i = 0; i < this.planner.getStages().size(); i++) {
            graphics.draw(new Line2D.Double(i * this.columnWidth, 0, i * this.columnWidth, this.canvas.getHeight()));
        }

        // Draw all times to the createLayout
        for (int j = 1; j < 24; j++) {
            graphics.drawString(j + ":00", -50, (int) (j * this.canvas.getHeight() / 24));
            graphics.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, new float[]{25, 25}, 40));
            graphics.draw(new Line2D.Double(0, this.canvas.getHeight() / 24 * j, this.canvas.getWidth() - TIME_COLUMN_WIDTH, this.canvas.getHeight() / 24 * j));
        }
        graphics.setStroke(new BasicStroke(1f));
    }

    /**
     * This method draws all shows under the correct stage, it also fills in the information of each show
     */
    private void drawPlanning(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.translate(TIME_COLUMN_WIDTH, 0);

        for (Stage stage : this.planner.getStages()) {
            for (Show show : this.planner.getShows()) {
                if (show.getStage().getName().equals(stage.getName()) && show.getStage().getCapacity() == stage.getCapacity()) {
                    double timeDecimalBeginTime = show.getBeginTime().getHour() + (show.getBeginTime().getMinute() / 60.0);
                    double timeDecimalEndTime = show.getEndTime().getHour() + (show.getEndTime().getMinute() / 60.0);

                    // Draw the box around the show
                    Shape rectangle = new RoundRectangle2D.Double(((this.planner.getStages().indexOf(stage)) * this.columnWidth) + 5, timeDecimalBeginTime * (this.canvas.getHeight() / 24.0), this.columnWidth - 10, (timeDecimalEndTime - timeDecimalBeginTime) * (this.canvas.getHeight() / 24.0), 25, 10);
                    graphics.draw(rectangle);
                    graphics.setColor(SECONDARY_COLOR);
                    graphics.fill(rectangle);
                    graphics.setColor(Color.BLACK);
                    String artists = "";
                    for (Artist artist : show.getArtists()) {
                        artists += artist.getName() + ", ";
                    }

                    artists = artists.substring(0, artists.length() - 2);

                    String genres = "";
                    if (show.getGenre() != null) {
                        genres = show.getGenre().getFancyName();
                    } else {
                        genres += "No specified genre";
                    }

                    // Draw the info of the show
                    graphics.drawString(show.getBeginTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " - " + show.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")) + "\n" + artists + " " + "||"+ genres, ((this.planner.getStages().indexOf(stage)) * this.columnWidth) + 10, (int) (timeDecimalBeginTime * (this.canvas.getHeight() / 24) + 20));
                }
            }
        }
    }

    /**
     * The getter of the visual tab
     * @return The visual tab
     */
    public Tab getVisualTab() {
        return visualTab;
    }

    /**
     * The update function that updates the visual tab.
     */
    public void update() {
        scrollPane.setVvalue(DataController.getInstance().getClock().getHours() / 24f);
        drawStages(new FXGraphics2D(this.canvasStages.getGraphicsContext2D()));
        drawLayout(new FXGraphics2D(this.canvas.getGraphicsContext2D()));
        drawPlanning(new FXGraphics2D(this.canvas.getGraphicsContext2D()));
    }
}