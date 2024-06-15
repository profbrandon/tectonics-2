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
import simulation.parameters.BooleanParameter;
import simulation.parameters.FloatParameter;
import simulation.parameters.IntegerParameter;
import simulation.parameters.SimulationParameterGroup;
import simulation.parameters.StringParameter;
import util.data.trees.DistinguishedTree;

public class SimulationScene {
    private final int MAX_SIMULATION_WIDTH  = 1024;
    private final int MAX_SIMULATION_HEIGHT = 512;

    private final double BOTTOM_BAR_HEIGHT = 256;
    private final double RIGHT_BAR_WIDTH   = 320;
    private final double INSETS            = 10;

    private Scene scene;

    public SimulationScene() {
        final BorderPane borderPane = new BorderPane();
        this.scene = new Scene(borderPane);

        final HBox bottom = new HBox();
        final VBox right  = new VBox(10);
        final HBox center = new HBox();

        final Canvas canvas = new Canvas();
        canvas.setWidth(MAX_SIMULATION_WIDTH);
        canvas.setHeight(MAX_SIMULATION_HEIGHT);

        final GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, MAX_SIMULATION_WIDTH, MAX_SIMULATION_HEIGHT);

        center.getChildren().add(canvas);

        final Background background = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));

        bottom.setMinHeight(BOTTOM_BAR_HEIGHT);
        bottom.setPadding(new Insets(INSETS));
        right.setMinWidth(RIGHT_BAR_WIDTH);
        right.setPadding(new Insets(INSETS));

        bottom.setBackground(background);
        right.setBackground(background);

        borderPane.setRight(right);
        borderPane.setBottom(bottom);
        borderPane.setCenter(center);

        final DistinguishedTree<String, SimulationParameterGroup> mapParameterTree =
            new DistinguishedTree<>("Map Parameters", 
                List.of(
                    new DistinguishedTree<>(new SimulationParameterGroup.Builder()
                        .addIntegerParameter(new IntegerParameter("Width", "W", "The width, in pixels, of the simulation.", MAX_SIMULATION_WIDTH, 0, MAX_SIMULATION_WIDTH))
                        .addIntegerParameter(new IntegerParameter("Height", "H", "The height, in pixels, of the simulation.", MAX_SIMULATION_HEIGHT, 0, MAX_SIMULATION_HEIGHT))
                        .build())));

        final DistinguishedTree<String, SimulationParameterGroup> viewingParameterTree =
            new DistinguishedTree<>("Viewing Window Parameters",
                List.of(
                    new DistinguishedTree<>(new SimulationParameterGroup.Builder()
                        .addFloatParameter(new FloatParameter("Zoom", "Z", "The zoom-level of the map. If z is the zoom value, then magnification is 2^z.", 0f, -10f, 10f))
                        .addFloatParameter(new FloatParameter("Center X", "CX", "The center's x coordinate.", 0f, (float) (-MAX_SIMULATION_WIDTH / 2), (float) (MAX_SIMULATION_WIDTH / 2)))
                        .addFloatParameter(new FloatParameter("Center Y", "CY", "The center's y coordinate.", 0f, (float) (-MAX_SIMULATION_HEIGHT / 2), (float) (MAX_SIMULATION_HEIGHT / 2)))
                        .build())));

        final DistinguishedTree<String, SimulationParameterGroup> simulationParameterTree =
            new DistinguishedTree<>("Simulation Parameters",
                List.of(
                    new DistinguishedTree<>(new SimulationParameterGroup.Builder()
                        .addIntegerParameter(new IntegerParameter("Plate Count", "PC", "How many tectonic plates should be initialized.", 10, 0, 100))
                        .build()),
                    new DistinguishedTree<>(new SimulationParameterGroup.Builder()
                        .addStringParameter(new StringParameter("Simulation Name", "Name", "The name of the simulation"))
                        .build()),
                    new DistinguishedTree<>("Erosion",
                        List.of(
                            new DistinguishedTree<>(new SimulationParameterGroup.Builder()
                                .addBooleanParameter(new BooleanParameter("Enable Erosion", "Enable", "Enables and disables erosion", true, false, true))
                                .addFloatParameter(new FloatParameter("Global Erosion Factor", "GEF","A multiplication factor for the total aggregate erosion."))
                                .addFloatParameter(new FloatParameter("Landslide Erosion Factor", "LEF", "A multiplication factor for how well landslides erode material."))
                                .addFloatParameter(new FloatParameter("Water Erosion Factor", "WEF", "A multiplication factor for how well water erodes material."))
                                .build()))),
                    new DistinguishedTree<>(new SimulationParameterGroup.Builder()
                        .addFloatParameter(new FloatParameter("Maximum Chunk Height", "MCH","How tall a chunk is allowed to be before it is truncated."))
                        .build())));

        final ParameterSelectionMenu windowParameterSelectionMenu = new ParameterSelectionMenu(mapParameterTree, 50);
        final ParameterSelectionMenu viewingParameterSelectionMenu = new ParameterSelectionMenu(viewingParameterTree, 80);
        final ParameterSelectionMenu parameterSelectionMenu = new ParameterSelectionMenu(simulationParameterTree, 240);

        right.getChildren().addAll(
            windowParameterSelectionMenu.asNode(),
            viewingParameterSelectionMenu.asNode(),
            parameterSelectionMenu.asNode());
    }

    public Scene asScene() {
        return this.scene;
    }
}
