package gui;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import simulation.parameters.Parameter;

public class ParameterCheckNode implements NodeInterpretable {
    
    private final HBox checkBoxWrapper = new HBox();

    public ParameterCheckNode(final Parameter<Boolean> parameter) {
        final CheckBox checkBox = new CheckBox();
        checkBox.setIndeterminate(false);

        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            parameter.setValue(newValue);
        });

        checkBox.setDisable(!parameter.isEnabled());
        parameter.addEnableListener(enabled -> checkBox.setDisable(!enabled));

        this.checkBoxWrapper.getChildren().addAll(checkBox);
    }

    @Override
    public Node asNode() {
        return this.checkBoxWrapper;
    }
}
