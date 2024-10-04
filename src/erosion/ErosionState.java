package erosion;

import java.util.List;
import java.util.function.Function;

import simulation.SimulationState;
import util.data.algebraic.Prod;
import util.data.algebraic.Unit;
import util.math.MathUtil;

public class ErosionState implements SimulationState {

    private static final int WIDTH  = Erosion.WIDTH;
    private static final int HEIGHT = Erosion.HEIGHT;

    private final ErosionParameters parameters;

    private float[][] heights  = new float[WIDTH][HEIGHT];
    private float[][] sediment = new float[WIDTH][HEIGHT];
    private float[][] water    = new float[WIDTH][HEIGHT];
    
    private ErosionState(final ErosionParameters parameters, final boolean initialize) {
        this.parameters = parameters;

        if (initialize)
            setAllHeights(0f);
        setSediment(0f);
    }

    private ErosionState(final ErosionParameters parameters, final float initialHeight) {
        this.parameters = parameters;
        setAllHeights(initialHeight);
        setSediment(0f);
    }

    private void setSediment(final float value) {
        for (int i = 0; i < WIDTH; ++i)
            for (int j = 0; j < HEIGHT; ++j)
                sediment[i][j] = value;
    }

    public float[][] getHeights() {
        return heights;
    }

    public float[][] getSediment() {
        return this.sediment;
    }

    public float[][] getWater() {
        return this.water;
    }

    public void setAllHeights(float value) {
        for (int i = 0; i < WIDTH; ++i)
            for (int j = 0; j < HEIGHT; ++j)
                heights[i][j] = value;
    }

    public void perturb(float perturbation) {
        for (int i = 0; i < WIDTH; ++i)
            for (int j = 0; j < HEIGHT; ++j)
                heights[i][j] += perturbation * (float) (2.0 * Math.random() - 1.0);
    }

    public void flow(final float erodibility, final float transportFactor, final float carvingFactor) {
        float[][] deltaH = new float[WIDTH + 2][HEIGHT + 2];
        float[][] deltaS = new float[WIDTH + 2][HEIGHT + 2];
        float[][] deltaW = new float[WIDTH + 2][HEIGHT + 2];

        for (int i = 0; i < WIDTH + 2; ++i)
            for (int j = 0; j < HEIGHT + 2; ++j) {
                deltaH[i][j] = 0f;
                deltaS[i][j] = 0f;
                deltaW[i][j] = 0f;
            }

        for (int i = 1; i < WIDTH + 1; ++i) {
            for (int j = 1; j < HEIGHT + 1; ++j) {
                final int ci = i - 1;
                final int cj = j - 1;

                if (ci == 0 || ci == WIDTH - 1 || cj == 0 || cj == HEIGHT - 1)
                    continue;

                final float refHeight = heights[ci][cj];

                final List<Prod<Prod<Integer, Integer>, Float>> elements
                    = List.of(
                            //Prod.pair(ci - 1, cj - 1),
                            Prod.pair(ci - 1, cj),
                            //Prod.pair(ci - 1, cj + 1),
                            Prod.pair(ci, cj - 1),
                            Prod.pair(ci, cj + 1),
                            //Prod.pair(ci + 1, cj - 1),
                            Prod.pair(ci + 1, cj)
                            //Prod.pair(ci + 1, cj + 1)
                            )
                        .stream()
                        .map(pair -> Prod.destroy(pair, c -> r -> 
                            Prod.pair(pair, heights[c][r] - refHeight)))
                        .toList();
                    
                final List<Prod<Prod<Integer, Integer>, Float>> below
                    = elements
                        .stream()
                        .filter(pair -> Prod.destroy(pair, __ -> relative -> relative < 0))
                        .sorted((a, b) -> a.second() > b.second() ? -1 : 1)
                        .toList();

                final int n = below.size();

                // Filling a hole
                if (n == 0) {
                    deltaH[i][j] += sediment[ci][cj];
                    deltaS[i][j] -= sediment[ci][cj];
                    continue;
                }

                final Prod<Prod<Integer, Integer>, Float> lowest = below.get(below.size() - 1);
                final float aggregateDrop = below
                    .stream()
                    .map(p -> p.second())
                    .reduce(0f, (a, b) -> a + b);

                // A valley
                if (n == 1 || carvingFactor * lowest.second() < (aggregateDrop - lowest.second())) {
                    final float drop       = - lowest.second();
                    final float erosion    = (float) (Math.sqrt(erodibility) + MathUtil.squeeze(10f * drop)) * drop / 2;
                    final float transport  = (float) (Math.sqrt(transportFactor) + MathUtil.squeeze(1 / (drop + 1))) * sediment[ci][cj] / 2;
                    final float deposition = sediment[ci][cj] - transport;

                    final int di = lowest.first().first() + 1;
                    final int dj = lowest.first().second() + 1;

                    deltaS[di][dj]         += transport + erosion;

                    deltaH[di][dj] += deposition;
                    deltaH[ci + 1][cj + 1] -= erosion;
                    deltaS[ci + 1][cj + 1] -= sediment[ci][cj];
                } 
                
                else if (n > 1) {
                    final float transport  = sediment[ci][cj] * (1 - MathUtil.squeeze(sediment[ci][cj] + 1f) + transportFactor) / 2.0f;
                    final float deposition = sediment[ci][cj] - transport;
                    //final float erosion    = - (float)Math.pow(erodibility, 4) * lowest.second();

                    //deltaH[i][j] -= erosion;
                    deltaS[i][j] -= sediment[ci][cj];

                    below.forEach(pair -> pair.destroy(loc -> drop -> {
                        final int di = loc.first() + 1;
                        final int dj = loc.second() + 1;

                        deltaH[di][dj] += (deposition) / n;
                        deltaS[di][dj] += transport / n;

                        return Unit.unit();
                    }));
                }
            }
        }

        applyChanges(deltaH, heights);
        applyChanges(deltaS, sediment);
        applyChanges(deltaW, water);
    }

