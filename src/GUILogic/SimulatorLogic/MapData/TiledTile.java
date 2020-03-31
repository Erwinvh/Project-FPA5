package GUILogic.SimulatorLogic.MapData;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * this object stores a position and the sprite that has to be drawn here
 */
class TiledTile {

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
    TiledTile(BufferedImage tileImage, Point2D position) {
        this.tileImage = tileImage;
        this.position = position;
    }

    /**
     * drawG
     * draws Tile on image with the given opacity.
     * @param graphics Graphics2D
     * @param opacity opacity
     */
    void drawG(Graphics2D graphics, double opacity) {
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)opacity));
        graphics.drawImage(tileImage, (int) position.getX(), (int) position.getY(), MapDataController.getTileSize(), MapDataController.getTileSize(), null);
    }
}