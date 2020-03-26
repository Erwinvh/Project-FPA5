package GUILogic.SimulatorLogic.NPCLogic;

import GUILogic.SimulatorLogic.MapData.MapDataController;

import java.awt.geom.Point2D;

public class PathCalculator {

    /**
     * Calculates the next target for a NPCLogic.Person, allows movement in 8 directions
     *
     * @param currPos     the current position of the character
     * @param distanceMap map of all distances relative to target position
     * @return the next position the character moves to
     */
    public static Point2D nextPositionToTarget(Point2D currPos, DistanceMap distanceMap) {
        double tileSize = MapDataController.getTileSize();

        int xIndex = (int) Math.floor(currPos.getX() / tileSize);
        int yindex = (int) Math.floor(currPos.getY() / tileSize);

        Point2D middlePointCoords = new Point2D.Double(distanceMap.getTarget().getMiddlePoint().getX() * tileSize, distanceMap.getTarget().getMiddlePoint().getY() * tileSize);

        if (middlePointCoords.distance(currPos) <= tileSize) {
            return new Point2D.Double(-100, -100);
        }

        int lowest = Integer.MAX_VALUE;
        int lowestIndexX = Integer.MAX_VALUE;
        int lowestIndexY = Integer.MAX_VALUE;

        for (int yOffset = -1; yOffset <= 1; yOffset++) {
            for (int xOffset = -1; xOffset <= 1; xOffset++) {
                int currX = xIndex + xOffset;
                int currY = yindex + yOffset;
                if (currX > -1 && currX < 99 && currY > -1 && currY < 99) {
                    int value = distanceMap.getMap()[currX][currY];
                    if (value < lowest) {
                        lowest = value;
                        lowestIndexX = currX;
                        lowestIndexY = currY;
                    }
                }
            }
        }

        return new Point2D.Double(lowestIndexX * tileSize + tileSize * 0.5, lowestIndexY * tileSize + tileSize * 0.5);
    }

    /**
     * Finds a random tile that is walkable and closest to a point
     *
     * @param currentPosition the position the character is currently on
     * @param distanceMap     the DistanceMap it is walking on
     * @return the available tile, if not found return the currentPosition
     */
    public static Point2D findRandomClosestWalkable(Point2D currentPosition, DistanceMap distanceMap) {
        int tileSize = MapDataController.getTileSize();

        int failedAttempts = 0;
        while (failedAttempts < 8) {
            int xPos = (int) Math.floor(Math.random() * 2.9) - 1 + ((int) currentPosition.getX()) / tileSize;
            int yPos = (int) Math.floor(Math.random() * 2.9) - 1 + ((int) currentPosition.getY()) / tileSize;
            xPos = Math.min(xPos, 99);
            xPos = Math.max(0, xPos);
            yPos = Math.min(yPos, 99);
            yPos = Math.max(0, yPos);
            boolean[][] walkableMap = distanceMap.getWalkableMap();
            if (walkableMap[xPos][yPos]) {
                return new Point2D.Double(xPos * tileSize, yPos * tileSize);
            }
            failedAttempts++;
        }

        return currentPosition;
    }
}