package erosion;

import java.util.List;
import java.util.function.Function;

import util.data.algebraic.Prod;

public class ErosionState {

    private static final int WIDTH = ErosionSimulation.WIDTH;
    private static final int HEIGHT = ErosionSimulation.HEIGHT;

    private float[][] heights = new float[WIDTH][HEIGHT];

    private float[][] sediment = new float[WIDTH][HEIGHT];
    
    private ErosionState(boolean initialize) {
        if (initialize) {
            setAllHeights(0f);
        }
    }

    private ErosionState(float initialHeight) {
        setAllHeights(initialHeight);
    }

    public void evolve() {
        try {
            flow(0.5f);
            
            blur(new float[][] {
                new float[]{ 0.10f, 0.15f, 0.10f },
                new float[]{ 0.15f, 0.01f, 0.15f },
                new float[]{ 0.10f, 0.15f, 0.10f }
            });
            //perturb(0.001f);
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

    public void landslide(float slopeThreshold, float param) {
        float[][] deltas = new float[WIDTH + 2][HEIGHT + 2];

        for (int i = 0; i < WIDTH + 2; ++i)
            for (int j = 0; j < HEIGHT + 2; ++j)
                deltas[i][j] = 0f;

        for (int i = 1; i < WIDTH + 1; ++i) {
            for (int j = 1; j < HEIGHT + 1; ++j) {
                final int ci = i - 1;
                final int cj = j - 1;
                final float refHeight = heights[ci][cj];

                if (ci > 0 && ci < WIDTH - 1 && cj > 0 && cj < HEIGHT - 1) {

                    final float temp1 = refHeight - heights[ci - 1][cj];
                    final float temp2 = refHeight - heights[ci + 1][cj];
                    final float temp3 = refHeight - heights[ci][cj - 1];
                    final float temp4 = refHeight - heights[ci][cj + 1];

                    if (temp1 > slopeThreshold) deltas[i - 1][j] += temp1 * param;
                    if (temp2 > slopeThreshold) deltas[i + 1][j] += temp2 * param;
                    if (temp3 > slopeThreshold) deltas[i][j - 1] += temp3 * param;
                    if (temp4 > slopeThreshold) deltas[i][j + 1] += temp4 * param;

                    final float aggregate = 
                        List.of(temp1, temp2, temp3, temp4)
                            .stream()
                            .filter(v -> v > 0)
                            .reduce(0f, (f1, f2) -> f1 + f2);

                    deltas[i][j] -= aggregate * param;
                }
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

    public void flow(float param) {
        float[][] deltas = new float[WIDTH + 2][HEIGHT + 2];

        for (int i = 0; i < WIDTH + 2; ++i)
            for (int j = 0; j < HEIGHT + 2; ++j)
                deltas[i][j] = 0f;

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
                    .filter(pair -> Prod.destroy(pair, __ -> relative -> relative < 0))
                    .sorted((a, b) -> a.second() > b.second() ? -1 : 1)
                    .toList();

                if (elements.size() > 1) {
                    final float toRemove = - param * elements.get(0).second();
                    final Prod<Prod<Integer, Integer>, Float> lowest = elements.get(elements.size() - 1);

                    deltas[i][j] -= toRemove;
                    deltas[lowest.first().first() + 1][lowest.first().second() + 1] += 0.1f * toRemove;

                    elements
                        .stream()
                        .skip(1)
                        .forEach(v -> {
                            deltas[v.first().first() + 1][v.first().second() + 1] += 0.7f * toRemove / (elements.size() - 1);
                        });
                } else if (elements.size() == 1) {
                    final Prod<Prod<Integer, Integer>, Float> lowest = elements.get(0);
                    final float toRemove = - (float)Math.sqrt(param) * lowest.second();

                    deltas[i][j] -= toRemove;
                    //deltas[lowest.first().first() + 1][lowest.first().second() + 1] += toRemove;
                }
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

    public void fillLows(float param) {
        float[][] deltas = new float[WIDTH + 2][HEIGHT + 2];

        for (int i = 0; i < WIDTH + 2; ++i)
            for (int j = 0; j < HEIGHT + 2; ++j)
                deltas[i][j] = 0f;

        for (int i = 1; i < WIDTH + 1; ++i) {
            for (int j = 1; j < HEIGHT + 1; ++j) {
                final int ci = i - 1;
                final int cj = j - 1;
                final float refHeight = heights[ci][cj];

                if (ci > 0 && ci < WIDTH - 1 && cj > 0 && cj < HEIGHT - 1) {

                    final float temp1 = refHeight - heights[ci - 1][cj];
                    final float temp2 = refHeight - heights[ci + 1][cj];
                    final float temp3 = refHeight - heights[ci][cj - 1];
                    final float temp4 = refHeight - heights[ci][cj + 1];

                    if (temp1 > 0) deltas[i - 1][j] += temp1 * param;
                    if (temp2 > 0) deltas[i + 1][j] += temp2 * param;
                    if (temp3 > 0) deltas[i][j - 1] += temp3 * param;
                    if (temp4 > 0) deltas[i][j + 1] += temp4 * param;

                    final float aggregate = 
                        List.of(temp1, temp2, temp3, temp4)
                            .stream()
                            .filter(v -> v > 0)
                            .reduce(0f, (f1, f2) -> f1 + f2);

                    deltas[i][j] -= aggregate * param;
                }
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

    public static ErosionState erosionState(float initialHeight) {
        return new ErosionState(initialHeight);
    }

    public static ErosionState randomErosionState(float initialHeight, float perturbation) {
        final ErosionState erosionState = new ErosionState(initialHeight);
        erosionState.perturb(perturbation);
        return erosionState;
    }

    public static ErosionState fromHeightFunction(Function<Integer, Function<Integer, Float>> heightFunction) {
        final ErosionState erosionState = new ErosionState(false);

        for (int i = 0; i < WIDTH; ++i)
            for (int j = 0; j < HEIGHT; ++j)
                erosionState.heights[i][j] = heightFunction.apply(j).apply(i);

        return erosionState;
    }
}
