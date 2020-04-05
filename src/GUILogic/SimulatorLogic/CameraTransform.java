package GUILogic.SimulatorLogic;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

class CameraTransform {
    private Point2D centerPoint;
    private Point2D lastMousePos;

    private double currZoom;
    private static final double MIN_ZOOM = 0.2;
    private static final double MAX_ZOOM = 2;

    /**
     * Sets initial currZoom, center point, inverseTransform and lastMousePos.
     * <p>
     * Zoom:               Scaling modified by scrolling.
     * Center point:        The replacement done by dragging with right mouse button. Scaling is included in calculations.
     * inverseTransform:   The AffineTransform that is need to calculate back to initial canvas conditions.
     * Used for f.e. clearRect.
     * lastMousePos        The previous mouse position when moved, pressed or scrolling the mouse.
     * Used to calculate the distance dragged.
     */
    CameraTransform(Canvas node) {
        this.currZoom = 1.0;
        this.centerPoint = new Point2D.Double(0, 0);
        this.lastMousePos = new Point2D.Double(0, 0);

        node.setOnScroll(this::onScroll);
        node.setOnMouseDragged(this::mouseDragged);
        node.setOnMousePressed(e -> {
            centerPoint = new Point2D.Double(
                    centerPoint.getX() + (lastMousePos.getX() - e.getX()) / currZoom,
                    centerPoint.getY() + (lastMousePos.getY() - e.getY()) / currZoom
            );
            lastMousePos = new Point2D.Double(e.getX(), e.getY());
        });
    }

    /**
     * This method allows the user to scroll in and out of the map.
     *
     * @param event the scroll event itself
     */
    private void onScroll(ScrollEvent event) {
        Point2D preZoom = getInverseTransform().transform(new Point2D.Double(event.getX(), event.getY()), null);

        currZoom *= (1 + event.getDeltaY() / 150.0f);

        if (currZoom > MAX_ZOOM) currZoom = MAX_ZOOM;
        else if (currZoom < MIN_ZOOM) currZoom = MIN_ZOOM;

        Point2D postZoom = getInverseTransform().transform(new Point2D.Double(event.getX(), event.getY()), null);

        centerPoint = new Point2D.Double(
                centerPoint.getX() + (postZoom.getX() - preZoom.getX()),
                centerPoint.getY() + (postZoom.getY() - preZoom.getY())
        );
    }

    /**
     * Calculates the CameraTransform and returns it, zooms relative to null point (0,0)
     *
     * @return AffineTransform
     */
    AffineTransform getTransform() {
        if (centerPoint == null) return new AffineTransform();

        AffineTransform tx = new AffineTransform();

        tx.translate(lastMousePos.getX(), lastMousePos.getY());

        tx.scale(currZoom, currZoom);
        tx.translate(centerPoint.getX(), centerPoint.getY());

        return tx;
    }

    /**
     * Gives the inverseTransform of the latest calculated CameraTransform with getTransform()
     * Does NOT update itself, simple get method.
     *
     * @return AffineTransform
     */
    AffineTransform getInverseTransform() {
        try {
            return getTransform().createInverse();
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Handles centerPoint when mouse is dragged, drags with MouseButton.SECONDARY
     *
     * @param e MouseEvent
     */
    private void mouseDragged(MouseEvent e) {
        if (e.getButton().equals(MouseButton.SECONDARY)) {
            centerPoint = new Point2D.Double(
                    centerPoint.getX() - (lastMousePos.getX() - e.getX()) * currZoom * 0.001,
                    centerPoint.getY() - (lastMousePos.getY() - e.getY()) * currZoom * 0.001
            );

            lastMousePos = new Point2D.Double(e.getX(), e.getY());
        }
    }
}