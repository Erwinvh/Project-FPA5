package GUILogic;

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

    public VisualTab(){
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
        stages.addAll(Arrays.asList("Rik", "Arne", "Lars", "Veel te lange naam"));
        this.amountOfColumns = stages.size();

        graphics.draw(new Line2D.Double(0, 50, 960, 50));

        for (int i = 0; i < this.amountOfColumns; i++) {
            graphics.draw(new Line2D.Double((960/amountOfColumns) * (1 + i),0 , (960/amountOfColumns) * (1 + i), 540));
            graphics.drawString(stages.get(i), (int)((960/amountOfColumns) * (i + 0.1)), 30);
        }
    }
}
