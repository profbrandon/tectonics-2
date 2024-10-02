package simulation.display;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import util.Functional;
import util.counting.Cardinals.Two;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.math.instances.doubles.linear.Linear2D;
import util.math.instances.doubles.vectors.Vec2D;

public class ViewingCanvas implements NodeInterpretable {
    private double zoom = 0.0;
    private double width;
    private double height;

    // The center in coordinate space
    private HomTuple<Two, Double> center = Vec2D.ZERO;

    // The offset of the center in canvas space
    private final HomTuple<Two, Double> offset;

    private final List<Consumer<Function<HomTuple<Two, Double>, HomTuple<Two, Double>>>> toDraw = new ArrayList<>();

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
        this.width  = width;
        this.height = height;
    }

    public void setWidth(final double width) {
        setBounds(width, this.height);
    }

    public void setHeight(final double height) {
        setBounds(this.width, height);
    }

    public synchronized void setZoom(final double zoom) {
        this.zoom = zoom;
        clearScreen();
        draw();
    }

    public synchronized void clearDrawings() {
        this.toDraw.clear();
    }

    public synchronized void clearScreen() {
        this.context.clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
        this.context.setFill(Color.BLACK);
        this.context.fillRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
    }

    public synchronized void draw() {
        clearScreen();
        toDraw.forEach(fun -> fun.accept(this::toCanvasCoords));
    }

    public synchronized void setCenter(final HomTuple<Two, Double> center) {
        this.center = center;
        clearScreen();
        draw();
    }

    public synchronized void setCenterX(final double x) {
        setCenter(HomTuple.tuple(
            x, 
            Vec2D.y(this.center)));
    }

    public synchronized void setCenterY(final double y) {
        setCenter(HomTuple.tuple(
            Vec2D.x(this.center), 
            y));
    }

    public synchronized void drawImage(final HomTuple<Two, Double> center, final Image image) {
        this.toDraw.add(
            Functional.let(Vec2D.INSTANCE.scale(Vec2D.vector(image.getWidth(), image.getHeight()), 0.5), upperRightRelative ->
            Functional.let(Vec2D.INSTANCE.neg(upperRightRelative), lowerLeftRelative ->

        getCoords -> {
            final Runnable draw = 
                Functional.let(
                    getCoords.apply(Vec2D.INSTANCE.subVec(lowerLeftRelative, center)), 
                    realLowerLeft ->

                Functional.let(
                    getCoords.apply(Vec2D.INSTANCE.subVec(upperRightRelative, center)), 
                    realUpperRight ->

                Functional.let(
                    Vec2D.INSTANCE.subVec(realUpperRight, realLowerLeft), 
                    realWidthAndHeight ->

                () -> {
                        try {
                            this.context.drawImage(
                                image, 
                                Vec2D.x(realLowerLeft), 
                                Vec2D.y(realLowerLeft), 
                                Vec2D.x(realWidthAndHeight),
                                Vec2D.y(realWidthAndHeight));
                        } catch (final Exception e) {
                            System.out.println(e);
                        }
                    })));
                
                draw.run();
            })));
    }

    private HomTuple<Two, Double> toCanvasCoords(final HomTuple<Two, Double> vector) {
        final double mag = Math.pow(2, this.zoom);
        final Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> flipYZoom 
            = Linear2D.asLinearMap(mag, 0, 0, -mag);

        return
            Vec2D.INSTANCE.sum(
                flipYZoom.apply(Vec2D.INSTANCE.subVec(vector, this.center)),
                offset);
    }

    @Override
    public Node asNode() {
        return this.canvas;
    }
}
