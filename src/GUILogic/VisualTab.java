package GUILogic;

import PlannerData.Planner;
import PlannerData.Show;
import PlannerData.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Tab;
import org.jfree.fx.FXGraphics2D;

import java.awt.geom.Line2D;
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
        this.endTime = 24;
        this.visualTab = new Tab("Visual");
        this.canvas = new Canvas(960, 540);
        this.visualTab.setContent(this.canvas);
        drawLayout(new FXGraphics2D(canvas.getGraphicsContext2D()));

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
            graphics.drawString(stages.get(i).getName(), (int)((900/amountOfColumns) * (i + 0.1) + 60), 30);
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
