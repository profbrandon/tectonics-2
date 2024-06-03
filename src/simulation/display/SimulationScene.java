package simulation.display;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import simulation.parameters.BasicParameterTree;
import simulation.parameters.FloatParameter;
import simulation.parameters.IntegerParameter;

public class SimulationScene {
    private Scene scene;

    public SimulationScene() {
        final BorderPane borderPane = new BorderPane();

        this.scene = new Scene(borderPane);
        this.scene.setFill(Color.BLACK);

        final HBox bottom = new HBox();
        final VBox right  = new VBox();

        final Background background = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));

        bottom.setMinHeight(200);
        right.setMinWidth(200);

        bottom.setBackground(background);
        right.setBackground(background);

        borderPane.setRight(right);
        borderPane.setBottom(bottom);

        final BasicParameterTree parameterTree = new BasicParameterTree.Builder()
            .addIntegerParameter(new IntegerParameter("Plate Count", "PC", "How many tectonic plates should be initialized."))
            .addFloatParameter(new FloatParameter("Global Erosion Factor", "GEF","A multiplication factor for the total aggregate erosion."))
            .addFloatParameter(new FloatParameter("Maximum Chunk Height", "MCH","How tall a chunk is allowed to be before it is truncated."))
            .build();

        final ParameterSelectionMenu parameterSelectionMenu = new ParameterSelectionMenu("Simulation Parameters", parameterTree);
        right.getChildren().add(parameterSelectionMenu.asNode());
    }

    public Scene asScene() {
        return this.scene;
    }
}
