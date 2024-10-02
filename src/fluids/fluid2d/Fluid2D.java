package fluids.fluid2d;

import java.util.List;

import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import simulation.ThreadedSimulation;
import simulation.display.SimulationScene;
import util.counting.Cardinals.Two;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.MathUtil;
import util.math.instances.doubles.vectors.Vec2D;

public class Fluid2D extends Application {
    
    private final int WIDTH  = 500;
    private final int HEIGHT = 500;

    private final Fluid2DParameters parameters = new Fluid2DParameters();

    private final ThreadedSimulation<Fluid2DSimulationMode, Fluid2DState, Fluid2DParameters> simulation = 
        new ThreadedSimulation<>(
            WIDTH, 
            HEIGHT, 
            Fluid2DSimulationMode.DENSITY,
            parameters,
            __ -> new Fluid2DState(WIDTH, HEIGHT, parameters), 
            s -> m -> buffer -> {
                switch(m) {
                    case DENSITY:
                        final float[][] density = s.getDensity();
                    
                        for (int i = 0; i < density.length; ++i)
                            for (int j = 0; j < density[0].length; ++j) {
                                final int offset = 4 * (WIDTH * j + i);
                                final byte value = (byte) (MathUtil.squeeze(1000 * density[i][j]) * 0xFF);
        
                                buffer.put(offset + 0, value);
                                buffer.put(offset + 1, value);
                                buffer.put(offset + 2, value);
                            }
                        break;

                    case VELOCITY:
                        final Prod<float[][], float[][]> velocity = s.getVelocity();

                        for (int i = 0; i < WIDTH; ++i)
                            for (int j = 0; j < HEIGHT; ++j) {
                                final int offset = 4 * (WIDTH * j + i);

                                final HomTuple<Two, Double> vector = Vec2D.vector(velocity.first()[i][j], velocity.second()[i][j]);

                                final double angle = Vec2D.angle(vector, Vec2D.UNIT_X);
                                final double mag   = Vec2D.INSTANCE.length(vector);

                                final Color color = Color.hsb(angle * 180 / Math.PI, 1f, MathUtil.squeeze(4f * (float) mag));

                                buffer.put(offset + 0, (byte) (color.getBlue() * 0xFF));
                                buffer.put(offset + 1, (byte) (color.getGreen() * 0xFF));
                                buffer.put(offset + 2, (byte) (color.getRed() * 0xFF));
                            }

                        break;

                    case VELOCITY_X:
                        final float[][] velX = s.getVelocity().first();
                        
                        for (int i = 0; i < velX.length; ++i)
                            for (int j = 0; j < velX[0].length; ++j) {
                                final int offset = 4 * (WIDTH * j + i);
                                final int value = (int) (MathUtil.squeeze(4 * velX[i][j]) * 0xFF);

                                buffer.put(offset + 0, (byte) 0x00);
                                buffer.put(offset + 1, (byte) 0x00);
                                buffer.put(offset + 2, (byte) 0x00);

                                if (value > 0)
                                    buffer.put(offset + 0, (byte) value);
                                else
                                    buffer.put(offset + 2, (byte) (-value));
                            }
                        break;

                    case VELOCITY_Y:
                        final float[][] velY = s.getVelocity().second();
                            
                        for (int i = 0; i < velY.length; ++i)
                            for (int j = 0; j < velY[0].length; ++j) {
                                final int offset = 4 * (WIDTH * j + i);
                                final int value = (int) (MathUtil.squeeze(4 * velY[i][j]) * 0xFF);

                                buffer.put(offset + 0, (byte) 0x00);
                                buffer.put(offset + 1, (byte) 0x00);
                                buffer.put(offset + 2, (byte) 0x00);

                                if (value > 0)
                                    buffer.put(offset + 0, (byte) value);
                                else
                                    buffer.put(offset + 2, (byte) (-value));
                            }
                        break;
                }
            });

    @Override
    public void start(final Stage stage) throws Exception {
        final SimulationScene scene = new SimulationScene(simulation.getParameters(), List.of(Fluid2DSimulationMode.values()));

        scene.bindPlayButton(simulation::play, simulation::pause);
        scene.bindStepButton(simulation::step);
        scene.bindEndButton(simulation::end);

        scene.bindModeBox(mode -> {
            simulation.setMode((Fluid2DSimulationMode) mode);
        });

        simulation.addSimulationListeners(List.of(scene));

        stage.setScene(scene.asScene());
        stage.setResizable(false);
        stage.show();

        stage.setOnCloseRequest(event -> simulation.end());
    }
}