    public void blur(float[][] mask) {
        float[][] deltas = new float[WIDTH + 2][HEIGHT + 2];

        for (int i = 0; i < WIDTH + 2; ++i)
            for (int j = 0; j < HEIGHT + 2; ++j)
                deltas[i][j] = 0f;

        for (int i = 1; i < WIDTH + 1; ++i) {
            for (int j = 1; j < HEIGHT + 1; ++j) {
                float temp = mask[1][1] * heights[i - 1][j - 1];

                deltas[i - 1][j - 1] += mask[0][0] * temp;
                deltas[i - 1][j]     += mask[0][1] * temp;
                deltas[i - 1][j + 1] += mask[0][2] * temp;

                deltas[i][j - 1]     += mask[1][0] * temp;
                deltas[i][j]         -= temp;
                deltas[i][j + 1]     += mask[1][2] * temp;
                
                deltas[i + 1][j - 1] += mask[2][0] * temp;
                deltas[i + 1][j]     += mask[2][1] * temp;
                deltas[i + 1][j + 1] += mask[2][2] * temp;
            }
        }

        for (int i = 0; i < WIDTH; ++i)
            for (int j = 0; j < HEIGHT; ++j)
                heights[i][j] += deltas[i + 1][j + 1];

        for (int i = 0; i < WIDTH; ++i) {
            heights[i][0] += deltas[i + 1][HEIGHT + 1];
            heights[i][HEIGHT - 1] += deltas[i + 1][0];
        }

        for (int j = 0; j < HEIGHT; ++j) {
            heights[0][j] += deltas[WIDTH + 1][j + 1];
            heights[WIDTH - 1][j] += deltas[0][j + 1]; 
        }

        heights[0][0]                  += deltas[WIDTH + 1][HEIGHT + 1];
        heights[WIDTH - 1][0]          += deltas[0][HEIGHT + 1];
        heights[0][HEIGHT - 1]         += deltas[WIDTH + 1][0];
        heights[WIDTH - 1][HEIGHT - 1] += deltas[0][0];

    }

