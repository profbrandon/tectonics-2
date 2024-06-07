package simulation.display.parameters;

import java.util.function.Function;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import simulation.parameters.Parameter;

public class ParameterRenderer<T> {
    
    private final Function<T, Node> valueRenderer;

    private boolean abbreviate = false;

    public ParameterRenderer(final Function<T, Node> valueRenderer) {
        this.valueRenderer = valueRenderer;
    }

    public void setAbbreviate(final boolean abbreviate) {
        this.abbreviate = abbreviate;
    }

    public Node render(Parameter<T> parameter) {
        final HBox box = new HBox(4);

        box.getChildren().addAll(
            new Text(this.abbreviate ? parameter.getAbbreviation() : parameter.getName()),
            this.valueRenderer.apply(parameter.getValue()));

        return box;
    }
}
