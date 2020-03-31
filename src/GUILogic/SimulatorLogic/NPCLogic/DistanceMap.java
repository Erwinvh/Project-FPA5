package GUILogic.SimulatorLogic.NPCLogic;

import GUILogic.SimulatorLogic.MapData.MapDataController;
import GUILogic.SimulatorLogic.MapData.TargetArea;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.Queue;

public class DistanceMap {
    private String mapName;
    private int[][] map;
    private TargetArea target;

    /**
     * Makes a distanceMap which can be used to find the shortest path
     *
     * @param mapName    The name of the map
     * @param targetArea The point from which the DistanceMap starts (the 0 point)
     */
    public DistanceMap(String mapName, TargetArea targetArea) {
        this.mapName = mapName;

        boolean[][] walkableMap = MapDataController.getInstance().getWalkableMap();

        int mapWidth = walkableMap.length;
        int mapHeight = walkableMap[0].length;

        int total = mapWidth * mapHeight;
        this.map = new int[mapWidth][mapHeight];
        boolean[][] visited = new boolean[mapWidth][mapHeight];
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                this.map[i][j] = total;
                visited[i][j] = false;
            }
        }

        this.target = targetArea;

        Queue<Point2D> queue = new LinkedList<>();
        queue.offer(targetArea.getMiddlePoint());
        this.map[(int) targetArea.getMiddlePoint().getX()][(int) targetArea.getMiddlePoint().getY()] = 0;
        visited[(int) targetArea.getMiddlePoint().getX()][(int) targetArea.getMiddlePoint().getY()] = true;

        /*
         * The Breadth-First Search Algorithm
         * Loops trough every accessible point in the map and gives it the corresponding distance value
         */
        // Whilst there is still something to look through
        while (!queue.isEmpty()) {
            // Retrieve the next center point to look around from
            Point2D currentPoint = queue.poll();

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
                        if (!visited[checkingX][checkingY] && walkableMap[checkingX][checkingY]) {
                            this.map[checkingX][checkingY] = this.map[currentX][currentY] + 1;
                            queue.offer(new Point2D.Double(checkingX, checkingY));
                            visited[checkingX][checkingY] = true;
                        }
                    }
                }
            }
        }
    }

    /**
     * @return The map as a 2D int array, each index containing the distance from the target in this DistanceMap
     */
    public int[][] getMap() {
        return map;
    }

    /**
     * The getter for the map name
     *
     * @return The map name
     */
    public String getMapName() {
        return mapName;
    }

    public TargetArea getTarget() {
        return target;
    }
}