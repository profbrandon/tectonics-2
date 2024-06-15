package simulation.display;

import java.util.Optional;

import gui.ExpandableTreeNode;
import gui.LabeledEntryNode;
import gui.ParameterCheckNode;
import gui.ParameterSliderNode;
import gui.ParameterTextFieldNode;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import simulation.parameters.BooleanParameter;
import simulation.parameters.ByteParameter;
import simulation.parameters.DoubleParameter;
import simulation.parameters.FloatParameter;
import simulation.parameters.IntegerParameter;
import simulation.parameters.LongParameter;
import simulation.parameters.ShortParameter;
import simulation.parameters.SimulationParameterGroup;
import simulation.parameters.StringParameter;
import util.data.trees.DistinguishedTree;

public class ParameterSelectionMenu implements NodeInterpretable {
    private final double OTHER_INSETS = 2;
    private final double LEFT_INSETS  = 10;
    private final double LABEL_WIDTH  = 100;
    private final double SLIDER_WIDTH = 100;
    
    private final VBox container = new VBox();
    private final ScrollPane scrollPane = new ScrollPane();

    public ParameterSelectionMenu(final String title, final double height) {
        final VBox scrollPaneBox = new VBox(scrollPane);
        scrollPaneBox.setPadding(new Insets(OTHER_INSETS, OTHER_INSETS, OTHER_INSETS, LEFT_INSETS));

        this.container.getChildren().addAll(new Text(title), scrollPaneBox);
        this.scrollPane.setPrefHeight(height);
    }

    public ParameterSelectionMenu(final DistinguishedTree<String, SimulationParameterGroup> parameterTree, final double height) {
        this((String) parameterTree.getNode().match(s -> s, spg -> "Unrooted Tree"), height);
        setContent(parameterTree);
    }

    private void setContent(final DistinguishedTree<String, SimulationParameterGroup> parameterTree) {
        final VBox content = new VBox(parameterTreeToNode(Optional.of(parameterTree)));
        this.scrollPane.setContent(content);
        this.scrollPane.setFitToHeight(true);
        this.scrollPane.setFitToWidth(true);
    }

    private Node parameterTreeToNode(final Optional<DistinguishedTree<String, SimulationParameterGroup>> parameterTree) {
        return parameterTree.map(tree ->
            ExpandableTreeNode.fromDistinguishedTree(
                tree, 
                s -> new LabeledEntryNode(s, LABEL_WIDTH, new Text("")),
                spg -> {
                    final VBox vbox = new VBox();
                    vbox.setFillWidth(true);

                    for (final BooleanParameter parameter : spg.getBooleanParameters()) {
                        vbox.getChildren().add(
                            new LabeledEntryNode(
                                parameter.getName(),
                                parameter.getAbbreviation(),
                                parameter.getDescription(),
                                LABEL_WIDTH,
                                new ParameterCheckNode(parameter).asNode())
                            .asNode());
                    }

                    for (final ByteParameter parameter : spg.getByteParameters()) {
                        vbox.getChildren().add(
                            new LabeledEntryNode(
                                parameter.getName(),
                                parameter.getAbbreviation(),
                                parameter.getDescription(),
                                LABEL_WIDTH,
                                new ParameterSliderNode<Byte>(parameter, Number::byteValue, 1, SLIDER_WIDTH).asNode())
                            .asNode());
                    }
                    
                    for (final ShortParameter parameter : spg.getShortParameters()) {
                        vbox.getChildren().add(
                            new LabeledEntryNode(
                                parameter.getName(),
                                parameter.getAbbreviation(),
                                parameter.getDescription(),
                                LABEL_WIDTH,
                                new ParameterSliderNode<Short>(parameter, Number::shortValue, 1, SLIDER_WIDTH).asNode())
                            .asNode());
                    }

                    for (final IntegerParameter parameter : spg.getIntegerParameters()) {
                        vbox.getChildren().add(
                            new LabeledEntryNode(
                                parameter.getName(),
                                parameter.getAbbreviation(),
                                parameter.getDescription(),
                                LABEL_WIDTH,
                                new ParameterSliderNode<Integer>(parameter, Number::intValue, 1, SLIDER_WIDTH).asNode())
                            .asNode());
                    }

                    for (final LongParameter parameter : spg.getLongParameters()) {
                        vbox.getChildren().add(
                            new LabeledEntryNode(
                                parameter.getName(),
                                parameter.getAbbreviation(),
                                parameter.getDescription(),
                                LABEL_WIDTH,
                                new ParameterSliderNode<Long>(parameter, Number::longValue, 1, SLIDER_WIDTH).asNode())
                            .asNode());
                    }

                    for (final FloatParameter parameter : spg.getFloatParameters()) {
                        vbox.getChildren().add(
                            new LabeledEntryNode(
                                parameter.getName(),
                                parameter.getAbbreviation(),
                                parameter.getDescription(),
                                LABEL_WIDTH,
                                new ParameterSliderNode<Float>(parameter, Number::floatValue, 0.001, SLIDER_WIDTH).asNode())
                            .asNode());
                    }

                    for (final DoubleParameter parameter : spg.getDoubleParameters()) {
                        vbox.getChildren().add(
                            new LabeledEntryNode(
                                parameter.getName(),
                                parameter.getAbbreviation(),
                                parameter.getDescription(),
                                LABEL_WIDTH,
                                new ParameterSliderNode<Double>(parameter, Number::doubleValue, 0.001, SLIDER_WIDTH).asNode())
                            .asNode());
                    }

                    for (final StringParameter parameter : spg.getStringParameters()) {
                        vbox.getChildren().add(
                            new LabeledEntryNode(
                                parameter.getName(),
                                parameter.getAbbreviation(),
                                parameter.getDescription(),
                                LABEL_WIDTH,
                                new ParameterTextFieldNode<String>(parameter, x -> x, SLIDER_WIDTH).asNode())
                            .asNode());
                    }

                    return () -> vbox;
                },
                true).asNode())
            .orElse(new Text("No parameters to display"));
    }

    @Override
    public Node asNode() {
        return this.container;
    }
}
