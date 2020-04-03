package GUILogic.SimulatorLogic.MapData;

import java.awt.geom.Point2D;

/**
 * This object saves the tiles of a target on the Map
 */
public class TargetArea {
    private String name;

    public enum TargetAreaType {ALL, VISITOR, ARTIST}

    private TargetAreaType targetAreaType;

    private Point2D position;
    private Point2D size;

    /**
     * The constructor of the target area
     * @param name the name of the target area
     * @param targetAreaType the type of target area
     * @param position the position of the target area
     * @param size the size of the target area
     */
    public TargetArea(String name, TargetAreaType targetAreaType, Point2D position, Point2D size) {
        this.name = name;
        this.targetAreaType = targetAreaType;
        this.position = position;
        this.size = size;
    }

    /**
     * The getter for the middle point of the target area
     * @return the middle point
     */
    public Point2D getMiddlePoint() {
        double xPos = (position.getX() + size.getX() * 0.5) / 32;
        double yPos = (position.getY() + size.getY() * 0.5) / 32;
        return new Point2D.Double(xPos, yPos);
    }

    /**
     * The getter for the name of the target area
     * @return the target area name
     */
    public String getName() {
        return name;
    }

    /**
     * The getter for the position of the target area
     * @return the target area position
     */
    public Point2D getPosition() {
        return position;
    }

    /**
     * The getter for the size of the target area
     * @return the target area size
     */
    public Point2D getSize() {
        return size;
    }

    /**
     * The setter for the name of the target area
     */
    public void setName(String name) {
        this.name = name;
    }
}