package MapData;

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
     *  simple constructor that sets the attributes
     *
     * @param tileImage sprite that needs to be drawn
     * @param position Point2d with an x and y coordinate
     */
    public TiledTile(BufferedImage tileImage, Point2D position) {
        this.tileImage = tileImage;
        this.position = position;
    }

    @Override
    public void draw(FXGraphics2D graphics) {
        graphics.drawImage(tileImage, (int) position.getX(), (int) position.getY(),  MapDataController.getTileSize(),  MapDataController.getTileSize(), null);
    }

    public void drawG(Graphics graphics){
        graphics.drawImage(tileImage, (int) position.getX(), (int) position.getY(),  MapDataController.getTileSize(),  MapDataController.getTileSize(), null);
    }
}