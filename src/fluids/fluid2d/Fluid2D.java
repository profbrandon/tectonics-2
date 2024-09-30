package fluids.fluid2d;

import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;
import simulation.ThreadedSimulation;
import simulation.display.SimulationScene;
import util.math.MathUtil;

public class Fluid2D extends Application {
    
    private final int WIDTH  = 700;
    private final int HEIGHT = 500;

    private final ThreadedSimulation<Fluid2DSimulationMode, Fluid2DState, Fluid2DParameters> simulation = 
        new ThreadedSimulation<>(
            WIDTH, 
            HEIGHT, 
            Fluid2DSimulationMode.DENSITY,
            new Fluid2DParameters(), 
            __ -> new Fluid2DState(WIDTH, HEIGHT), 
            s -> m -> buffer -> {
                final float[][] density = s.getDensity();
                
                for (int i = 0; i < density.length; ++i)
                    for (int j = 0; j < density[0].length; ++j) {
                        final int offset = 4 * (WIDTH * j + i);
                        final byte value = (byte) (MathUtil.squeeze(density[i][j]) * 0xFF);

                        buffer.put(offset + 0, value);
                        buffer.put(offset + 1, value);
                        buffer.put(offset + 2, value);
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
