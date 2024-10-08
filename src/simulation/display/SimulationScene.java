package simulation.display;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import simulation.SimulationListener;
import simulation.SimulationMode;
import simulation.parameters.FloatParameter;
import simulation.parameters.IntegerParameter;
import simulation.parameters.SimulationParameterGroup;
import util.data.trees.DistinguishedTree;
import util.math.instances.doubles.vectors.Vec2D;

public class SimulationScene implements SimulationListener {
    private static final int MAX_SIMULATION_WIDTH  = 1024;
    private static final int MAX_SIMULATION_HEIGHT = 512;

    private static final int WINDOW_PARAMETERS_HEIGHT     = 50;
    private static final int VIEWING_PARAMETERS_HEIGHT    = 80;
    private static final int SIMULATION_PARAMETERS_HEIGHT = 280;

    private static final double BOTTOM_BAR_HEIGHT = 256;
    private static final double RIGHT_BAR_WIDTH   = 320;
    private static final double INSETS            = 10;

    private DistinguishedTree<String, SimulationParameterGroup> windowParametersTree  = getWindowParameterTree();
    private DistinguishedTree<String, SimulationParameterGroup> viewingParametersTree = getViewingParameterTree();

    private final ParameterSelectionMenu windowParametersMenu     = new ParameterSelectionMenu(windowParametersTree, WINDOW_PARAMETERS_HEIGHT);
    private final ParameterSelectionMenu viewingParametersMenu    = new ParameterSelectionMenu(viewingParametersTree, VIEWING_PARAMETERS_HEIGHT);
    private final ParameterSelectionMenu simulationParametersMenu;

    private final ViewingCanvas canvas = new ViewingCanvas(MAX_SIMULATION_WIDTH, MAX_SIMULATION_HEIGHT);

    private final Button playButton = new Button("Play");
    private final Button stepButton = new Button("Step");
    private final Button endButton  = new Button("End");

    private final ComboBox<SimulationMode> comboBox;

    private final AtomicLong lastTime = new AtomicLong(System.currentTimeMillis());

    private Scene scene;

    public SimulationScene(final DistinguishedTree<String, SimulationParameterGroup> simulationParameterTree, final List<SimulationMode> modes) {
        this.simulationParametersMenu = new ParameterSelectionMenu(simulationParameterTree, SIMULATION_PARAMETERS_HEIGHT);
        this.comboBox = new ComboBox<SimulationMode>();
        this.comboBox.getItems().addAll(modes);

        final BorderPane borderPane = new BorderPane();
        final HBox bottom = new HBox();
        final VBox right  = new VBox(10);
        final HBox center = new HBox();
        final Background background = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));

        this.scene = new Scene(borderPane);

        center.getChildren().add(this.canvas.asNode());

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

        bottom.getChildren().addAll(
            playButton,
            stepButton,
            endButton,
            comboBox);
    }

    public void bindPlayButton(final Runnable onPlay, final Runnable onPause) {
        final AtomicBoolean play = new AtomicBoolean(true);
        playButton.setOnAction(
            event -> {
                if (play.get()) {
                    onPlay.run();
                    playButton.setText("Pause");
                    play.set(false);
                } else {
                    onPause.run();
                    playButton.setText("Play");
                    play.set(true);
                }
            });
    }

    public void bindStepButton(final Runnable onStep) {
        stepButton.setOnAction(event -> onStep.run());
    }

    public void bindEndButton(final Runnable onEnd) {
        endButton.setOnAction(event -> {
            onEnd.run();
            this.canvas.clearDrawings();
            this.canvas.clearScreen();
        });
    }

    public void bindModeBox(final Consumer<SimulationMode> onModeChange) {
        comboBox.setOnAction(event -> {
            onModeChange.accept(comboBox.getValue());
        });
    }

    public Scene asScene() {
        return this.scene;
    }

    private DistinguishedTree<String, SimulationParameterGroup> getWindowParameterTree() {
        final IntegerParameter widthParameter = new IntegerParameter(
            "Width", 
            "W", 
            "The width, in pixels, of the simulation.",
            MAX_SIMULATION_WIDTH, 
            0, 
            MAX_SIMULATION_WIDTH);
            
        widthParameter.addUpdateListener(width -> this.canvas.setWidth(width));

        final IntegerParameter heightParameter = new IntegerParameter(
            "Height", 
            "H", 
            "The height, in pixels, of the simulation.", 
            MAX_SIMULATION_HEIGHT, 
            0, 
            MAX_SIMULATION_HEIGHT);

        heightParameter.addUpdateListener(height -> this.canvas.setHeight(height));

        return new DistinguishedTree<>(
            "Map Parameters", 
            List.of(
                new DistinguishedTree<>(new SimulationParameterGroup.Builder()
                    .addIntegerParameter(widthParameter)
                    .addIntegerParameter(heightParameter)
                    .build())));
    }

    private DistinguishedTree<String, SimulationParameterGroup> getViewingParameterTree() {
        final FloatParameter zoomParameter = new FloatParameter(
            "Zoom",
            "Z", 
            "The zoom-level of the map. If z is the zoom value, then magnification is 2^z.", 
            0f, 
            -5f, 
            5f);

        zoomParameter.addUpdateListener(zoom -> this.canvas.setZoom(zoom));

        final FloatParameter centerXParameter = new FloatParameter(
            "Center X", 
            "CX", 
            "The center's x coordinate.", 
            0f,
            - MAX_SIMULATION_WIDTH / 2.0f,
            MAX_SIMULATION_WIDTH / 2.0f);

        centerXParameter.addUpdateListener(centerX -> this.canvas.setCenterX(centerX));

        final FloatParameter centerYParameter = new FloatParameter(
            "Center Y", 
            "CY", 
            "The center's y coordinate.", 
            0f,
            - MAX_SIMULATION_HEIGHT / 2.0f,
            MAX_SIMULATION_HEIGHT / 2.0f);

        centerYParameter.addUpdateListener(centerY -> this.canvas.setCenterY(centerY));

        return new DistinguishedTree<>(
            "Viewing Parameters",
            List.of(
                new DistinguishedTree<>(new SimulationParameterGroup.Builder()
                    .addFloatParameter(zoomParameter)
                    .addFloatParameter(centerXParameter)
                    .addFloatParameter(centerYParameter)
                    .build())));
    }

    @Override
    public synchronized void postFrame(final WritableImage image) {
        final long currentTime = System.currentTimeMillis();

        if (currentTime - lastTime.get() > 20) {
            this.canvas.clearDrawings();
            this.canvas.drawImage(Vec2D.ZERO, image);
            this.canvas.clearScreen();
            this.canvas.draw();
        }
    }
}
