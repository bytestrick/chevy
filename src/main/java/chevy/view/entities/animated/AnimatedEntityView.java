package chevy.view.entities.animated;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.GlobalState;
import chevy.utils.Load;
import chevy.utils.Log;
import chevy.utils.Pair;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.EntityView;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public abstract class AnimatedEntityView extends EntityView {
    private final Map<Pair<CommonState, Integer>, AnimatedSprite> animations = new HashMap<>();
    protected Interpolation moveInterpolationX;
    protected Interpolation moveInterpolationY;
    protected Vector2<Double> currentViewPosition;
    protected GlobalState currentGlobalState;
    protected boolean firstTimeInState = false;

    protected void createAnimation(CommonState state, int type, int frames, float secDurationFrame,
                                   String folderPath, String extension) {
        createAnimation(state, type, frames, false, 1, secDurationFrame, new Vector2<>(0, 0), 1f,
                folderPath,
                extension);
    }

    protected void createAnimation(CommonState state, int type, int frames,
                                   float frameDurationSeconds,
                                   Vector2<Integer> offset, float scale, String folderPath,
                                   String extension) {
        createAnimation(state, type, frames, false, 1, frameDurationSeconds, offset, scale,
                folderPath, extension);
    }

    protected void createAnimation(CommonState enumStates, int type, int frames, boolean loop,
                                   int times,
                                   float duration, String folderPath, String extension) {
        if (!loop) {
            times = 1;
        }
        createAnimation(enumStates, type, frames, loop, times, duration, new Vector2<>(0, 0), 1f,
                folderPath
                , extension);
    }

    /**
     * @param state      stato a cui l'animazione deve essere associata
     * @param type       tipo di animazione associata a <code>state</code>
     * @param frames     numero di frame totali di cui è composta l'animazione
     * @param loop       se l'animazione può andare in loop
     * @param times      (solo se <code>loop</code> è <code>true</code>) ripetizioni per
     *                   l'animazione
     * @param duration   durata totale, in secondi, dell'animazione
     * @param offset     offset per la posizione dei frame
     * @param scale      scale per la grandezza dei frame
     * @param folderPath percorso della cartella dove sono contenuti i frame
     * @param extension  estensione comune dei frame
     */
    protected void createAnimation(CommonState state, int type, int frames, boolean loop, int times,
                                   float duration, Vector2<Integer> offset, float scale,
                                   String folderPath,
                                   String extension) {
        AnimatedSprite animatedSprite = new AnimatedSprite(new Pair<>(state, type), frames,
                duration / (times * frames), loop, offset, scale);
        initAnimation(animatedSprite, folderPath, extension);
    }

    protected void initAnimation(AnimatedSprite animation, String folderPath, String extension) {
        for (int i = 0; i < animation.getNFrame(); ++i) {
            animation.addFrame(i, Load.image(folderPath + "/" + i + extension));
        }
        addAnimation(animation.getAnimationTypes(), animation);
    }

    protected void addAnimation(Pair<CommonState, Integer> animationTypes,
                                AnimatedSprite animatedSprite) {
        if (animations.containsKey(animationTypes)) {
            Log.warn("L'animazione " + animationTypes + " è già presente");
        } else {
            animations.put(animationTypes, animatedSprite);
        }
    }

    protected void deleteAnimations() {
        for (Pair<CommonState, Integer> key : animations.keySet()) {
            animations.get(key).delete();
        }
    }

    protected AnimatedSprite getAnimatedSprite(CommonState state, int type) {
        Pair<CommonState, Integer> animationTypes = new Pair<>(state, type);
        return animations.get(animationTypes);
    }

    public void remove() {
        if (moveInterpolationX != null) {
            moveInterpolationX.delete();
        }
        if (moveInterpolationY != null) {
            moveInterpolationY.delete();
        }
        deleteAnimations();
    }

    public abstract BufferedImage getCurrentFrame();
}