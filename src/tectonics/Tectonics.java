package tectonics;

import javafx.application.Application;
import javafx.stage.Stage;
import simulation.display.SimulationScene;

public class Tectonics extends Application {
    @Override
    public void start(final Stage stage) throws Exception {
        final SimulationScene scene = new SimulationScene();

        stage.setScene(scene.asScene());

        stage.show();
    }
}
