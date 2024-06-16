package chevy.view.animation;

import chevy.service.Render;
import chevy.service.RenderManager;

public class Interpolate implements Render {
    private double time = 0d;
    private boolean isRunning = false;
    private final InterpolationTypes interpolationTypes;
    private final double start;
    private final double end;
    private final float duration;


    public Interpolate (double start, double end, float duration, InterpolationTypes interpolationTypes) {
        this.start = start;
        this.end = end;
        this.duration = duration;
        this.interpolationTypes = interpolationTypes;
    }


    public double getValue() {
        return InterpolationFunctions.interpolate(start, end, time, interpolationTypes);
    }

    public void start() {
        isRunning = true;
        time = 0d;

        RenderManager.addToRender(this);
    }

    public void stop() {
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void render(double delta) {
        if (isRunning && time >= 1) {
            time = 1f;
            stop();
            return;
        }
        time += delta / duration;
    }

    @Override
    public boolean renderIsEnd() {
        return !isRunning;
    }
}
