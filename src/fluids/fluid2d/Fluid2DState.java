package fluids.fluid2d;

import simulation.SimulationState;

public class Fluid2DState implements SimulationState {

    private final int width;
    private final int height;

    private final float[][] density;

    public Fluid2DState(final int width, final int height) {
        this.width = width;
        this.height = height;
        this.density = new float[this.width][this.height];

        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                this.density[i][j] = (i + j) % 2 == 0 ? 1f : 0f;
            }
        }
    }

    public float[][] getDensity() {
        return this.density;
    }

    @Override
    public void evolve() {

    }
}
