package GUILogic;

import Enumerators.Genres;
import PlannerData.Artist;
import PlannerData.Planner;
import PlannerData.Show;
import PlannerData.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import org.jfree.fx.FXGraphics2D;

import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;


public class VisualTab {
    private Tab visualTab;
    private Canvas canvas;
    private int amountOfColumns;
    private int columnWith;
    private int startTime;
    private int endTime;
    private Planner planner;

    public VisualTab(){
        this.planner = new Planner();
        this.startTime = 17;
         
        this.visualTab = new Tab("Visual");
        this.canvas = new Canvas(960, 1240);

        ScrollPane scrollPane = new ScrollPane(this.canvas);
        scrollPane.setPrefSize(960, 540);
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
//        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);

        this.visualTab.setContent(scrollPane);
        drawLayout2(new FXGraphics2D(this.canvas.getGraphicsContext2D()));
    }

    public void drawLayout2(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.translate(60, 40);

        graphics.draw(new Line2D.Double(-60, 0, this.canvas.getWidth()-60, 0));
        graphics.draw(new Line2D.Double(0, -40, 0, this.canvas.getHeight()-40));

        this.planner.getStages().add(new Stage(1, "Test"));
        this.planner.getStages().add(new Stage(1, "Barry"));
        this.planner.getStages().add(new Stage(1, "I am Bored"));
        this.planner.getStages().add(new Stage(1, "Pieter"));

        int i = 0;
        for (Stage stage : this.planner.getStages()) {
            i++;
            graphics.draw(new Line2D.Double(i * ((this.canvas.getWidth() - 60) / this.planner.getStages().size()), -40, i * ((this.canvas.getWidth() - 60) / this.planner.getStages().size()), this.canvas.getHeight()-40));
            graphics.drawString(stage.getName(), (int) ((i - 1) * ((this.canvas.getWidth() - 60) / this.planner.getStages().size()) + 10), -15);
        }

        for (int j = 1; j < 25; j++) {
            graphics.drawString(j + ":00", -50, (int) ((j - 0.5) * 50));
        }
    }

    public void drawPlanning2(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.translate(60, 40);
    }

    public Tab getVisualTab(){
        return visualTab;
    }

       public void drawLayout(FXGraphics2D graphics) {
        ArrayList<String> stages = new ArrayList<>(); // needs to be getter
        stages.addAll(Arrays.asList("Rik", "Arne", "Lars", "Veel te lange naam", "Henk", "Peter"));
        this.amountOfColumns = stages.size();

        graphics.draw(new Line2D.Double(0, 50, 960, 50));
        graphics.draw(new Line2D.Double(60, 0, 60, 540));

        for (int i = 0; i < this.amountOfColumns; i++) {
            graphics.draw(new Line2D.Double((900/amountOfColumns) * (1 + i) + 60,0 , (900/amountOfColumns) * (1 + i) + 60, 540));
//            graphics.drawString(stages.get(i).getName(), (int)((900/amountOfColumns) * (i + 0.1) + 60), 30);
            //dit werkt niet^
        }


        for (int i = this.startTime; i <= this.endTime; i++){
            graphics.drawString((i) + ":00", 5, (int) ((i - this.startTime + 0.5) * (490/(this.endTime - this.startTime + 2)) + 50));
        }
    }

    public void drawPlanning(FXGraphics2D graphics) {
        int amountOfHours = this.endTime - this.startTime;
        for (Show show : this.planner.getShows()) {
            double timeDecimal = show.getBeginTime().minusHours(this.startTime).getHour() + (show.getBeginTime().getMinute()/60);
            int beginY = (int) (490/amountOfHours * timeDecimal/amountOfHours);

            timeDecimal = show.getEndTime().minusHours(this.startTime).getHour() + (show.getEndTime().getMinute()/60);
            int endY = (int) (490/amountOfHours * timeDecimal/amountOfHours);

            int counter = 0;
            for (Stage stage : this.planner.getStages()) {
                if (stage == show.getStage()) {
                    break;
                }
                counter++;
            }

            int beginX = counter * this.columnWith + 62;

            graphics.drawRect(beginX, beginY, this.columnWith - 4, endY - beginY);
        }
    }
}
