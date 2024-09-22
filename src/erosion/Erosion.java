package erosion;

import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;
import simulation.display.SimulationScene;

public class Erosion extends Application {

    private ErosionSimulation simulation = new ErosionSimulation();

    @Override
    public void start(final Stage stage) throws Exception {
        final SimulationScene scene = new SimulationScene(simulation.getParameters());

        scene.bindPlayButton(simulation::play, simulation::pause);
        scene.bindStepButton(simulation::step);
        scene.bindEndButton(simulation::end);

        simulation.addSimulationListeners(List.of(scene));

        stage.setScene(scene.asScene());
        stage.setResizable(false);
        stage.show();

        stage.setOnCloseRequest(event -> simulation.end());
    }
}