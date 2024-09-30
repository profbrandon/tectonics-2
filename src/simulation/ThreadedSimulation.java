package simulation;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import simulation.parameters.SimulationParameterGroup;
import util.data.algebraic.Maybe;
import util.data.algebraic.Unit;
import util.data.trees.DistinguishedTree;

public class ThreadedSimulation
    <M extends SimulationMode, 
     S extends SimulationState, 
     P extends SimulationParameters> 
    implements Simulation<M> {
    
    private final int WIDTH;
    private final int HEIGHT;

    private final P parameters;

    private final Function<Unit, S> getDefaultState;
    private final Function<S, Function<M, Consumer<ByteBuffer>>> setBytes;

    private ByteBuffer byteBuffer;
    private PixelBuffer<ByteBuffer> pixelBuffer;

    private Maybe<S> state = Maybe.nothing();

    private M mode;

    private List<SimulationListener> listeners = new ArrayList<>();

    private SimulationThread thread = new SimulationThread();

    public ThreadedSimulation(
        final int width, 
        final int height, 
        final M defaultMode,
        final P parameters,
        final Function<Unit, S> getDefaultState,
        final Function<S, Function<M, Consumer<ByteBuffer>>> setBytes) {
        
        this.WIDTH  = width;
        this.HEIGHT = height;

        this.parameters = parameters;

        this.getDefaultState = getDefaultState;
        this.setBytes        = setBytes;

        this.mode = defaultMode;

        final PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteBgraPreInstance();
        this.byteBuffer  = ByteBuffer.allocateDirect(4 * WIDTH * HEIGHT);
        this.pixelBuffer = new PixelBuffer<ByteBuffer>(WIDTH, HEIGHT, byteBuffer, pixelFormat);

        for (int i = 0; i < WIDTH; ++i)
            for (int j = 0; j < HEIGHT; ++j) {
                int offset = 4 * (WIDTH * j + i);
                byteBuffer.put(offset + 3, (byte) 0xFF); // Set opaque
            }
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
                this.state = Maybe.just(getDefaultState.apply(Unit.unit()));
                return Unit.unit();
            },
            s -> Unit.unit());

        this.thread = new SimulationThread();
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
                this.state = Maybe.just(getDefaultState.apply(Unit.unit()));
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
    public void setMode(final M simulationMode) {
        this.mode = simulationMode;
    }

    private class SimulationThread extends Thread {

        private AtomicBoolean unlocked = new AtomicBoolean(false);

        public SimulationThread() { }

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
                setBytes
                    .apply(s)
                    .apply(mode)
                    .accept(byteBuffer);

                final WritableImage image = new WritableImage(pixelBuffer);

                for (SimulationListener listener : listeners)
                        listener.postFrame(image);

                return Maybe.just(Unit.unit());
            });
        }
    }
}
