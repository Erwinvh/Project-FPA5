package GUILogic.SimulatorLogic;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

//TODO (Could have) Zoom relative to mouse

public class CameraTransform {
    private Point2D centerPoint;
    private double zoom;
    private Point2D lastMousePos;
    private AffineTransform inverseTransform;

    /**
     * Sets initial zoom, center point, inverseTransform and lastMousePos.
     * <p>
     * Zoom:               Scaling modified by scrolling.
     * Centerpoint:        The replacement done by dragging with right mouse button. Scaling is included in calculations.
     * inverseTransform:   The AffineTransform that is need to calculate back to initial canvas conditions.
     * Used for f.e. clearRect.
     * lastMousePos        The previous mouse position when moved, pressed or scrolling the mouse.
     * Used to calculate the distance dragged.
     */
    public CameraTransform(Canvas node) {
        this.zoom = 1.0;
        this.centerPoint = new Point2D.Double(0, 0);
        this.inverseTransform = new AffineTransform();
        this.lastMousePos = new Point2D.Double(0, 0);

        node.setOnScroll(event -> {
            lastMousePos = new Point2D.Double(event.getX(), event.getY());
            zoom *= (1 + event.getDeltaY() / 150.0f);
        });

        node.setOnMouseDragged(this::mouseDragged);
        node.setOnMousePressed(event -> lastMousePos = new Point2D.Double(event.getX(), event.getY()));
    }

    /**
     * Calculates the CameraTransform and returns it, zooms relative to null point (0,0)
     *
     * @return AffineTransform
     */
    public AffineTransform getTransform() {
        if (centerPoint != null) {
            AffineTransform tx = new AffineTransform();
            AffineTransform sx = new AffineTransform();

            sx.scale(zoom, zoom);
            tx.translate(centerPoint.getX() * zoom, centerPoint.getY() * zoom);
            tx.concatenate(sx);

            try {
                this.inverseTransform = tx.createInverse();
            } catch (NoninvertibleTransformException e) {
                e.printStackTrace();
            }

            return tx;
        }

        return new AffineTransform();
    }

    /**
     * Gives the inverseTransform of the latest calculated CameraTransform with getTransform()
     * Does NOT update itself, simple get method.
     *
     * @return AffineTransform
     */
    public AffineTransform getInverseTransform() {
        return this.inverseTransform;
    }

    /**
     * Handles centerPoint when mouse is dragged, drags with MouseButton.SECONDARY
     *
     * @param e MouseEvent
     */
    private void mouseDragged(MouseEvent e) {
        if (e.getButton() == MouseButton.SECONDARY) {
            centerPoint = new Point2D.Double(
                    centerPoint.getX() - (lastMousePos.getX() - e.getX()) / zoom,
                    centerPoint.getY() - (lastMousePos.getY() - e.getY()) / zoom
            );

            lastMousePos = new Point2D.Double(e.getX(), e.getY());
        }
    }
}