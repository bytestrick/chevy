package chevy.model;

import chevy.service.Update;
import chevy.service.UpdateManager;

public class Timer implements Update {
    private final double duration;
    private double time;
    private boolean isRunning;


    public Timer(double secDuration) {
        this.duration = secDuration;
        this.isRunning = false;
        this.time = 0d;
    }


    public boolean isRunning() {
        return isRunning;
    }

    public void start() {
        if (!isRunning()) {
            time = 0d;
            isRunning = true;
            UpdateManager.addToUpdate(this);
        }
    }

    public void stop() {
        isRunning = false;
    }

    @Override
    public void update(double delta) {
        if (time >= duration) {
            isRunning = false;
            return;
        }

        time += delta;
    }

    @Override
    public boolean updateIsEnd() {
        return !isRunning;
    }
}
