package gui;

import java.util.function.Function;

import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import simulation.parameters.Parameter;

public class ParameterSliderNode<T extends Number> implements NodeInterpretable {
    
    private final HBox sliderWrapper;

    public ParameterSliderNode(final Parameter<T> parameter, final Function<Number, T> converter, final double increment, final double width) {
        final Text valueLabel = new Text(parameter.defaultValue().toString());
        final Slider slider = new Slider(
            parameter.minAllowableValue().doubleValue(),
            parameter.maxAllowableValue().doubleValue(),
            parameter.defaultValue().doubleValue());

        slider.setPrefWidth(width);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            final T value = converter.apply(Math.round(newValue.doubleValue() / increment) * increment);
            final String valueString = value.toString();
            
            parameter.setValue(value);
            valueLabel.setText(valueString.length() < 9 ? valueString : valueString.substring(0, 9));
        });

        this.sliderWrapper = new HBox(slider, valueLabel);
    }

    @Override
    public Node asNode() {
        return this.sliderWrapper;
    }
}
