package fluids.fluid2d;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import simulation.SimulationState;
import util.data.algebraic.Prod;
import util.math.MathUtil;

public class Fluid2DState implements SimulationState {

    private final int width;
    private final int height;

    private AtomicReference<float[][]> prevDensity;
    private AtomicReference<float[][]> density;

    private AtomicReference<float[][]> prevVelocityX;
    private AtomicReference<float[][]> prevVelocityY;
    private AtomicReference<float[][]> velocityX;
    private AtomicReference<float[][]> velocityY;

    private AtomicReference<float[][]> forceX;
    private AtomicReference<float[][]> forceY;

    private final Fluid2DParameters parameters;

    public Fluid2DState(final int width, final int height, final Fluid2DParameters parameters) {
        this.width = width;
        this.height = height;

        this.parameters = parameters;

        this.prevDensity = new AtomicReference<>(new float[this.width][this.height]);
        this.density     = new AtomicReference<>(new float[this.width][this.height]);

        this.prevVelocityX = new AtomicReference<>(new float[this.width][this.height]);
        this.prevVelocityY = new AtomicReference<>(new float[this.width][this.height]);
        this.velocityX     = new AtomicReference<>(new float[this.width][this.height]);
        this.velocityY     = new AtomicReference<>(new float[this.width][this.height]);

        this.forceX = new AtomicReference<>(new float[this.width][this.height]);
        this.forceY = new AtomicReference<>(new float[this.width][this.height]);

        for (int i = 1; i < this.width - 1; ++i)
            for (int j = 1; j < this.height - 1; ++j) {
                //this.density[i][j] = j < this.height / 2 ? 0.5f + (float)Math.random() * 0.1f : 0f;

                //this.source.get()[i][j] = (j == 1 && Math.abs(i - this.width / 2) < 20) ? 100f : 0f;
                //this.prevVelocityX.get()[i][j] = (j > 200 && j < 300) ? 100.0f * (1.0f - (float)Math.abs(j - this.height / 2) / this.height) : 0f;

                this.forceY.get()[i][j] = (j < 20) ? 0.1f * (20 - j) : (j > this.height - 21) ? -0.1f * (j - this.height + 20) : 0f;
                this.forceY.get()[i][j] += -0.01f;
            }

        this.prevDensity.get()[250][250] = 10f;
    }

    public float[][] getDensity() {
        return this.density.get();
    }

    public Prod<float[][], float[][]> getVelocity() {
        return Prod.pair(this.velocityX.get(), this.velocityY.get());
    }

    private void addSource(
        final AtomicReference<float[][]> updated, 
        final AtomicReference<float[][]> sources, 
        final float dt) {

        for (int i = 0; i < this.width; ++i)
            for (int j = 0; j < this.height; ++j)
                updated.get()[i][j] += dt * sources.get()[i][j];
    }

    private void diffuse(
        final int b, 
        final AtomicReference<float[][]> updated, 
        final AtomicReference<float[][]> original, 
        final float diffusion, 
        final float dt) {
        
        final float a = dt * diffusion * (this.width - 1);// * (this.height - 1);

        for (int k = 0; k < 20; ++k)
            for (int i = 1; i < this.width - 1; ++i)
                for (int j = 1; j < this.height - 1; ++j)
                    updated.get()[i][j] = (original.get()[i][j] + a * (updated.get()[i][j - 1] + updated.get()[i][j + 1] + updated.get()[i - 1][j] + updated.get()[i + 1][j])) / (1 + 4 * a);

        setBoundary(updated, b);
    }

    private void advect(
        final int b, 
        final AtomicReference<float[][]> updated, 
        final AtomicReference<float[][]> original, 
        final AtomicReference<float[][]> velX, 
        final AtomicReference<float[][]> velY, 
        final float dt) {

        final float w = this.width - 1;
        final float h = this.height - 1;
        final float dt0 = dt * (this.width - 1);

        final Function<Prod<Float, Float>, Float> interpolator = MathUtil.interpolate(original.get());

        for (int i = 1; i < w; ++i)
            for (int j = 1; j < h; ++j) {
                float x = i - dt0 * velX.get()[i][j];
                float y = j - dt0 * velY.get()[i][j];

                if (x < 0.5)
                    x = 0.5f;

                if (x > w - 0.5) 
                    x = w - 0.5f;

                if (y < 0.5)     
                    y = 0.5f;

                if (y > h - 0.5)
                    y = h - 0.5f;

                updated.get()[i][j] = interpolator.apply(Prod.pair(x, y));
            }

        setBoundary(updated, b);
    }

    private void swap(final AtomicReference<float[][]> a, final AtomicReference<float[][]> b) {
        final AtomicReference<float[][]> temp = new AtomicReference<>(a.get());
        a.set(b.get());
        b.set(temp.get());
    }

