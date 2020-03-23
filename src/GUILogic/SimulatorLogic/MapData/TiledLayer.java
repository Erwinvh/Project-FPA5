package MapData;

import org.jfree.fx.FXGraphics2D;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * this is the object that holds the tiles for that layer
 */
public class TiledLayer implements Drawable {

    private ArrayList<TiledTile> tiles;

    /**
     * constructor
     * where
     *
     * @param mapImage        this object holds all the sprites and from here we get them for each place of the map
     * @param jsonObjectLayer in here the is the data stored what on this layer has to be printed
     */
    public TiledLayer(TiledMapImage mapImage, JsonObject jsonObjectLayer) {
        this.tiles = new ArrayList<>();

        //this array stores the values of each tile and that value represents what sprite has to ve used
        JsonArray gidArray = jsonObjectLayer.getJsonArray("data");

        //get the values now so we only have to do it once
        int mapWidth = MapDataController.getMapWidth();
        int mapHeight = MapDataController.getMapHeight();
        int tileSize = MapDataController.getTileSize();

        //loop trough every value
        for (int i = 0; i < gidArray.size(); i++) {
            //if the gid is 0 the sprite has to be empty
            if (gidArray.getInt(i) != 0) {

                //calculate the position where the sprite has to be drawn
                Point2D pos = new Point2D.Double((i % mapWidth) * tileSize, (i / mapHeight) * tileSize);

                //add the sprite to the tiles array so we know we have to draw it
                tiles.add(new TiledTile(mapImage.getTile(gidArray.getInt(i)), pos));
            }
        }
    }

    @Override
    public void draw(FXGraphics2D graphics) {
        for (TiledTile tile : tiles) {
            tile.draw(graphics);
        }
    }

    public void drawG(Graphics graphics){
        for (TiledTile tile : tiles){
            tile.drawG(graphics);
        }
    }

}