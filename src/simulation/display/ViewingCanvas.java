package simulation.display;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import util.Functional;
import util.counting.Cardinals.Two;
import util.data.algebraic.HomTuple;
import util.math.instances.doubles.linear.Linear2D;
import util.math.instances.doubles.vectors.Vec2D;

public class ViewingCanvas implements NodeInterpretable {

    private double zoom = 0.0;
    private double width;
    private double height;

    private HomTuple<Two, Double> lowerLeft;
    private HomTuple<Two, Double> upperRight;

    // The center in coordinate space
    private HomTuple<Two, Double> center = Vec2D.ZERO;

    // The offset of the center in canvas space
    private final HomTuple<Two, Double> offset;

    private final Canvas canvas;
    private final GraphicsContext context;

    public ViewingCanvas(final double actualWidth, final double actualHeight) {
        this.offset = Vec2D.vector(actualWidth / 2.0, actualHeight / 2.0);
        this.canvas = new Canvas(actualWidth, actualHeight);
        this.context = this.canvas.getGraphicsContext2D();

        setBounds(actualWidth, actualHeight);
    }

    public void setBounds(final double width, final double height) {
        this.width      = width;
        this.height     = height;
        this.upperRight = Vec2D.vector(width / 2.0, height / 2.0);
        this.lowerLeft  = Vec2D.INSTANCE.neg(this.upperRight);
        clear();
        drawBounds();
    }

    public void setWidth(final double width) {
        setBounds(width, this.height);
    }

    public void setHeight(final double height) {
        setBounds(this.width, height);
    }

    public void setZoom(final double zoom) {
        this.zoom = zoom;
        clear();
        drawBounds();
    }

    public void clear() {
        this.context.setFill(
            Functional.let(this.context.getFill(), oldFill -> {
                this.context.setFill(Color.BLACK);
                this.context.fillRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
                return oldFill;
            }));
    }

    public void setCenter(final HomTuple<Two, Double> center) {
        this.center = center;
        clear();
        drawBounds();
    }

    public void setCenterX(final double x) {
        setCenter(HomTuple.tuple(
            x, 
            Vec2D.y(this.center)));
    }

    public void setCenterY(final double y) {
        setCenter(HomTuple.tuple(
            Vec2D.x(this.center), 
            y));
    }

    public void drawLine(final HomTuple<Two, Double> start, final HomTuple<Two, Double> end, final Color color) {
        this.context.setStroke(
            Functional.let(this.context.getStroke(), oldStroke ->
            Functional.let(toCanvasCoords(start), realStart ->
            Functional.let(toCanvasCoords(end), realEnd -> {
                this.context.setStroke(color);
                this.context.strokeLine(
                    Vec2D.x(realStart), Vec2D.y(realStart), 
                    Vec2D.x(realEnd), Vec2D.y(realEnd));
                return oldStroke;
            }))));
    }

    public void drawRect(final HomTuple<Two, Double> lowerLeft, final HomTuple<Two, Double> upperRight, final Color strokeColor) {
        this.context.setStroke(
            Functional.let(this.context.getStroke(), oldStroke ->
            Functional.let(toCanvasCoords(lowerLeft), realLowerLeft ->
            Functional.let(toCanvasCoords(upperRight), realUpperRight ->
            Functional.let(Vec2D.INSTANCE.subVec(toCanvasCoords(upperRight), toCanvasCoords(lowerLeft)), widthAndHeight -> {
                this.context.setStroke(strokeColor);
                this.context.strokeRect(
                    Vec2D.x(realLowerLeft), Vec2D.y(realUpperRight),
                    Math.abs(Vec2D.x(widthAndHeight)), Math.abs(Vec2D.y(widthAndHeight)));
                return oldStroke;
            })))));
    }

    public void fillRect(final HomTuple<Two, Double> lowerLeft, final HomTuple<Two, Double> upperRight, final Color fillColor) {
        this.context.setFill(
            Functional.let(this.context.getFill(), oldFill ->
            Functional.let(toCanvasCoords(lowerLeft), realLowerLeft ->
            Functional.let(toCanvasCoords(upperRight), realUpperRight ->
            Functional.let(Vec2D.INSTANCE.subVec(toCanvasCoords(upperRight), toCanvasCoords(lowerLeft)), widthAndHeight -> {
                this.context.setFill(fillColor);
                this.context.fillRect(
                    Vec2D.x(realLowerLeft), Vec2D.y(realUpperRight),
                    Math.abs(Vec2D.x(widthAndHeight)), Math.abs(Vec2D.y(widthAndHeight)));
                return oldFill;
            })))));
    }

    public void drawCircle(final HomTuple<Two, Double> center, final double radius, final Color strokeColor) {
        this.context.setStroke(
            Functional.let(this.context.getStroke(), oldStroke -> 
            Functional.let(toCanvasCoords(center), realCenter ->
            Functional.let(toCanvasCoords(Vec2D.INSTANCE.sum(center, HomTuple.tuple(-radius, radius))), upperLeft ->
            Functional.let(Vec2D.x(Vec2D.INSTANCE.subVec(realCenter, upperLeft)), realRadius -> {
                this.context.setStroke(strokeColor);
                this.context.strokeOval(Vec2D.x(upperLeft), Vec2D.y(upperLeft), 2 * realRadius, 2 * realRadius);
                return oldStroke;
            })))));
    }

    public void fillCircle(final HomTuple<Two, Double> center, final double radius, final Color fillColor) {
        this.context.setFill(
            Functional.let(this.context.getFill(), oldFill -> 
            Functional.let(toCanvasCoords(center), realCenter ->
            Functional.let(toCanvasCoords(Vec2D.INSTANCE.sum(center, HomTuple.tuple(-radius, radius))), upperLeft ->
            Functional.let(Vec2D.x(Vec2D.INSTANCE.subVec(realCenter, upperLeft)), realRadius -> {
                this.context.setFill(fillColor);
                this.context.fillOval(Vec2D.x(upperLeft), Vec2D.y(upperLeft), 2 * realRadius, 2 * realRadius);
                return oldFill;
            })))));
    }

    private void drawBounds() {
        drawRect(this.lowerLeft, this.upperRight, Color.GRAY);
        drawLine(this.lowerLeft, this.upperRight, Color.GRAY);
        drawCircle(Vec2D.ZERO, Math.sqrt(this.width * this.width + this.height * this.height) / 2.0, Color.GRAY);
    }

    private HomTuple<Two, Double> toCanvasCoords(final HomTuple<Two, Double> vector) {
        return
            Vec2D.INSTANCE.sum(
                Linear2D.asLinearMap(1, 0, 0, -1).apply(
                    Vec2D.INSTANCE.subVec(
                        Vec2D.INSTANCE.scale(vector, Math.pow(2, this.zoom)),
                        this.center)),
                offset);
    }

    @Override
    public Node asNode() {
        return this.canvas;
    }
}
