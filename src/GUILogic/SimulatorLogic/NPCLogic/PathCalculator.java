package NPCLogic;

import java.awt.geom.Point2D;

public class PathCalculator {

    /**
     * Calculates the next target for a NPCLogic.Person, allows movement in 8 directions
     *
     * @param currPos the current position of the character
     * @param distanceMap map of all distances relative to target position
     * @return the next position the character moves to
     */
    public static Point2D nextPositionToTarget(Point2D currPos, DistanceMap distanceMap) {
        // TODO: Not hardcode this 32 tilesize and retrieve from tile logic
        int Xindex = (int) Math.floor(currPos.getX() / 32.0);
        int Yindex = (int) Math.floor(currPos.getY() / 32.0);

        Point2D middlePointCoords = new Point2D.Double(distanceMap.getTarget().getMiddlePoint().getX() * 32, distanceMap.getTarget().getMiddlePoint().getY() * 32);

        if (middlePointCoords.distance(currPos) <= 32) {
            return new Point2D.Double(-1, -1);
        }

        int lowest = Integer.MAX_VALUE;
        int lowestIndexX = Integer.MAX_VALUE;
        int lowestIndexY = Integer.MAX_VALUE;

        for (int yOffset = -1; yOffset <= 1; yOffset++) {
            for (int xOffset = -1; xOffset <= 1; xOffset++) {
                int currX = Xindex + xOffset;
                int currY = Yindex + yOffset;
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

        return new Point2D.Double(lowestIndexX * 32 + 16, lowestIndexY * 32 + 16);
    }

    /**
     * Finds a random tile that is walkable and closest to a point
     *
     * @param currentPosition the position the character is currently on
     * @param distanceMap the DistanceMap it is walking on
     * @return the available tile, if not found return the currentPosition
     */
    public static Point2D findRandomClosestWalkable(Point2D currentPosition, DistanceMap distanceMap) {
        int failedAttempts = 0;
        while (failedAttempts < 8) {
            int xPos = (int) Math.floor(Math.random() * 2.9) - 1 + ((int) currentPosition.getX()) / 32;
            int yPos = (int) Math.floor(Math.random() * 2.9) - 1 + ((int) currentPosition.getY()) / 32;
            xPos = Math.min(xPos, 99);
            xPos = Math.max(0, xPos);
            yPos = Math.min(yPos, 99);
            yPos = Math.max(0, yPos);
            boolean[][] walkableMap = distanceMap.getWalkableMap();
            if (walkableMap[xPos][yPos]) {
                return new Point2D.Double(xPos * 32, yPos * 32);
            }
            failedAttempts++;
        }

        return currentPosition;
    }

    public static boolean isWalkable(Point2D currentPosition) {
        NPCLogic.DistanceMap[] distanceMaps = null;//Main.getDistanceMaps();
        boolean[][] walkableMap = distanceMaps[1].getWalkableMap();
        int xPos = (int) currentPosition.getX() / 32;
        int yPos = (int) currentPosition.getY() / 32;
        xPos = Math.min(xPos, 99);
        xPos = Math.max(0, xPos);
        yPos = Math.min(yPos, 99);
        yPos = Math.max(0, yPos);
        if (walkableMap[xPos][yPos]) {
            return true;
        } else return false;
    }
}