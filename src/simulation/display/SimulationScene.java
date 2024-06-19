package simulation.display;

import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import simulation.parameters.FloatParameter;
import simulation.parameters.IntegerParameter;
import simulation.parameters.SimulationParameterGroup;
import util.data.trees.DistinguishedTree;

public class SimulationScene {
    private static final int MAX_SIMULATION_WIDTH  = 1024;
    private static final int MAX_SIMULATION_HEIGHT = 512;

    private static final int WINDOW_PARAMETERS_HEIGHT     = 50;
    private static final int VIEWING_PARAMETERS_HEIGHT    = 80;
    private static final int SIMULATION_PARAMETERS_HEIGHT = 280;

    private static final double BOTTOM_BAR_HEIGHT = 256;
    private static final double RIGHT_BAR_WIDTH   = 320;
    private static final double INSETS            = 10;

    private final ParameterSelectionMenu windowParametersMenu     = new ParameterSelectionMenu(getWindowParameterTree(), WINDOW_PARAMETERS_HEIGHT);
    private final ParameterSelectionMenu viewingParametersMenu    = new ParameterSelectionMenu(getViewingParameterTree(), VIEWING_PARAMETERS_HEIGHT);
    private final ParameterSelectionMenu simulationParametersMenu = new ParameterSelectionMenu("Simulation Parameters", SIMULATION_PARAMETERS_HEIGHT);

    private Scene scene;

    public SimulationScene() {
        final BorderPane borderPane = new BorderPane();
        final HBox bottom = new HBox();
        final VBox right  = new VBox(10);
        final HBox center = new HBox();
        final Canvas canvas = new Canvas();
        final GraphicsContext context = canvas.getGraphicsContext2D();
        final Background background = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));

        this.scene = new Scene(borderPane);

        canvas.setWidth(MAX_SIMULATION_WIDTH);
        canvas.setHeight(MAX_SIMULATION_HEIGHT);

        context.setFill(Color.BLACK);
        context.fillRect(0, 0, MAX_SIMULATION_WIDTH, MAX_SIMULATION_HEIGHT);

        center.getChildren().add(canvas);

        bottom.setMinHeight(BOTTOM_BAR_HEIGHT);
        bottom.setPadding(new Insets(INSETS));
        right.setMinWidth(RIGHT_BAR_WIDTH);
        right.setPadding(new Insets(INSETS));

        bottom.setBackground(background);
        right.setBackground(background);

        borderPane.setRight(right);
        borderPane.setBottom(bottom);
        borderPane.setCenter(center);

        right.getChildren().addAll(
            windowParametersMenu.asNode(),
            viewingParametersMenu.asNode(),
            simulationParametersMenu.asNode());
    }

    public Scene asScene() {
        return this.scene;
    }

    private static DistinguishedTree<String, SimulationParameterGroup> getWindowParameterTree() {
        return new DistinguishedTree<>(
            "Map Parameters", 
            List.of(
                new DistinguishedTree<>(new SimulationParameterGroup.Builder()
                    .addIntegerParameter(new IntegerParameter(
                        "Width", 
                        "W", 
                        "The width, in pixels, of the simulation.",
                        MAX_SIMULATION_WIDTH, 
                        0, 
                        MAX_SIMULATION_WIDTH))
                    .addIntegerParameter(new IntegerParameter(
                        "Height", 
                        "H", 
                        "The height, in pixels, of the simulation.", 
                        MAX_SIMULATION_HEIGHT, 
                        0, 
                        MAX_SIMULATION_HEIGHT))
                    .build())));
    }

    private static DistinguishedTree<String, SimulationParameterGroup> getViewingParameterTree() {
        return new DistinguishedTree<>("Viewing Parameters",
            List.of(
                new DistinguishedTree<>(new SimulationParameterGroup.Builder()
                    .addFloatParameter(new FloatParameter(
                        "Zoom",
                        "Z", 
                        "The zoom-level of the map. If z is the zoom value, then magnification is 2^z.", 
                        0f, 
                        -10f, 
                        10f))
                    .addFloatParameter(new FloatParameter(
                        "Center X", 
                        "CX", 
                        "The center's x coordinate.", 
                        0f,
                        (float) (-MAX_SIMULATION_WIDTH / 2),
                        (float) (MAX_SIMULATION_WIDTH / 2)))
                    .addFloatParameter(new FloatParameter(
                        "Center Y", 
                        "CY", 
                        "The center's y coordinate.", 
                        0f, 
                        (float) (-MAX_SIMULATION_HEIGHT / 2), 
                        (float) (MAX_SIMULATION_HEIGHT / 2)))
                    .build())));
    }
}
