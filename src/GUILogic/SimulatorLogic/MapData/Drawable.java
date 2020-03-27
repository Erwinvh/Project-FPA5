package GUILogic.SimulatorLogic.MapData;

import org.jfree.fx.FXGraphics2D;

public interface Drawable {

    /**
     * The draw function that is shared
     * @param graphics the graphics that need to be drawn
     */
    void draw(FXGraphics2D graphics);
}