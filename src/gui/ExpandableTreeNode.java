package gui;

import java.util.Collection;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import util.DistinguishedTree;

public class ExpandableTreeNode<N extends NodeInterpretable, A extends NodeInterpretable> extends DistinguishedTree<N, A> implements NodeInterpretable {

    private boolean isExpanded = false;

    private Runnable onExpandChange = () -> {};
    private final Node jNode;

    public ExpandableTreeNode(final A node) {
        super(node);
        this.jNode = this.createNode(false);
    }
    
    public ExpandableTreeNode(final N node, final Collection<ExpandableTreeNode<N, A>> subTrees, final boolean isRootHidden) {
        super(node, List.copyOf(subTrees));
        this.jNode = this.createNode(isRootHidden);

        if (isRootHidden) {
            this.isExpanded = true;
            this.onExpandChange.run();
            this.onExpandChange = () -> {};
        }
    }

    public ExpandableTreeNode(final N node, final Collection<ExpandableTreeNode<N, A>> subTrees) {
        this(node, subTrees, false);
    }

    private Node createNode(final boolean isRootHidden) {
        return this.getNode().match(
            branch -> {
                final VBox vBox = new VBox();
                final HBox hBox = new HBox();
                final VBox vBox2 = new VBox();
                final HBox hBox2 = new HBox();

                hBox2.setMinWidth(10);
                vBox.getChildren().add(branch.asNode());
                this.getSubTrees().forEach(subTree -> {
                    vBox2.getChildren().add(((ExpandableTreeNode<N, A>) subTree).asNode());
                });
                hBox.getChildren().addAll(hBox2, vBox2);

                onExpandChange = () -> {
                    synchronized(this) {
                        if (this.isExpanded) {
                            vBox.getChildren().add(hBox);
                        } else {
                            vBox.getChildren().remove(hBox);
                        }
                    }
                };

                branch.asNode().setOnMouseClicked(event -> {
                    this.isExpanded = !this.isExpanded;
                    onExpandChange.run();
                });

                return isRootHidden ? vBox2 : vBox;
            },
            leaf -> leaf.asNode());
    }

    @Override
    public Node asNode() {
        return this.jNode;
    }

    public void setExpanded(final boolean expanded) {
        this.isExpanded = expanded;
        onExpandChange.run();
    }
}
