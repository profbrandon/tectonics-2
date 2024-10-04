package erosion;

import java.util.List;

import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import simulation.ThreadedSimulation;
import simulation.display.SimulationScene;
import util.Functional;
import util.counting.Cardinals.Two;
import util.data.algebraic.HomTuple;
import util.math.MathUtil;
import util.math.instances.doubles.vectors.Vec2D;

public class Erosion extends Application {

    public static final int WIDTH  = 700;
    public static final int HEIGHT = 500;

    private final ErosionParameters parameters = new ErosionParameters();

    private ThreadedSimulation<ErosionSimulationMode, ErosionState, ErosionParameters> simulation = new ThreadedSimulation<ErosionSimulationMode, ErosionState, ErosionParameters>(
        WIDTH,
        HEIGHT,
        ErosionSimulationMode.HEIGHT,
        parameters,
        __ -> {
            final ErosionState state = ErosionState.fromHeightFunction(
                parameters,
                row -> col -> 
                    Functional.let(Math.abs(350f - col), x ->
                    Functional.let(Math.abs(250f - row), y ->
                    Functional.let((float)x / 350f, normX -> 
                    Functional.let((float)y / 250f, normY ->

                    normX < 0.5 ? 15f : 1f
                    //15f * (1.0f - (float)Math.sqrt(normX * normX + normY * normY))
                    //15f * (1f - normX)
                    )))));

            state.perturb(parameters.getInitialNoise());
            return state;
        },
        s -> m -> buffer -> {
            final float[][] heights = s.getHeights();

            switch(m) {
                case HEIGHT:
                    for (int i = 0; i < WIDTH; ++i)
                        for (int j = 0; j < HEIGHT; ++j) {
                            int offset = 4 * (WIDTH * j + i);
                            byte value = (byte)
                                (MathUtil.squeeze(heights[i][j] / 4f)
                                * 0xFF);

                            buffer.put(offset + 0, value);
                            buffer.put(offset + 1, value);
                            buffer.put(offset + 2, value);
                        }
                    break;

                case SEDIMENT:
                    final float[][] sediment = s.getSediment();

                    for (int i = 0; i < WIDTH; ++i)
                        for (int j = 0; j < HEIGHT; ++j) {
                            int offset = 4 * (WIDTH * j + i);
                            byte value = (byte)
                                (MathUtil.squeeze(5f * sediment[i][j])
                                * 0xFF);

                            buffer.put(offset + 0, value);
                            buffer.put(offset + 1, value);
                            buffer.put(offset + 2, value);
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

                            final Color color = Color.hsb(
                                360 * angle, 
                                0.5f + 0.5f * MathUtil.squeeze(net), 
                                0.5f + 0.5f * MathUtil.squeeze(mag));

                            final int offset = 4 * (WIDTH * j + i);

                            buffer.put(offset + 0, (byte) (color.getBlue() * 0xFF));
                            buffer.put(offset + 1, (byte) (color.getGreen() * 0xFF));
                            buffer.put(offset + 2, (byte) (color.getRed() * 0xFF));
                        }
                    }
                    break;
                }
        });

    @Override
    public void start(final Stage stage) throws Exception {
        final SimulationScene scene = new SimulationScene(simulation.getParameters(), List.of(ErosionSimulationMode.values()));

        scene.bindPlayButton(simulation::play, simulation::pause);
        scene.bindStepButton(simulation::step);
        scene.bindEndButton(simulation::end);

        scene.bindModeBox(mode -> {
            simulation.setMode((ErosionSimulationMode) mode);
        });

        simulation.addSimulationListeners(List.of(scene));

        stage.setScene(scene.asScene());
        stage.setResizable(false);
        stage.show();

        stage.setOnCloseRequest(event -> simulation.end());
    }
}