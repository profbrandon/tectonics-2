package gui;

import java.util.function.Function;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import simulation.parameters.Parameter;

public class ParameterTextFieldNode<T extends CharSequence> implements NodeInterpretable {

    private final HBox textFieldWrapper = new HBox();

    public ParameterTextFieldNode(final Parameter<T> parameter, final Function<String, T> converter, final double width) {
        final TextField textField = new TextField(parameter.defaultValue().toString());
        textField.setPrefHeight(20);
        textField.setFont(Font.font(10));

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            parameter.setValue(converter.apply(newValue));
        });

        textField.setDisable(!parameter.isEnabled());
        parameter.addEnableListener(enabled -> textField.setDisable(!enabled));

        this.textFieldWrapper.getChildren().add(textField);
        this.textFieldWrapper.setPrefWidth(width);
    }

    @Override
    public Node asNode() {
        return this.textFieldWrapper;
    }
}
