package gui;

import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class LabelNode implements NodeInterpretable {

    private final HBox hBox = new HBox();

    public LabelNode(final String text, final double width) {
        this(text, text, text, width);
    }

    public LabelNode(final String text, final String tooltip, final double width) {
        this(text, text, tooltip, width);
    }

    public LabelNode(final String text, final String abbreviation, final String tooltip, final double width) {
        String textValue = text;
        double textWidth = getTextWidth(textValue);

        if (textWidth > width) {
            textValue = abbreviation;
            textWidth = getTextWidth(textValue);

            if (textWidth > width) {
                do {
                    textValue = textValue.substring(0, textValue.length() - 2);
                    textWidth = getTextWidth(textValue + "...");
                } while (textWidth > width && textValue.length() > 2);

                textValue = textValue + "...";
            }
        }

        this.hBox.getChildren().add(new Text(textValue));
        this.hBox.setPrefWidth(width);
        this.hBox.setBorder(Border.stroke(Color.LIGHTGRAY));
        
        Tooltip.install(this.hBox, new Tooltip(tooltip));
    }

    private double getTextWidth(final String text) {
        return new Text(text).getLayoutBounds().getWidth();
    }

    @Override
    public Node asNode() {
        return this.hBox;
    }
}
