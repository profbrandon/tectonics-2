package simulation.display;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

public class ViewingCanvas implements NodeInterpretable {

    private final double actualWidth;
    private final double actualHeight;

    private double centerX;
    private double centerY;
    private double width;
    private double height;
    private double zoom;

    private final Canvas canvas;

    public ViewingCanvas(final double actualWidth, final double actualHeight) {
        this.canvas = new Canvas(actualWidth, actualHeight);
        this.actualWidth = actualWidth;
        this.actualHeight = actualHeight;
        this.width = actualWidth;
        this.height = actualHeight;
        this.centerX = 0;
        this.centerY = 0;
        this.zoom = 0;
        clear();
        drawBounds();
    }
    
    public void setCenterX(final double centerX) {
        this.centerX = centerX;
        this.clear();
        this.drawBounds();
    }

    public void setCenterY(final double centerY) {
        this.centerY = centerY;
        this.clear();
        this.drawBounds();
    }

    public void setWidth(final double width) {
        this.width = width;
        this.clear();
        this.drawBounds();
    }

    public void setHeight(final double height) {
        this.height = height;
        this.clear();
        this.drawBounds();
    }

    public void setZoom(final double zoom) {
        this.zoom = zoom;
        this.clear();
        this.drawBounds();
    }

    public void drawImage(final WritableImage image) {
        this.canvas.getGraphicsContext2D().drawImage(image, 0, 0);
    }

    private void drawBounds() {
        final GraphicsContext context = this.canvas.getGraphicsContext2D();

        context.setTransform(new Affine(getTransform()));

        context.setStroke(Color.GRAY);
        context.strokeRect(0, 0, this.width, this.height);

        System.out.println("Top Left: " + getTransform().deltaTransform(new Point2D(0, 0)));
        System.out.println("Lower Right: " + getTransform().deltaTransform(new Point2D(this.width, this.height)));
    }

    private double getInternalCanvasWidth() {
        return this.actualWidth / getZoomScalar();
    }

    private double getInternalCanvasHeight() {
        return this.actualHeight / getZoomScalar();
    }

    private double getZoomScalar() {
        return Math.pow(2, this.zoom);
    }

    private Transform getTransform() {
        final double scaleValue = getZoomScalar();
        return new Translate(-this.width / 2, -this.height / 2)
            .createConcatenation(new Scale(scaleValue, scaleValue));
            //.createConcatenation(new Translate(scaleValue * this.width / 2, scaleValue * this.height / 2));
    }

    private void clear() {
        final GraphicsContext context = this.canvas.getGraphicsContext2D();
        context.setFill(Color.BLACK);
        context.setTransform(new Affine());
        context.fillRect(0, 0, this.actualWidth, this.actualHeight);
    }

    @Override
    public Node asNode() {
        return this.canvas;
    }
}
