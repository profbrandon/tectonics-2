package simulation.display;

import java.util.Optional;

import gui.ExpandableTreeNode;
import gui.LabelNode;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import simulation.parameters.NamedParameter;
import simulation.parameters.SimulationParameterGroup;
import util.data.trees.DistinguishedTree;

public class ParameterSelectionMenu implements NodeInterpretable {
    private final double OTHER_INSETS = 2;
    private final double LEFT_INSETS  = 10;
    
    private final VBox container = new VBox();
    private final ScrollPane scrollPane = new ScrollPane();

    private Optional<DistinguishedTree<String, SimulationParameterGroup>> currentTree = Optional.empty();

    public ParameterSelectionMenu(final String title) {
        final VBox scrollPaneBox = new VBox(scrollPane);
        scrollPaneBox.setPadding(new Insets(OTHER_INSETS, OTHER_INSETS, OTHER_INSETS, LEFT_INSETS));

        this.container.getChildren().addAll(new Text(title), scrollPaneBox);
        this.scrollPane.setContent(parameterTreeToNode(this.currentTree));
    }

    public ParameterSelectionMenu(final DistinguishedTree<String, SimulationParameterGroup> parameterTree) {
        this((String) parameterTree.getNode().match(s -> s, spg -> "Unrooted Tree"));
        this.currentTree = Optional.of(parameterTree);
        this.scrollPane.setContent(parameterTreeToNode(this.currentTree));
    }

    private Node parameterTreeToNode(final Optional<DistinguishedTree<String, SimulationParameterGroup>> parameterTree) {
        return parameterTree.map(tree ->
            ExpandableTreeNode.fromDistinguishedTree(
                tree, 
                s -> new LabelNode(s),
                spg -> {
                    final VBox vbox = new VBox();

                    for (final NamedParameter parameter : spg.getParameters()) {
                        vbox.getChildren().add(new LabelNode(parameter.getName()).asNode());
                    }

                    return () -> vbox;
                },
                true).asNode())
            .orElse(new LabelNode("No parameters to display").asNode());

    }

    @Override
    public Node asNode() {
        return this.container;
    }
}
