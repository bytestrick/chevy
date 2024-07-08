package chevy.view.entityView.entityViewAnimated;

import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.utils.Pair;
import chevy.utils.Vector2;
import chevy.view.Image;
import chevy.view.animation.AnimatedSprite;
import chevy.view.entityView.EntityView;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public abstract class EntityViewAnimated extends EntityView {
    private final Map<Pair<CommonEnumStates, Integer>, AnimatedSprite> animations;


    public EntityViewAnimated() {
        animations = new HashMap<>();
    }


    // crea l'animation player con le funzionalità base
    protected void createAnimation(CommonEnumStates enumStates, int type,
                                   int nFrame, float secDurationFrame,
                                   String folderPath, String extension) {
        createAnimation(enumStates, type,
                nFrame, false, 1, secDurationFrame,
                new Vector2<>(0, 0), 1f,
                folderPath, extension);
    }

    protected void createAnimation(CommonEnumStates enumStates, int type,
                                   int nFrame, float secDurationFrame,
                                   Vector2<Integer> offset, float scale,
                                   String folderPath, String extension) {
        createAnimation(enumStates, type,
                nFrame, false, 1, secDurationFrame,
                offset, scale,
                folderPath, extension);
    }

    protected void createAnimation(CommonEnumStates enumStates, int type,
                                   int nFrame, boolean loop, int times, float animationDuration,
                                   String folderPath, String extension) {
        if (!loop)
            times = 1;
        createAnimation(enumStates, type,
                nFrame, loop, times, animationDuration,
                new Vector2<>(0, 0), 1f,
                folderPath, extension);
    }

    /**
     * @param enumStates stato a cui l'animazione deve essere associata
     * @param type tipo di animazione associata all'enumState
     * @param nFrame numero di frame totali di cui è composta l'animazione
     * @param loop se l'animazione può andare in loop
     * @param times (disponibile solo se loop è true) numero di volte che l'animazione verrà ripetuta
     * @param animationDuration durata totale, in secondi, dell'animazione
     * @param offset offset per la posizione dei frame
     * @param scale scale per la grandezza dei frame
     * @param folderPath percorso della cartella dove sono contenuti i frame
     * @param extension estensione comune dei frame
     */
    protected void createAnimation(CommonEnumStates enumStates, int type,
                                   int nFrame, boolean loop, int times, float animationDuration,
                                   Vector2<Integer> offset, float scale,
                                   String folderPath, String extension) {
        AnimatedSprite animatedSprite = new AnimatedSprite(
                new Pair<>(enumStates, type),
                nFrame,
                animationDuration / (times * nFrame),
                loop,
                offset,
                scale
        );
        initAnimation(animatedSprite, folderPath, extension);
    }

    protected void initAnimation(AnimatedSprite animation, String folderPath, String extension) {
        for (int i = 0; i < animation.getNFrame(); ++i)
            animation.addFrame(i, Image.load(folderPath + "/" + i + extension));
        addAnimation(animation.getAnimationTypes(), animation);
    }


    protected void addAnimation(Pair<CommonEnumStates, Integer> animationTypes, AnimatedSprite animatedSprite) {
        if (!animations.containsKey(animationTypes))
            animations.put(animationTypes, animatedSprite);
        else
            System.out.println("[!] L'animaizone " + animationTypes + " è già presente");
    }

    protected void deleteAnimations() {
        for (Pair<CommonEnumStates, Integer> key : animations.keySet()) {
            animations.get(key).delete();
        }
    }

    protected AnimatedSprite getAnimatedSprite(CommonEnumStates enumStates, int type) {
        Pair<CommonEnumStates, Integer> animationTypes = new Pair<>(enumStates, type);
        return animations.get(animationTypes);
    }

    public abstract BufferedImage getCurrentFrame();

    public abstract Vector2<Double> getCurrentPosition();

    public abstract void wasRemoved();
}
