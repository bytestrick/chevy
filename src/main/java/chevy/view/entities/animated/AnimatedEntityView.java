package chevy.view.entities.animated;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Load;
import chevy.utils.Log;
import chevy.utils.Pair;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.EntityView;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public abstract class AnimatedEntityView extends EntityView {
    private final Map<Pair<EntityState, Direction>, AnimatedSprite> animations = new HashMap<>();
    protected Interpolation horizontal, vertical;
    /**
     * The current state in the state machine
     */
    protected Vertex vertex;
    protected boolean firstTimeInState;

    protected void animate(EntityState state, Direction direction, int frames, float duration,
                           String path) {
        animate(state, direction, frames, false, 1, duration, new Point(0, 0), 1f, path);
    }

    protected void animate(EntityState state, Direction direction, int frames, float duration,
                           Point offset, String path) {
        animate(state, direction, frames, false, 1, duration, offset, 1f, path);
    }

    protected void animate(EntityState state, Direction direction, int frames,
                           int times, float duration, String path) {
        animate(state, direction, frames, true, times, duration, new Point(0, 0), 1f, path);
    }

    /**
     * @param state     state to which the animation is associated
     * @param direction direction of the animation
     * @param frames    number of frames of the animation
     * @param loop      if the animation can loop
     * @param times     only if loop is {@code true}, repetitions for the animation
     * @param duration  total duration of the animation in seconds
     * @param offset    offset for the frame position
     * @param scale     scale of the frame
     * @param path      resource path of the frames
     */
    protected void animate(EntityState state, Direction direction, int frames,
                           boolean loop, int times, float duration,
                           Point offset, float scale, String path) {
        AnimatedSprite animation = new AnimatedSprite(new Pair<>(state, direction), frames,
                duration / (times * frames), loop, offset, scale);
        int nFrame = animation.getNFrame();
        for (int i = 0; i < nFrame; ++i) {
            animation.addFrame(i, Load.image(path + "/" + i + ".png"));
        }

        final Pair<EntityState, Direction> animationType = animation.getType();

        if (animations.containsKey(animationType)) {
            Log.warn("The animation " + animationType + " is already present");
        } else {
            animations.put(animationType, animation);
        }
    }

    protected final void deleteAnimations() {
        for (AnimatedSprite animatedSprite : animations.values()) {
            animatedSprite.delete();
        }
    }

    protected final AnimatedSprite getAnimatedSprite(EntityState state, Direction direction) {
        return animations.get(new Pair<>(state, direction));
    }

    @Override
    public void remove() {
        if (horizontal != null) {
            horizontal.delete();
        }
        if (vertical != null) {
            vertical.delete();
        }
        deleteAnimations();
    }

    /**
     * @return the current animation frame
     */
    public abstract BufferedImage getFrame();
}
