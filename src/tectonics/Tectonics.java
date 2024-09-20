package tectonics;

import javafx.application.Application;
import javafx.stage.Stage;
import simulation.display.SimulationScene;

public class Tectonics extends Application {

    private TectonicSimulation simulation = new TectonicSimulation();

    @Override
    public void start(final Stage stage) throws Exception {
        final SimulationScene scene = new SimulationScene(simulation.getParameters());

        stage.setScene(scene.asScene());
        stage.setResizable(false);
        stage.show();
    }
}
