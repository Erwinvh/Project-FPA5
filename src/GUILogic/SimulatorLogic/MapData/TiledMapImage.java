package MapData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * this object is where all the sprites are stored by their corresponding gid
 */
public class TiledMapImage {
    //hash map to save the bufferedImages by their gid
    private HashMap<Integer, BufferedImage> tileImages;

    /**
     * simple constructor only initiating the tileImages HashMap
     */
    public TiledMapImage() {
        this.tileImages = new HashMap<>();
    }

    /**
     * this function gets the tile by inputting the gid
     * @param gid global id for the sprite
     */
    public BufferedImage getTile(int gid) {
        return tileImages.get(gid);
    }

    /**
     * this method cuts the given spritesheet in sprites and gives the sprite a global id
     *
     * @param spritesheetName file name where the sprites are stored
     * @param startingGID the first sprite is this global id and from here on we can just add 1 per sprite
     * @param mapWidth how many sprites are beside each other
     * @param mapHeight how many sprites are under each other
     */
    public void addSpriteSheet(String spritesheetName, int startingGID, int mapWidth, int mapHeight) {
        try {
            int tileSize = MapDataController.getTileSize();
            int counter = startingGID;
            BufferedImage image = ImageIO.read(new File(MapDataController.getSpritesheetsDir() + spritesheetName));

            for (int y = 0; y < mapHeight; y++) {
                for (int x = 0; x < mapWidth; x++) {
                    this.tileImages.put(counter, image.getSubimage(x * tileSize, y * tileSize, tileSize, tileSize));
                    counter++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}