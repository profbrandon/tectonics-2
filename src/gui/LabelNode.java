package gui;

import javafx.scene.Node;
import javafx.scene.text.Text;

public class LabelNode implements NodeInterpretable {

    private final Text text;

    public LabelNode(final String text) {
        this.text = new Text(text);
    }

    @Override
    public Node asNode() {
        return this.text;
    }
}
