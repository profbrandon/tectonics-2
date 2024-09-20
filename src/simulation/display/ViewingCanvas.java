package simulation.display;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import util.Functional;
import util.counting.Cardinals.Two;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Maybe;
import util.data.algebraic.Unit;
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

    private final List<Runnable> toDraw = new ArrayList<>();

    private final Canvas canvas;
    private final GraphicsContext context;

    public ViewingCanvas(final double actualWidth, final double actualHeight) {
        this.offset = Vec2D.vector(actualWidth / 2.0, actualHeight / 2.0);
        this.canvas = new Canvas(actualWidth, actualHeight);
        this.context = this.canvas.getGraphicsContext2D();

        clearScreen();

        setBounds(actualWidth, actualHeight);
    }

    public void setBounds(final double width, final double height) {
        this.width      = width;
        this.height     = height;
        this.upperRight = Vec2D.vector(width / 2.0, height / 2.0);
        this.lowerLeft  = Vec2D.INSTANCE.neg(this.upperRight);
    }

    public void setWidth(final double width) {
        setBounds(width, this.height);
    }

    public void setHeight(final double height) {
        setBounds(this.width, height);
    }

    public void setZoom(final double zoom) {
        this.zoom = zoom;
    }

    public void clearDrawings() {
        this.toDraw.clear();
    }

    public void clearScreen() {
        this.context.setFill(Color.BLACK);
        this.context.fillRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
    }

    public void draw() {
        clearScreen();
        toDraw.forEach(fun -> fun.run());
    }

    public void setCenter(final HomTuple<Two, Double> center) {
        this.center = center;
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

    public void drawImage(final HomTuple<Two, Double> center, final Image image) {
        Maybe.<Runnable, Unit>bind(
            Functional.let(Vec2D.vector(image.getWidth(), image.getHeight()), widthAndHeight ->
            Functional.let(Linear2D.asLinearMap(0.5, 0, 0, -0.5).apply(widthAndHeight), lowerLeftRelative ->
            Functional.let(Vec2D.INSTANCE.neg(lowerLeftRelative), upperRightRelative ->
            Maybe.bind(toCanvasCoords(Vec2D.INSTANCE.sum(center, lowerLeftRelative)), realLowerLeft ->
            Maybe.bind(toCanvasCoords(Vec2D.INSTANCE.sum(center, upperRightRelative)), realUpperRight ->
            Functional.let(Vec2D.INSTANCE.subVec(realLowerLeft, realUpperRight), realWidthAndHeight ->
            Maybe.<Runnable>just(
                () -> {
                    this.context.drawImage(
                        image, 
                        Vec2D.x(realUpperRight), 
                        Vec2D.y(realUpperRight), 
                        Vec2D.x(realWidthAndHeight),
                         Vec2D.y(realWidthAndHeight));
                }))))))),
            fun -> {
                this.toDraw.add(fun);
                return Maybe.<Unit>nothing();
            });
    }

    private Maybe<HomTuple<Two, Double>> toCanvasCoords(final HomTuple<Two, Double> vector) {
        final Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> flipY = Linear2D.asLinearMap(1, 0, 0, -1);
        return Functional.let(
            Vec2D.INSTANCE.sum(
                flipY.apply(
                        Vec2D.INSTANCE.scale(
                            Vec2D.INSTANCE.subVec(vector, this.center), Math.pow(2, this.zoom))),
                offset),
            coordVector ->
                Vec2D.x(coordVector) < 0 || Vec2D.x(coordVector) > this.canvas.getWidth() ||
                Vec2D.y(coordVector) < 0 || Vec2D.y(coordVector) > this.canvas.getHeight()
                    ? Maybe.nothing()
                    : Maybe.just(coordVector));
    }

    @Override
    public Node asNode() {
        return this.canvas;
    }
}
