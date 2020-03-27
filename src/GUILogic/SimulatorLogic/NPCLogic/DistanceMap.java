package GUILogic.SimulatorLogic.NPCLogic;

import GUILogic.SimulatorLogic.MapData.TargetArea;
import GUILogic.SimulatorLogic.MapData.WalkableMap;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.Queue;

public class DistanceMap {
    private String mapName;
    private int[][] map;
    private Queue<Point2D> queue;
    private boolean[][] visited;
    private boolean[][] walkableMap;
    private TargetArea target;

    private final int size = 100;

    public TargetArea getTarget() {
        return target;
    }

    public boolean[][] getWalkableMap() {
        return walkableMap;
    }

    /**
     * Makes a distanceMap which can be used to find the shortest path
     *
     * @param mapName     The name of the map
     * @param targetArea  The point from which the DistanceMap starts (the 0 point)
     * @param walkableMap A map used to know if the surface is walkable for the NPC's
     */
    public DistanceMap(String mapName, TargetArea targetArea, WalkableMap walkableMap) {
        this.mapName = mapName;

        int mapWidth = walkableMap.getMap().length;
        int mapHeight = walkableMap.getMap()[0].length;

        this.map = new int[mapWidth][mapHeight];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.map[i][j] = size * size;
            }
        }

        this.visited = new boolean[mapWidth][mapHeight];
        this.walkableMap = walkableMap.getMap();
        this.target = targetArea;

        this.queue = new LinkedList<>();
        this.queue.offer(targetArea.getMiddlePoint());
        this.map[(int) targetArea.getMiddlePoint().getX()][(int) targetArea.getMiddlePoint().getY()] = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.visited[i][j] = false;
            }
        }

        this.visited[(int) targetArea.getMiddlePoint().getX()][(int) targetArea.getMiddlePoint().getY()] = true;

        /**
         * The Breadth-First Search Algorithm
         * Loops trough every accessible point in the map and gives it the corresponding distance value
         */
        // Whilst there is still something to look through
        while (!this.queue.isEmpty()) {
            // Retrieve the next center point to look around from
            Point2D currentPoint = this.queue.poll();

            int currentX = (int) currentPoint.getX();
            int currentY = (int) currentPoint.getY();

            // Two for loops to check around the center point
            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                for (int xOffset = -1; xOffset <= 1; xOffset++) {
                    // Do not check the diagonals
                    if (!(xOffset == 0 || yOffset == 0))
                        continue;

                    int checkingX = currentX + xOffset;
                    int checkingY = currentY + yOffset;

                    // If it is within bounds
                    if (checkingX > -1 && checkingX < mapWidth && checkingY > -1 && checkingY < mapHeight) {

                        // If the spot to check hasn't been visited before and it is walkable as well
                        if (!this.visited[checkingX][checkingY] && this.walkableMap[checkingX][checkingY]) {
                            this.map[checkingX][checkingY] = this.map[currentX][currentY] + 1;
                            this.queue.offer(new Point2D.Double(checkingX, checkingY));
                            this.visited[checkingX][checkingY] = true;
                        }
                    }
                }
            }
        }
    }


    /**
     * The getter for the map in the form of ...
     * @return The map as ...
     */
    public int[][] getMap() {
        return map;
    }

    /**
     * The getter for the map name
     * @return The map name
     */
    public String getMapName() {
        return mapName;
    }
}