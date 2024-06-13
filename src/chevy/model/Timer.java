package chevy.model;

import chevy.service.Update;
import chevy.service.UpdateManager;

public class Timer implements Update {
    private final double duration;
    private boolean isEnd;
    private boolean isStart;
    private double time;


    public Timer(double duration) {
        this.duration = duration;
        this.isEnd = true;
        this.isStart = false;
        this.time = 0d;
    }


    public boolean isEnd() {
        return isEnd;
    }

    public boolean isStart() {
        return isStart;
    }

    public void start() {
        if (!isStart) {
            time = 0d;
            isStart = true;
            isEnd = false;
            UpdateManager.addToUpdate(this);
        }
    }

    public void stop() {
        isEnd = true;
    }

    @Override
    public void update(double delta) {
        if (time >= duration) {
            isEnd = true;
            isStart = false;
            return;
        }

        time += delta;
    }

    @Override
    public boolean updateIsEnd() {
        return isEnd;
    }
}
