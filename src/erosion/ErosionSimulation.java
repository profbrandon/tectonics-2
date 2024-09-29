package erosion;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import simulation.Simulation;
import simulation.SimulationListener;
import simulation.parameters.SimulationParameterGroup;
import util.Functional;
import util.counting.Cardinals.Two;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Maybe;
import util.data.algebraic.Unit;
import util.data.trees.DistinguishedTree;
import util.math.instances.doubles.vectors.Vec2D;

public class ErosionSimulation implements Simulation<ErosionSimulationMode> {

    public static final int WIDTH  = 700;
    public static final int HEIGHT = 500;

    private final ErosionParameters parameters = new ErosionParameters();

    private ByteBuffer byteBuffer;
    private PixelBuffer<ByteBuffer> pixelBuffer;

    private Maybe<ErosionState> state = Maybe.nothing();

    private ErosionSimulationMode mode = ErosionSimulationMode.HEIGHT;

    private List<SimulationListener> listeners = new ArrayList<>();

    private ErosionThread thread = new ErosionThread();

    public ErosionSimulation() {
        final PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteBgraPreInstance();

        byteBuffer = ByteBuffer.allocateDirect(4 * WIDTH * HEIGHT);
        pixelBuffer = new PixelBuffer<ByteBuffer>(WIDTH, HEIGHT, byteBuffer, pixelFormat);

        for (int i = 0; i < WIDTH; ++i)
            for (int j = 0; j < HEIGHT; ++j) {
                int offset = 4 * (WIDTH * j + i);
                byteBuffer.put(offset + 3, (byte) 0xFF); // Set opaque
            }
    }

    private ErosionState getState() {
        final ErosionState state = 
            ErosionState.fromHeightFunction(
                this.parameters,
                row -> col -> 
                    Functional.let(Math.abs(350f - col), x ->
                    Functional.let(Math.abs(250f - row), y ->
                    Functional.let((float)x / 350f, normX -> 
                    Functional.let((float)y / 250f, normY ->

                    normX < 0.5 ? 15f : 1f
                    //15f * (1.0f - (float)Math.sqrt(normX * normX + normY * normY))
                    //15f * (1f - normX)
                    )))));

        state.perturb(this.parameters.getInitialNoise());

        return state;
    }

    @Override
    public DistinguishedTree<String, SimulationParameterGroup> getParameters() {
        return parameters.getParameterTree();
    }

    @Override
    public void play() {
        this.parameters.lock();
        
        this.state.match(
            __ -> {
                this.state = Maybe.just(getState());
                return Unit.unit();
            },
            s -> Unit.unit());

        this.thread = new ErosionThread();
        thread.play();
    }

    @Override
    public void pause() {
        thread.pause();
    }

    @Override
    public void end() {
        thread.pause();
        this.parameters.unlock();
        this.state = Maybe.nothing();
        thread.postImage();
    }

    @Override
    public void step() {
        this.parameters.lock();
        this.state.match(
            __ -> {
                this.state = Maybe.just(getState());
                return Unit.unit();
            },
            s -> Unit.unit());
        thread.generateNextFrame();
    }

    @Override
    public void addSimulationListeners(final Collection<SimulationListener> listeners) {
        this.listeners.addAll(listeners);
        thread.postImage();
    }
    
    @Override
    public void setMode(final ErosionSimulationMode simulationMode) {
        this.mode = simulationMode;
    }

    private class ErosionThread extends Thread {

        private AtomicBoolean unlocked = new AtomicBoolean(false);

        public ErosionThread() { }

        public void run() {
            int count = 0;

            while (unlocked.get()) {
                System.out.print("\r" + count);
                generateNextFrame();
                ++count;
            }
        }

        public synchronized void pause() {
            unlocked.set(false);
        }

        public synchronized void play() {
            unlocked.set(true);
            this.start();
        }

        public void generateNextFrame() {
            Maybe.bind(state, s -> { s.evolve(); return Maybe.just(Unit.unit()); });
            postImage();
        }

        public void postImage() {
            Maybe.bind(state, s -> {

                final float[][] heights = s.getHeights();

                switch(mode) {
                    case HEIGHT:
                        for (int i = 0; i < WIDTH; ++i)
                            for (int j = 0; j < HEIGHT; ++j) {
                                int offset = 4 * (WIDTH * j + i);
                                byte value = (byte)
                                    (squeeze(heights[i][j] / 4f)
                                    * 0xFF);

                                byteBuffer.put(offset + 0, value);
                                byteBuffer.put(offset + 1, value);
                                byteBuffer.put(offset + 2, value);
                            }
                        break;

                case SEDIMENT:
                    final float[][] sediment = s.getSediment();

                    for (int i = 0; i < WIDTH; ++i)
                        for (int j = 0; j < HEIGHT; ++j) {
                            int offset = 4 * (WIDTH * j + i);
                            byte value = (byte)
                                (squeeze(5f * sediment[i][j])
                                * 0xFF);

                            byteBuffer.put(offset + 0, value);
                            byteBuffer.put(offset + 1, value);
                            byteBuffer.put(offset + 2, value);
                        }
                    break;
                
                case SLOPE:
                    for (int i = 1; i < WIDTH - 1; ++i) {
                        for (int j = 1; j < HEIGHT - 1; ++j) {
                            final float ref = heights[i][j];

                            final float diffX2 = heights[i + 1][j] - ref;
                            final float diffX1 = heights[i - 1][j] - ref;

                            final float diffY1 = heights[i][j + 1] - ref;
                            final float diffY2 = heights[i][j - 1] - ref;

                            final float diffX = diffX2 - diffX1;
                            final float diffY = diffY2 - diffY1;

                            final float net = diffX1 + diffX2 + diffY1 + diffY2;

                            final HomTuple<Two, Double> slope = Vec2D.vector(-diffX, -diffY);
                            final HomTuple<Two, Double> dir   = Vec2D.INSTANCE.normalize(slope);
                            
                            final float angle = (float) (Math.acos(Vec2D.INSTANCE.dot(dir, Vec2D.UNIT_X)) / Math.PI);
                            final float mag   = (float) Math.sqrt(Vec2D.INSTANCE.dot(slope, slope));

                            final Color color = Color.hsb(360 * angle, 0.5f + 0.5f * squeeze(net), 0.5f + 0.5f * squeeze(mag));

                            final int offset = 4 * (WIDTH * j + i);

                            byteBuffer.put(offset + 0, (byte) (color.getBlue() * 0xFF));
                            byteBuffer.put(offset + 1, (byte) (color.getGreen() * 0xFF));
                            byteBuffer.put(offset + 2, (byte) (color.getRed() * 0xFF));
                        }
                    }
                    break;
                }

                final WritableImage image = new WritableImage(pixelBuffer);

                for (SimulationListener listener : listeners)
                        listener.postFrame(image);

                return Maybe.just(Unit.unit());
            });
        }
    }

    private static float squeeze(float value) {
        return 1.0f - 2.0f / ((float)Math.pow(2.0, value) + 1f);
    }
}
