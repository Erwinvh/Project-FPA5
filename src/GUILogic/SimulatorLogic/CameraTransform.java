package SimulatorLogic;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

//@TODO Zoom relative to mouse

public class CameraTransform {
    private Point2D centerPoint;
    private double zoom;
    private Point2D lastMousePos;
    private Canvas canvas;
    private AffineTransform inverseTransform;


    public CameraTransform(Canvas node){
        this.zoom = 1.0;
        this.centerPoint = new Point2D.Double(0,0);
        this.inverseTransform = new AffineTransform();
        this.lastMousePos = new Point2D.Double(0,0);

        node.setOnScroll(event -> {
            lastMousePos = new Point2D.Double(event.getX(), event.getY());
            zoom *= (1 + event.getDeltaY()/150.0f);
        });

      node.setOnMouseDragged(event ->  mouseDragged(event));

        node.setOnMousePressed(event -> lastMousePos = new Point2D.Double(event.getX(), event.getY()));

        this.canvas = node;
    }

    public Point2D getCenterPoint(){ return this.centerPoint; }

    public double getZoom(){ return this.zoom; }

    public AffineTransform getTransform(){
        if (centerPoint != null){
            AffineTransform tx = new AffineTransform();
            AffineTransform sx = new AffineTransform();

            sx.scale(zoom, zoom);
            tx.translate(centerPoint.getX()*zoom, centerPoint.getY()*zoom);

            tx.concatenate(sx);
            try {
                this.inverseTransform = tx.createInverse();
            } catch (NoninvertibleTransformException e) {
                e.printStackTrace();
            }
            return tx;
        } else {
            return new AffineTransform();
        }
    }

    public AffineTransform getInverseTransform(){ return this.inverseTransform; }

    public void mouseDragged(MouseEvent e) {
        if(e.getButton() == MouseButton.MIDDLE) {
            centerPoint = new Point2D.Double(
                    centerPoint.getX() - (lastMousePos.getX() - e.getX()) / zoom,
                    centerPoint.getY() - (lastMousePos.getY() - e.getY()) / zoom
            );
            lastMousePos = new Point2D.Double(e.getX(), e.getY());
        }
    }
}
