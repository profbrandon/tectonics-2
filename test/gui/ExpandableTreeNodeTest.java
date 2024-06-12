package test.gui;

import java.util.List;

import gui.ExpandableTreeNode;
import gui.NodeInterpretable;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ExpandableTreeNodeTest extends Application {
    @Override
    public void start(final Stage stage) throws Exception {
        final Group root = new Group();
        final Scene scene = new Scene(root);

        final ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefWidth(200);
        scrollPane.setPrefHeight(300);

        final ExpandableTreeNode<TextNode, TextNode> tree = new ExpandableTreeNode<>(
            new TextNode("Top Level"),
            List.of(
                new ExpandableTreeNode<>(new TextNode("Category 1"), 
                    List.of(
                        new ExpandableTreeNode<>(new TextNode("have you ever heard the wind blow?")),
                        new ExpandableTreeNode<>(new TextNode("we must be swift as a coursing river"))
                    )),
                new ExpandableTreeNode<>(new TextNode("Category 2"),
                    List.of(
                        new ExpandableTreeNode<>(new TextNode(".......................................................................")),
                        new ExpandableTreeNode<>(new TextNode("Category 2.1"),
                            List.of(
                                new ExpandableTreeNode<>(new TextNode("Boop Bop")),
                                new ExpandableTreeNode<>(new TextNode("Zip Zap"))))
                    )),
                new ExpandableTreeNode<>(new TextNode("Category 3"))),
            false);

        tree.setExpanded(true);

        scrollPane.setContent(tree.asNode());
        root.getChildren().add(scrollPane);

        stage.setScene(scene);
        stage.show();
    }

    private class TextNode implements NodeInterpretable {

        private final Text node;

        public TextNode(final String string) {
            this.node = new Text(string);
        }

        @Override
        public Node asNode() {
            return this.node;
        }
    }
    
}
