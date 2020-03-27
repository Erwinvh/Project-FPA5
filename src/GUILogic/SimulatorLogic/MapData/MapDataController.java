package GUILogic.SimulatorLogic.MapData;

import GUILogic.SimulatorLogic.NPCLogic.DistanceMap;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * this object is the mother object of the map
 * all the tiled layers are stored here
 */
public class MapDataController {
    //static finals for where you can find the sprites and the layout json file
    private final static String SPRITESHEETS_DIR = "Resources/spritesheets/";
    private final static String MAP_LAYOUT_DIR = "Resources/festmap.json";

    //values for the dimensions of the map
    private static int MAP_WIDTH;
    private static int MAP_HEIGHT;
    private static int TILE_SIZE;

    private static BufferedImage dayImage;
    private static BufferedImage nightImage;

    private static WalkableMap walkableMap;
    private static TargetArea[] targetAreas;
    private static DistanceMap[] distanceMaps;

    /**
     * constructor
     * first the TiledMapImage is created
     * then the json file is read out and for each layer a new object is created
     */
    public MapDataController() {
        //arraylist where the layers are stored
        ArrayList<TiledLayer> tiledLayers = new ArrayList<>();
        ArrayList<TiledLayer> tiledLayersNight = new ArrayList<>();

        //create the tiledMapImage object so we can add to it the sprites later
        TiledMapImage tiledMapImage = new TiledMapImage();

        try {
            //read out the map layout file to an json object
            JsonReader jsonReader = Json.createReader(new FileInputStream(new File(MAP_LAYOUT_DIR)));
            JsonObject baseJsonObject = jsonReader.readObject();
            jsonReader.close();

            //get from the json object the dimensions and set the attributes
            MAP_WIDTH = baseJsonObject.getInt("width");
            MAP_HEIGHT = baseJsonObject.getInt("height");
            TILE_SIZE = baseJsonObject.getInt("tilewidth");

            //read the jsonObject out so we get two array lists containing the data for the layers and tilesets
            JsonArray layersJsonArray = baseJsonObject.getJsonArray("layers");
            JsonArray tilesetsJsonArray = baseJsonObject.getJsonArray("tilesets");

            //for each tileset in the tileset array we add the sprites to the tiledMapImage object so we get access to every sprite by their corresponding gid
            for (JsonObject tileset : tilesetsJsonArray.getValuesAs(JsonObject.class)) {
                tiledMapImage.addSpriteSheet(tileset.getString("image"), tileset.getInt("firstgid"),
                        tileset.getInt("imagewidth") / TILE_SIZE, tileset.getInt("imageheight") / TILE_SIZE);
            }

            for (JsonObject layerJsonObject : layersJsonArray.getValuesAs(JsonObject.class)) {
                if (layerJsonObject.getJsonString("name").toString().equals("Walkable")) {
                    populateWalkableMap(layerJsonObject);

                } else if (layerJsonObject.getJsonString("type").toString().equals("objectgroup")) {
                    populateTargetAreas(layerJsonObject);

                } else if (layerJsonObject.getBoolean("visible"))
                    //adds darkness and light layer
                if (layerJsonObject.getInt("id") == 7 || layerJsonObject.getInt("id") == 8){
                        tiledLayersNight.add(new TiledLayer(tiledMapImage, layerJsonObject));
                    } else {
                        tiledLayers.add(new TiledLayer(tiledMapImage, layerJsonObject));
                    }

            }

        } catch (FileNotFoundException e) {
            System.out.println("MapDataController.MapDataController: could not find file in " + MAP_LAYOUT_DIR);
            //e.printStackTrace();
        }

        initializeDistanceMaps();

        Graphics graphics;

        dayImage = new BufferedImage(MAP_WIDTH * TILE_SIZE, MAP_HEIGHT * TILE_SIZE, BufferedImage.TYPE_INT_RGB);
        graphics = dayImage.getGraphics();
        for (TiledLayer tiledLayer : tiledLayers) {
            tiledLayer.drawG(graphics);
        }

        nightImage = new BufferedImage(MAP_WIDTH * TILE_SIZE, MAP_HEIGHT * TILE_SIZE, BufferedImage.TYPE_INT_RGB);
        graphics = nightImage.getGraphics();
        for (TiledLayer tiledLayer : tiledLayers){
            tiledLayer.drawG(graphics);
        }
        for (TiledLayer tiledLayer : tiledLayersNight){
            tiledLayer.drawG(graphics);
        }
    }

    /**
     * The getter for array list of distance maps
     * @return The list of distance maps
     */
    public static DistanceMap[] getDistanceMaps() {
        return distanceMaps;
    }

