package chevy.model.entity.stateMachine;

import chevy.service.Updatable;
import chevy.service.UpdateManager;
import chevy.utils.Log;

/**
 * Tracks the elapsed time
 */
public class Timer implements Updatable {
    private final double duration;
    private double time;
    private boolean running;
    private boolean delete = true;

    Timer(double secDuration) {
        duration = secDuration;
    }

    boolean isRunning() {
        return running && !delete;
    }

    /**
     * Starts the interpolation from where it stopped. If it is used for the first time the interpolation will start from the beginning.
     */
    private void start() {
        if (duration <= 0d) {
            Log.warn("The timer was not created because the duration is invalid: " + duration);
            return;
        }

        running = true;

        if (delete) {
            delete = false;
            UpdateManager.register(this);
        }
    }

    /**
     * Start the interpolation always from the beginning
     */
    public void restart() {
        time = 0d;
        if (delete) {
            start();
            return;
        }
        running = true;
    }

    /**
     * Stop the timer, but not permanently. The interpolation can be resumed later using {@link #start()},
     * or start it from the beginning using {@link #restart()}.
     */
    public void stop() {
        running = false;
    }

    /**
     * Delete the timer, this means that it will no longer be updated.
     */
    private void delete() {
        stop();
        delete = true;
    }

    @Override
    public void update(double delta) {
        if (running) {
            time += delta;

            if (time >= duration) {
                time = duration;
                running = false;
                delete();
            }
        }
    }

    @Override
    public boolean updateFinished() {
        return delete;
    }
}
