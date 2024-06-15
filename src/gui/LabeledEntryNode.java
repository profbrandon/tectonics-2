package gui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class LabeledEntryNode implements NodeInterpretable {

    private final HBox hBox = new HBox();

    public LabeledEntryNode(final String text, final double textWidth, final Node entry) {
        this(text, text, text, textWidth, entry);
    }

    public LabeledEntryNode(final String text, final String tooltip, final double textWidth, final Node entry) {
        this(text, text, tooltip, textWidth, entry);
    }

    public LabeledEntryNode(final String text, final String abbreviation, final String tooltip, final double textWidth, final Node entry) {
        String textValue = text;
        double realTextWidth = getTextWidth(textValue);

        if (realTextWidth > textWidth) {
            textValue = abbreviation;
            realTextWidth = getTextWidth(textValue);

            if (realTextWidth > textWidth) {
                do {
                    textValue = textValue.substring(0, textValue.length() - 2);
                    realTextWidth = getTextWidth(textValue + "...");
                } while (realTextWidth > textWidth && textValue.length() > 2);

                textValue = textValue + "...";
            }
        }

        final HBox textWrapper = new HBox();
        textWrapper.getChildren().add(new Text(textValue));
        textWrapper.setPrefWidth(textWidth);

        HBox.setHgrow(textWrapper, Priority.SOMETIMES);
        HBox.setHgrow(entry, Priority.ALWAYS);

        this.hBox.getChildren().addAll(textWrapper, entry);
        this.hBox.setBorder(Border.stroke(Color.LIGHTGRAY));
        
        Tooltip.install(this.hBox, new Tooltip(tooltip));
        this.hBox.setPadding(new Insets(2));
    }

    private double getTextWidth(final String text) {
        return new Text(text).getLayoutBounds().getWidth();
    }

    @Override
    public Node asNode() {
        return this.hBox;
    }
}
