package GUILogic.SimulatorLogic.MapData;

import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * this object stores a position and the sprite that has to be drawn here
 */
public class TiledTile implements Drawable {

    //sprite that needs to be draw
    private BufferedImage tileImage;
    //position where it needs to be drawn
    private Point2D position;

    /**
     * simple constructor that sets the attributes
     *
     * @param tileImage sprite that needs to be drawn
     * @param position  Point2d with an x and y coordinate
     */
    public TiledTile(BufferedImage tileImage, Point2D position) {
        this.tileImage = tileImage;
        this.position = position;
    }

    /**
     * ???
     * @param graphics the graphics that need to be drawn
     */
    @Override
    public void draw(FXGraphics2D graphics) {
        graphics.drawImage(tileImage, (int) position.getX(), (int) position.getY(), MapDataController.getTileSize(), MapDataController.getTileSize(), null);
    }

    /**
     * drawG
     * draws Tile on image with the given opacity.
     * @param graphics Graphics2D
     * @param opacity opacity
     */
    public void drawG(Graphics2D graphics, double opacity) {
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)opacity));
        graphics.drawImage(tileImage, (int) position.getX(), (int) position.getY(), MapDataController.getTileSize(), MapDataController.getTileSize(), null);
    }
}