package erosion;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import simulation.Simulation;
import simulation.SimulationListener;
import simulation.parameters.BooleanParameter;
import simulation.parameters.FloatParameter;
import simulation.parameters.IntegerParameter;
import simulation.parameters.SimulationParameterGroup;
import util.Functional;
import util.data.trees.DistinguishedTree;

public class ErosionSimulation implements Simulation<ErosionSimulationMode> {

    public static final int WIDTH  = 700;
    public static final int HEIGHT = 500;

    private final DistinguishedTree<String, SimulationParameterGroup> parameters 
        = new DistinguishedTree<>(
            "Erosion",
            List.of(
                new DistinguishedTree<>(
                    "Landslides",
                    List.of(
                        new DistinguishedTree<>(
                            new SimulationParameterGroup.Builder()
                                .addIntegerParameter(new IntegerParameter(
                                    "Blur Radius", 
                                    "BlRad", 
                                    "The radius of effect for the given blur",
                                    1,
                                    1,
                                    20))
                                .build()),
                        new DistinguishedTree<>(
                            new SimulationParameterGroup.Builder()
                                .addFloatParameter(new FloatParameter (
                                    "Blur Strength",
                                    "BlSt", 
                                    "The strength of the blur to be applied",
                                    1.0f,
                                    0.0f,
                                    10.0f))
                                .build()),
                        new DistinguishedTree<>(
                            new SimulationParameterGroup.Builder()
                                .addFloatParameter(new FloatParameter(
                                    "Prominence Dependence", 
                                    "PromDep", 
                                    "Determines how much the blur should vary with prominence",
                                    0.0f,
                                    0.0f,
                                    1.0f))
                                .build()),
                        new DistinguishedTree<>(
                            "Megaslides",
                            List.of(
                                new DistinguishedTree<>(
                                    new SimulationParameterGroup.Builder()
                                        .addBooleanParameter(new BooleanParameter(
                                            "Enable Megaslides",
                                            "EnMegSld", 
                                            "Determines whether megaslides should be enabled",
                                            false,
                                            false,
                                            false))
                                        .build())))
                        )),
                new DistinguishedTree<>(
                    "Simulated Water",
                    List.of(
                        new DistinguishedTree<>(
                            new SimulationParameterGroup.Builder()
                                .addFloatParameter(new FloatParameter(
                                    "Slope Dependence", 
                                    "SlpDep", 
                                    "To what degree slope controls erosion",
                                    1.0f,
                                    0.0f,
                                    10.0f))
                                .build()),
                        new DistinguishedTree<>(
                            new SimulationParameterGroup.Builder()
                                .addFloatParameter(new FloatParameter(
                                    "Confluence Factor",
                                    "CnflFac",
                                    "How much the confluence of multiple lows increases the output",
                                    1.0f,
                                    0.0f,
                                    2.0f))
                                .build())
                    ))
            ));

    private ByteBuffer byteBuffer;
    private PixelBuffer<ByteBuffer> pixelBuffer;

    private ErosionState state = ErosionState.fromHeightFunction(
        row -> col -> 
            Functional.let(Math.abs(350f - col), x ->
            Functional.let(Math.abs(250f - row), y ->
            Functional.let((float)x / 350f, normalizedDistance -> 
                (1.0f - normalizedDistance) > 0.5 ? 15f : 0f
            ))));

    //private ErosionSimulationMode mode = ErosionSimulationMode.HEIGHT;

    private List<SimulationListener> listeners = new ArrayList<>();

    private ErosionThread thread = new ErosionThread();

    public ErosionSimulation() {
        final PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteBgraPreInstance();

        byteBuffer = ByteBuffer.allocateDirect(4 * WIDTH * HEIGHT);
        pixelBuffer = new PixelBuffer<ByteBuffer>(WIDTH, HEIGHT, byteBuffer, pixelFormat);

        for (int i = 0; i < WIDTH; ++i)
            for (int j = 0; j < HEIGHT; ++j) {
                int offset = 4 * (WIDTH * j + i);

                byteBuffer.put(offset + 3, (byte) 0xFF);
                //byteBuffer.put(offset, (byte) 0xFF);
            }

        state.perturb(0.1f);
    }

    @Override
    public DistinguishedTree<String, SimulationParameterGroup> getParameters() {
        return parameters;
    }

    @Override
    public void play() {
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
        this.state = ErosionState.fromHeightFunction(
            row -> col -> 
                Functional.let(Math.abs(350f - col), x ->
                Functional.let(Math.abs(250f - row), y ->
                Functional.let((float)x / 350f, normalizedDistance -> 
                    (1.0f - normalizedDistance) > 0.5 ? 15f : 0f
                ))));
    }

    @Override
    public void step() {
        thread.generateNextFrame();
    }

    @Override
    public void addSimulationListeners(final Collection<SimulationListener> listeners) {
        this.listeners.addAll(listeners);
    }
    
    @Override
    public void setMode(final ErosionSimulationMode simulationMode) {
        //this.mode = simulationMode;
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

        public void pause() {
            synchronized(this) {
                unlocked.set(false);
            }
        }

        public void play() {
            synchronized(this) {
                unlocked.set(true);
                this.start();
            }
        }

        public void generateNextFrame() {
            state.evolve();

            final float[][] heights = state.getHeights();

            for (int i = 0; i < WIDTH; ++i)
                for (int j = 0; j < HEIGHT; ++j) {
                    int offset = 4 * (WIDTH * j + i);
                    byte value = (byte)
                        (Math.max(
                            Math.min(
                                heights[i][j] / 16f, 
                                1f), 
                            0f) 
                        * 0xFF);

                    byteBuffer.put(offset + 0, value);
                    byteBuffer.put(offset + 1, value);
                    byteBuffer.put(offset + 2, value);
                }

            final WritableImage image = new WritableImage(pixelBuffer);

            for (SimulationListener listener : listeners)
                    listener.postFrame(image);
        }
    }
}