    private void setBoundary(final AtomicReference<float[][]> updated, final int b) {
        for (int i = 1; i < this.width - 1; ++i) {
            updated.get()[i][0]               = b == 2 ? - updated.get()[i][1] : updated.get()[i][1];
            updated.get()[i][this.height - 1] = b == 2 ? - updated.get()[i][this.height - 2] : updated.get()[i][this.height - 2];
        }

        for (int j = 1; j < this.height - 1; ++j) {
            updated.get()[0][j]              = b == 1 ? - updated.get()[1][j] : updated.get()[1][j];
            updated.get()[this.width - 1][j] = b == 1 ? - updated.get()[this.width - 2][j] : updated.get()[this.width - 2][j];
        }

        updated.get()[0]             [0]               = (updated.get()[1][0] + updated.get()[0][1]) / 2;
        updated.get()[0]             [this.height - 1] = (updated.get()[1][this.height - 1] + updated.get()[0][this.height - 2]) / 2;
        updated.get()[this.width - 1][0]               = (updated.get()[this.width - 2][0] + updated.get()[this.width - 1][1]) / 2;
        updated.get()[this.width - 1][this.height - 1] = (updated.get()[this.width - 2][this.height - 1] + updated.get()[this.width - 1][this.height - 2]) / 2;
    }

    private void densitySolver(
        final AtomicReference<float[][]> updated, 
        final AtomicReference<float[][]> original, 
        final AtomicReference<float[][]> velX, 
        final AtomicReference<float[][]> velY, 
        final float diff, 
        final float dt) {
        
        addSource(updated, original, dt);
        swap(original, updated);
        diffuse(0, updated, original, diff, dt);
        swap(original, updated);
        advect(0, updated, original, velX, velY, dt);
    }

    private void velocitySolver(
        final AtomicReference<float[][]> newVelX, 
        final AtomicReference<float[][]> newVelY, 
        final AtomicReference<float[][]> velX, 
        final AtomicReference<float[][]> velY, 
        final AtomicReference<float[][]> density,
        final float visc, 
        final float diff,
        final float dt) {

        addSource(newVelX, velX, dt);
        addSource(newVelY, velY, dt);
        
        swap(velX, newVelX);
        diffuse(1, newVelX, velX, visc, dt);
        swap(velY, newVelY);
        diffuse(2, newVelY, velY, visc, dt);
        project(newVelX, newVelY, velX, velY);

        swap(newVelX, velX);
        swap(newVelY, velY);

        advect(1, newVelX, velX, velX, velY, dt);
        advect(2, newVelY, velY, velX, velY, dt);
        project(newVelX, newVelY, velX, velY);
    }

    private void project(
        final AtomicReference<float[][]> velX,
        final AtomicReference<float[][]> velY, 
        final AtomicReference<float[][]> grad, 
        final AtomicReference<float[][]> div) {

        final float h = 1.0f / (this.width - 2);

        for (int i = 1; i < this.width - 1; ++i) 
            for (int j = 1; j < this.height - 1; ++j) {
                grad.get()[i][j] = 0f;
                div.get()[i][j]  = - h * (velX.get()[i + 1][j] - velX.get()[i - 1][j] + velY.get()[i][j + 1] - velY.get()[i][j - 1]) / 2f;
            }

        setBoundary(grad, 0);
        setBoundary(div, 0);

        for (int k = 0; k < 20; ++k) {
            for (int i = 1; i < this.width - 1; ++i) 
                for (int j = 1; j < this.height - 1; ++j)
                    grad.get()[i][j] = (div.get()[i][j] + grad.get()[i - 1][j] + grad.get()[i + 1][j] + grad.get()[i][j - 1] + grad.get()[i][j + 1]) / 4f;

            setBoundary(grad, 0);
        }

        for (int i = 1; i < this.width - 1; ++i) 
            for (int j = 1; j < this.height - 1; ++j) {
                velX.get()[i][j] -= (grad.get()[i + 1][j] - grad.get()[i - 1][j]) / 2f / h;
                velY.get()[i][j] -= (grad.get()[i][j + 1] - grad.get()[i][j - 1]) / 2f / h;
            }

        setBoundary(velX, 1);
        setBoundary(velY, 2);
    }

    private void plusEquals(final AtomicReference<float[][]> a, final AtomicReference<float[][]> b) {
        for (int i = 0; i < this.width; ++i)
            for (int j = 0; j < this.height; ++j)
                a.get()[i][j] += b.get()[i][j];
    }

    private AtomicReference<float[][]> copy(final AtomicReference<float[][]> a) {
        final AtomicReference<float[][]> temp = new AtomicReference<>(new float[this.width][this.height]);

        for (int i = 0; i < this.width; ++i)
            for (int j = 0; j < this.height; ++j)
                temp.get()[i][j] = a.get()[i][j];
        
        return temp;
    }

    @Override
    public void evolve() {
        swap(prevVelocityY, copy(forceY));

        velocitySolver(
            velocityX,
            velocityY, 
            prevVelocityX, 
            prevVelocityY, 
            prevDensity,
            parameters.getViscosity(), 
            parameters.getDiffusion(),
            0.01f);
        densitySolver(
            density, 
            prevDensity, 
            velocityX, 
            velocityY, 
            parameters.getDiffusion(), 
            0.01f);

        //swap(prevDensity, density);
        //swap(prevVelocityY, velocityY);
/*
        float densitySum = 0.0f;
        float velXSum    = 0.0f;
        float velYSum    = 0.0f;

        for (int i = 0; i < this.width; ++i)
            for (int j = 0; j < this.height; ++j) {
                densitySum += this.density.get()[i][j];
                velXSum += this.velocityX.get()[i][j];
                velYSum += this.velocityY.get()[i][j];
            }

        System.out.print(" | " + densitySum + ", (" + velXSum + ", " + velYSum + ")");*/
    }
}
