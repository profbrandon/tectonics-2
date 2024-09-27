package erosion;

import java.util.List;
import java.util.function.Function;

import util.data.algebraic.Prod;

public class ErosionState {

    private static final int WIDTH = ErosionSimulation.WIDTH;
    private static final int HEIGHT = ErosionSimulation.HEIGHT;

    private final ErosionParameters parameters;

    private float[][] heights = new float[WIDTH][HEIGHT];

    private float[][] sediment = new float[WIDTH][HEIGHT];
    
    private ErosionState(final ErosionParameters parameters, final boolean initialize) {
        this.parameters = parameters;

        if (initialize) {
            setAllHeights(0f);
        }

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

    public void evolve() {
        try {
            final float blurS = parameters.getBlurStrength();

            if (parameters.getBlurEnabled())
                blur(new float[][] {
                    new float[]{ 0.10f, 0.15f, 0.10f },
                    new float[]{ 0.15f, blurS, 0.15f },
                    new float[]{ 0.10f, 0.15f, 0.10f }
                });

            if (parameters.getNoiseEnabled()) {
                perturb(parameters.getNoiseStrength());
            }

            double heightSum = 0.0f;
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

    public float[][] getHeights() {
        return heights;
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

    public void flow(final float erodibility, final float transportFactor) {
        float[][] deltaH = new float[WIDTH + 2][HEIGHT + 2];
        float[][] deltaS = new float[WIDTH + 2][HEIGHT + 2];

        for (int i = 0; i < WIDTH + 2; ++i)
            for (int j = 0; j < HEIGHT + 2; ++j) {
                deltaH[i][j] = 0f;
                deltaS[i][j] = 0f;
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
                        .map(pair -> Prod.destroy(pair, c -> r -> Prod.pair(pair, heights[c][r] - refHeight)))
                        .toList();
                    
                final List<Prod<Prod<Integer, Integer>, Float>> below
                    = elements
                        .stream()
                        .filter(pair -> Prod.destroy(pair, __ -> relative -> relative < 0))
                        .sorted((a, b) -> a.second() > b.second() ? -1 : 1)
                        .toList();

                // Filling a hole
                if (below.size() == 0) {
                    deltaH[i][j] += sediment[ci][cj];
                    deltaS[i][j] -= sediment[ci][cj];
                }
                else if (below.size() == 1) {
                    final Prod<Prod<Integer, Integer>, Float> lowest = below.get(0);

                    final float drop       = - lowest.second();
                    final float erosion    = (float) erodibility * drop;
                    final float transport  = (float) transportFactor * sediment[ci][cj];
                    final float deposition = sediment[ci][cj] - transport;

                    final int di = lowest.first().first() + 1;
                    final int dj = lowest.first().second() + 1;

                    deltaS[di][dj]         += transport + erosion;

                    deltaH[ci + 1][cj + 1] += deposition;
                    deltaH[ci + 1][cj + 1] -= erosion;
                    deltaS[ci + 1][cj + 1] -= sediment[ci][cj];
                } 
                else if (below.size() >= 2) {
                    final int n = below.size();

                    final float aggregateDrop
                        = - below
                            .stream()
                            .map(v -> v.second())
                            .reduce(0f, (v1, v2) -> v1 + v2);

                    final float slumpFactor = squeeze(aggregateDrop);

                    final float aggregateErosion
                        = erodibility * erodibility * aggregateDrop / n;

                    final float aggregateTransport
                        = slumpFactor * transportFactor * sediment[ci][cj];

                    final float aggregateDeposition
                        = sediment[ci][cj] - aggregateTransport;

                    below.forEach(
                        v -> {
                            final float drop  = - v.second();
                            final int di      = v.first().first() + 1;
                            final int dj      = v.first().second() + 1;

                            final float dropFactor = drop / aggregateDrop;

                            final float transport  = aggregateTransport * dropFactor;
                            final float deposition = aggregateDeposition * dropFactor; 
                            final float erosion    = aggregateErosion * dropFactor;
                            
                            deltaS[di][dj]         += transport + erosion;
                            deltaH[ci + 1][cj + 1] += deposition;
                        }
                    );

                    deltaS[ci + 1][cj + 1] -= sediment[ci][cj];
                    deltaH[ci + 1][cj + 1] -= aggregateErosion;
                }
            }
        }

        for (int i = 0; i < WIDTH; ++i)
            for (int j = 0; j < HEIGHT; ++j) {
                heights[i][j] += deltaH[i + 1][j + 1];
                sediment[i][j] += deltaS[i + 1][j + 1];
            }

        for (int i = 0; i < WIDTH; ++i) {
            heights[i][0]          += deltaH[i + 1][HEIGHT + 1];
            heights[i][HEIGHT - 1] += deltaH[i + 1][0];

            sediment[i][0]          += deltaS[i + 1][HEIGHT + 1];
            sediment[i][HEIGHT - 1] += deltaS[i + 1][0];
        }

        for (int j = 0; j < HEIGHT; ++j) {
            heights[0][j]         += deltaH[WIDTH + 1][j + 1];
            heights[WIDTH - 1][j] += deltaH[0][j + 1]; 

            sediment[0][j]         += deltaS[WIDTH + 1][j + 1];
            sediment[WIDTH - 1][j] += deltaS[0][j + 1]; 
        }

        heights[0][0]                  += deltaH[WIDTH + 1][HEIGHT + 1];
        heights[WIDTH - 1][0]          += deltaH[0][HEIGHT + 1];
        heights[0][HEIGHT - 1]         += deltaH[WIDTH + 1][0];
        heights[WIDTH - 1][HEIGHT - 1] += deltaH[0][0];

        sediment[0][0]                  += deltaS[WIDTH + 1][HEIGHT + 1];
        sediment[WIDTH - 1][0]          += deltaS[0][HEIGHT + 1];
        sediment[0][HEIGHT - 1]         += deltaS[WIDTH + 1][0];
        sediment[WIDTH - 1][HEIGHT - 1] += deltaS[0][0];
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

    private static float squeeze(final float f) {
        return (float) (Math.atan(f) / Math.PI * 2.0f);
    }
}
