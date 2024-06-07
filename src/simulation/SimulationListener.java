package simulation;

import javafx.scene.image.WritableImage;

public interface SimulationListener {
    public void postFrame(WritableImage image);
}
