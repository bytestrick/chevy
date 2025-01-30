package chevy.view.animation;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.stateMachine.EntityState;
import chevy.service.RenderManager;
import chevy.service.Renderable;
import chevy.utils.Pair;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Animates sprites in game
 */
public final class AnimatedSprite implements Renderable {
    /**
     * Types of animation based on direction
     */
    private final Pair<EntityState, Direction> type;
    /**
     * Frames for the animation
     */
    private final BufferedImage[] frames;
    /**
     * Total number of frames in the animation
     */
    private final int nFrame;
    /**
     * Duration in seconds of each frame
     */
    private final float frameDuration;
    /**
     * If the animation should loop or not
     */
    private final boolean loop;
    /**
     * Scale of the animation
     */
    private final float scale;
    /**
     * Offset of the animation
     */
    private final Point offset;
    /**
     * Current frame index in the animation
     */
    private int currentIndexFrame;
    /**
     * If the animation is currently running
     */
    private boolean running;
    /**
     * If the animation should be deleted on the next cycle
     */
    private boolean delete = true;
    /**
     * Time elapsed since the start
     */
    private double time;

    /**
     * @param type          types of animation based on direction
     * @param nFrames       number of frames
     * @param frameDuration duration of each frame
     * @param loop          if the animation should loop
     * @param offset        offset of the animation
     * @param scale         scale of the animation
     */
    public AnimatedSprite(Pair<EntityState, Direction> type, int nFrames, float frameDuration,
                          boolean loop, Point offset, float scale) {
        this.type = type;
        nFrame = nFrames;
        this.frameDuration = frameDuration;
        this.loop = loop;
        this.scale = scale;
        this.offset = offset;

        frames = new BufferedImage[nFrames];
    }

    /**
     * Add a frame to the animation at the specified index.
     */
    public void addFrame(int index, BufferedImage frame) {
        if (index < nFrame) {
            frames[index] = frame;
        }
    }

    /**
     * @return the current frame of the animation
     */
    public BufferedImage getFrame() {
        if (currentIndexFrame >= nFrame) {
            return frames[nFrame - 1];
        }
        return frames[currentIndexFrame];
    }

    /**
     * @return the offset of the animation
     */
    public Point getOffset() {
        return new Point(offset);
    }

    /**
     * @return the scale of the animation
     */
    public float getScale() {
        return scale;
    }

    /**
     * Start the animation
     */
    private void start() {
        running = true;
        if (delete) {
            delete = false;
            RenderManager.register(this);
        }
    }

    /**
     * Restart the animation from the beginning
     */
    public void restart() {
        currentIndexFrame = 0;
        time = 0;
        if (delete) {
            start();
            return;
        }
        running = true;
    }

    /**
     * Stop the animation
     */
    private void stop() {
        running = false;
    }

    /**
     * @return the number of frames in the animation
     */
    public int getNFrame() {
        return nFrame;
    }

    public boolean isNotRunning() {
        return !running;
    }

    /**
     * @return the type of animation for the sprite
     */
    public Pair<EntityState, Direction> getType() {
        return type;
    }

    /**
     * Delete the animation
     */
    public void delete() {
        stop();
        delete = true;
    }

    /**
     * Update the animation based on the elapsed time.
     */
    @Override
    public void render(double delta) {
        if (running) {
            if (time >= frameDuration) {
                // we don't use time = 0d because if time is 1.5 when it is set to 0 we lose 0.5s
                time -= frameDuration;
                ++currentIndexFrame;
                if (loop) {
                    currentIndexFrame = currentIndexFrame % nFrame;
                } else if (currentIndexFrame >= nFrame) {
                    delete();
                }
            } else {
                time += delta;
            }
        }
    }

    @Override
    public boolean renderFinished() {
        return delete;
    }
}