    /**
     * This function sets the value of WalkableMap at index i,j to true when at that position the id 214 is found
     *
     * @param walkableJsonObject
     */
    private void populateWalkableMap(JsonObject walkableJsonObject) {
        JsonArray dataArray = walkableJsonObject.getJsonArray("data");
        boolean[][] walkableArray = new boolean[MAP_WIDTH][MAP_HEIGHT];

        for (int i = 0; i < dataArray.size(); i++) {
            boolean isWalkable = false;
            if (dataArray.getInt(i) == 214) {
                isWalkable = true;
            }

            walkableArray[i % MAP_WIDTH][i / MAP_HEIGHT] = isWalkable;
        }

        walkableMap = new WalkableMap(walkableArray);
    }

    /**
     * Determines the type of Target area depending visitors, artists or all
     * @param objectsJsonObject
     */
    private void populateTargetAreas(JsonObject objectsJsonObject) {
        JsonArray targetsJsonArray = objectsJsonObject.getJsonArray("objects");
        targetAreas = new TargetArea[targetsJsonArray.size()];

        for (int i = 0; i < targetsJsonArray.size(); i++) {
            JsonObject targetArea = targetsJsonArray.getJsonObject(i);

            String name = targetArea.getString("name");

            TargetArea.TargetAreaType targetAreaType = TargetArea.TargetAreaType.ALL;
            switch (targetArea.getString("type")) {
                case "Visitor":
                    targetAreaType = TargetArea.TargetAreaType.VISITOR;
                    break;
                case "Artist":
                    targetAreaType = TargetArea.TargetAreaType.ARTIST;
                    break;
            }

            Point2D pos = new Point2D.Double(targetArea.getInt("x"), targetArea.getInt("y"));
            Point2D size = new Point2D.Double(targetArea.getInt("width"), targetArea.getInt("height"));

            targetAreas[i] = new TargetArea(name, targetAreaType, pos, size);
        }
    }

    /**
     * ???
     */
    private void initializeDistanceMaps() {
        distanceMaps = new DistanceMap[targetAreas.length];
        for (int i = 0; i < targetAreas.length; i++) {
            distanceMaps[i] = new DistanceMap(targetAreas[i].getName(), targetAreas[i], walkableMap);
        }
    }

    /**
     * The getter for the distance map as an object searched for by name
     * @param name The name of the searched distance map
     * @return the requested distance map, else null
     */
    public static DistanceMap getDistanceMap(String name) {
        for (DistanceMap distanceMap : distanceMaps) {
            if (distanceMap.getMapName().equals(name)) {
                return distanceMap;
            }
        }
        return null;
    }

    /**
     * The getter for the distance map as an object searched for by the target area.
     * @param targetArea The target area of the searched distance map
     * @return The distance map that was requested, else null
     */
    public static DistanceMap getDistanceMap(TargetArea targetArea) {
        for (DistanceMap distanceMap : distanceMaps) {
            if (distanceMap.getTarget().equals(targetArea)) {
                return distanceMap;
            }
        }

        return null;
    }

    /**
     * The draw function of the graphics of the map???
     * @param graphics
     */
    public void draw(Graphics2D graphics, boolean isDay) {
        if (isDay) {
            graphics.drawImage(dayImage, 0, 0, MAP_WIDTH * TILE_SIZE, MAP_HEIGHT * TILE_SIZE, null);
        } else {
            graphics.drawImage(nightImage, 0, 0, MAP_WIDTH * TILE_SIZE, MAP_HEIGHT * TILE_SIZE, null);
        }
    }

    /**
     * The getter for the walkable map
     * @return the walkable map
     */
    public WalkableMap getWalkableMap() {
        return walkableMap;
    }

    /**
     * The getter for the list of target areas
     * @return the list of target areas
     */
    public static TargetArea[] getTargetAreas() {
        return targetAreas;
    }

    /**
     * The getter for the map width
     * @return the map width
     */
    public static int getMapWidth() {
        return MAP_WIDTH;
    }

    /**
     * The getter for the map height
     * @return the map height
     */
    public static int getMapHeight() {
        return MAP_HEIGHT;
    }

    /**
     * The getter for the sprite sheet directory
     * @return the sprite sheet directory
     */
    public static String getSpritesheetsDir() {
        return SPRITESHEETS_DIR;
    }

    /**
     * The getter of the tile size
     * @return the tile size
     */
    public static int getTileSize() {
        return TILE_SIZE;
    }

    /**
     * The getter for the map image
     * @return the map image
     */
    public static BufferedImage getDayImage() {
        return dayImage;
    }
}