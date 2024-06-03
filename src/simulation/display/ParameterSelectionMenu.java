package simulation.display;

import java.util.Optional;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import simulation.parameters.BasicParameterTree;
import simulation.parameters.NamedParameter;

public class ParameterSelectionMenu implements NodeInterpretable {
    
    private final VBox container = new VBox();
    private final ScrollPane scrollPane = new ScrollPane();

    private Optional<BasicParameterTree> currentTree = Optional.empty();

    public ParameterSelectionMenu(final String title) {
        final VBox scrollPaneBox = new VBox(scrollPane);
        scrollPaneBox.setPadding(new Insets(2, 2, 2, 10));

        this.container.getChildren().addAll(new Text(title), scrollPaneBox);
        this.scrollPane.setContent(parameterTreeToNode(this.currentTree));
    }

    public ParameterSelectionMenu(final String title, final BasicParameterTree parameterTree) {
        this(title);
        this.currentTree = Optional.of(parameterTree);
        this.scrollPane.setContent(parameterTreeToNode(this.currentTree));
    }

    private final Node parameterTreeToNode(final Optional<BasicParameterTree> parameterTree) {
        final VBox vbox = new VBox();

        parameterTree.ifPresentOrElse(tree -> 
            vbox.getChildren().addAll(
                tree.getAllParameters().stream()
                    .map(NamedParameter::getName)
                    .map(Text::new)
                    .toList()),
                () -> vbox.getChildren().add(new Text("No parameters to display")));

        return vbox;
    }

    @Override
    public Node asNode() {
        return this.container;
    }
}