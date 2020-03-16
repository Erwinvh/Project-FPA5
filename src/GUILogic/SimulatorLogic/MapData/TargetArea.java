package MapData;

import java.awt.geom.Point2D;

public class TargetArea {
    private String name;

    public enum TargetAreaType {ALL, VISITOR, ARTIST}

    private TargetAreaType targetAreaType;

    private Point2D position;
    private Point2D size;

    public TargetArea(String name, TargetAreaType targetAreaType, Point2D position, Point2D size) {
        this.name = name;
        this.targetAreaType = targetAreaType;
        this.position = position;
        this.size = size;
    }

    public Point2D getMiddlePoint() {
        double xPos = (position.getX() + size.getX() * 0.5) / 32;
        double yPos = (position.getY() + size.getY() * 0.5) / 32;
        return new Point2D.Double(xPos, yPos);
    }

    public String getName() {
        return name;
    }

    public TargetAreaType getTargetAreaType() {
        return targetAreaType;
    }

    public Point2D getPosition() {
        return position;
    }

    public Point2D getSize() {
        return size;
    }
}