package erosion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import simulation.Simulation;
import simulation.SimulationListener;
import simulation.parameters.BooleanParameter;
import simulation.parameters.FloatParameter;
import simulation.parameters.IntegerParameter;
import simulation.parameters.SimulationParameterGroup;
import util.data.trees.DistinguishedTree;

public class ErosionSimulation implements Simulation<ErosionSimulationMode> {

    private DistinguishedTree<String, SimulationParameterGroup> parameters 
        = new DistinguishedTree<>(
            "Erosion",
            List.of(
                new DistinguishedTree<>(
                    "Landslides",
                    List.of(
                        new DistinguishedTree<>(
                            new SimulationParameterGroup.Builder()
                                .addIntegerParameter(new IntegerParameter(
                                    "Blur Radius", 
                                    "BlRad", 
                                    "The radius of effect for the given blur",
                                    1,
                                    1,
                                    20))
                                .build()),
                        new DistinguishedTree<>(
                            new SimulationParameterGroup.Builder()
                                .addFloatParameter(new FloatParameter (
                                    "Blur Strength",
                                    "BlSt", 
                                    "The strength of the blur to be applied",
                                    1.0f,
                                    0.0f,
                                    10.0f))
                                .build()),
                        new DistinguishedTree<>(
                            new SimulationParameterGroup.Builder()
                                .addFloatParameter(new FloatParameter(
                                    "Prominence Dependence", 
                                    "PromDep", 
                                    "Determines how much the blur should vary with prominence",
                                    0.0f,
                                    0.0f,
                                    1.0f))
                                .build()),
                        new DistinguishedTree<>(
                            "Megaslides",
                            List.of(
                                new DistinguishedTree<>(
                                    new SimulationParameterGroup.Builder()
                                        .addBooleanParameter(new BooleanParameter(
                                            "Enable Megaslides",
                                            "EnMegSld", 
                                            "Determines whether megaslides should be enabled",
                                            false,
                                            false,
                                            false))
                                        .build())))
                        )),
                new DistinguishedTree<>(
                    "Simulated Water",
                    List.of(
                        new DistinguishedTree<>(
                            new SimulationParameterGroup.Builder()
                                .addFloatParameter(new FloatParameter(
                                    "Slope Dependence", 
                                    "SlpDep", 
                                    "To what degree slope controls erosion",
                                    1.0f,
                                    0.0f,
                                    10.0f))
                                .build()),
                        new DistinguishedTree<>(
                            new SimulationParameterGroup.Builder()
                                .addFloatParameter(new FloatParameter(
                                    "Confluence Factor",
                                    "CnflFac",
                                    "How much the confluence of multiple lows increases the output",
                                    1.0f,
                                    0.0f,
                                    2.0f))
                                .build())
                    ))
            ));

    private ErosionSimulationMode mode = ErosionSimulationMode.HEIGHT;

    private List<SimulationListener> listeners = new ArrayList<>();

    @Override
    public DistinguishedTree<String, SimulationParameterGroup> getParameters() {
        return parameters;
    }

    @Override
    public void play() {
        
    }

    @Override
    public void pause() {

    }

    @Override
    public void end() {

    }

    @Override
    public void step() {

    }

    @Override
    public void addSimulationListeners(final Collection<SimulationListener> listeners) {
        this.listeners.addAll(listeners);
    }
    
    @Override
    public void setMode(final ErosionSimulationMode simulationMode) {
        this.mode = simulationMode;
    }
}
