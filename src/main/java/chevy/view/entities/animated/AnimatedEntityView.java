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

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public abstract class AnimatedEntityView extends EntityView {
    private final Map<Pair<EntityState, Direction>, AnimatedSprite> animations = new HashMap<>();
    protected Interpolation horizontal, vertical;
    /** Lo stato corrente nella macchina a stati */
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
     * @param state     stato a cui l'animazione deve essere associata
     * @param direction direzione dell'animazione
     * @param frames    numero di frame totali di cui è composta l'animazione
     * @param loop      se l'animazione può andare in loop
     * @param times     (solo se <code>loop</code> è <code>true</code>) ripetizioni per
     *                  l'animazione
     * @param duration  durata totale, in secondi, dell'animazione
     * @param offset    offset per la posizione dei frame
     * @param scale     scale per la grandezza dei frame
     * @param path      percorso della cartella dove sono contenuti i frame
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
            Log.warn("L'animazione " + animationType + " è già presente");
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
     * @return il fotogramma corrente dell'animazione
     */
    public abstract BufferedImage getFrame();
}