    public void evaporate(final float maxAmount) {
        for (int i = 0; i < WIDTH; ++i)
            for (int j = 0; j < HEIGHT; ++j)
                water[i][j] -= Math.min(maxAmount, water[i][j]);
    }

    public void precipitate(final float amount, final float heightDependence) {
        
    }

    public void applyChanges(final float[][] delta, final float[][] value) {
        for (int i = 0; i < WIDTH; ++i)
            for (int j = 0; j < HEIGHT; ++j) {
                value[i][j]  += delta[i + 1][j + 1];
            }

        for (int i = 0; i < WIDTH; ++i) {
            value[i][0]          += delta[i + 1][HEIGHT + 1];
            value[i][HEIGHT - 1] += delta[i + 1][0];
        }

        for (int j = 0; j < HEIGHT; ++j) {
            value[0][j]         += delta[WIDTH + 1][j + 1];
            value[WIDTH - 1][j] += delta[0][j + 1]; 
        }

        value[0][0]                  += delta[WIDTH + 1][HEIGHT + 1];
        value[WIDTH - 1][0]          += delta[0][HEIGHT + 1];
        value[0][HEIGHT - 1]         += delta[WIDTH + 1][0];
        value[WIDTH - 1][HEIGHT - 1] += delta[0][0];
    }

    public static ErosionState erosionState(final ErosionParameters parameters, final float initialHeight) {
        return new ErosionState(parameters, initialHeight);
    }

    public static ErosionState randomErosionState(final ErosionParameters parameters, final float initialHeight, final float perturbation) {
        final ErosionState erosionState = new ErosionState(parameters, initialHeight);
        erosionState.perturb(perturbation);
        return erosionState;
    }

    public static ErosionState fromHeightFunction(final ErosionParameters parameters,  final Function<Integer, Function<Integer, Float>> heightFunction) {
        final ErosionState erosionState = new ErosionState(parameters, false);

        for (int i = 0; i < WIDTH; ++i)
            for (int j = 0; j < HEIGHT; ++j)
                erosionState.heights[i][j] = heightFunction.apply(j).apply(i);

        return erosionState;
    }
    
    @Override
    public void evolve() {
        final float erodibility  = parameters.getErodibility();
        final float transport    = parameters.getSedimentTransport();
        final float blurS        = parameters.getBlurStrength();
        final float perturbation = parameters.getNoiseStrength();
        final float carving      = parameters.getCarvingFactor();

        try {
            if (erodibility != 0f || transport != 0f)
                flow(erodibility, transport, carving);

            if (blurS != 0f)
                blur(new float[][] {
                    new float[]{ 0.10f, 0.15f, 0.10f },
                    new float[]{ 0.15f, blurS, 0.15f },
                    new float[]{ 0.10f, 0.15f, 0.10f }
                });

            if (perturbation != 0f)
                perturb(perturbation);
    
            double heightSum   = 0.0f;
            double sedimentSum = 0.0f;

            float minHeight = heights[0][0];
            float maxHeight = heights[0][0];

            float minSediment = sediment[0][0];
            float maxSediment = sediment[0][0];

            for (int i = 0; i < WIDTH; ++i)
                for (int j = 0; j < HEIGHT; ++j) {
                    heightSum += heights[i][j];
                    sedimentSum += sediment[i][j];

                    if (minHeight > heights[i][j])
                        minHeight = heights[i][j];
                    
                    if (maxHeight < heights[i][j])
                        maxHeight = heights[i][j];

                    if (minSediment > sediment[i][j])
                        minSediment = sediment[i][j];
                    
                    if (maxSediment < sediment[i][j])
                        maxSediment = sediment[i][j];
                }

            System.out.print("| height: " + heightSum + ", sediment: " + sedimentSum + ", total: " + (heightSum + sedimentSum) 
                + ", min/max height: (" + minHeight + ", " + maxHeight + ")"
                + ", min/max sediment: (" + minSediment + ", " + maxSediment + ")");
    

        } catch (final Exception e) {
            System.out.println(e);
        }
    }

}
