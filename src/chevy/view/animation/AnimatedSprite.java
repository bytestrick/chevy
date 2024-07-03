package chevy.view.animation;

import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.service.Render;
import chevy.service.RenderManager;
import chevy.utils.Pair;
import chevy.utils.Vector2;

import java.awt.image.BufferedImage;

/**
 * Gestisce le animazioni sprite nel gioco.
 */
public class AnimatedSprite implements Render {
    private final Pair<CommonEnumStates, Integer> animationTypes; // Tipi di animazione.
    private final BufferedImage[] frames; // Array di frame per l'animazione.
    private final int nFrame; // Numero totale di frame nell'animazione.
    private final float secFrameDuration; // Durata in secondi di ciascun frame.
    private final boolean loop; // Se l'animazione deve ripetersi o meno.
    private final float scale; // Scala dell'animazione.
    private final Vector2<Integer> offset; // Offset dell'animazione.
    private int currentIndexFrame = 0; // Indice del frame corrente nell'animazione.
    private boolean isRunning = false; // Se l'animazione è attualmente in esecuzione.
    private boolean delete = true; // Se l'animazione deve essere eliminata.
    private double time = 0d; // Tempo trascorso dall'inizio.

    /**
     * @param animationTypes   tipi di animazione
     * @param nFrame           numero di frame
     * @param secFrameDuration durata di ciascun frame
     * @param loop             se l'animazione deve ripetersi
     */
    public AnimatedSprite(Pair<CommonEnumStates, Integer> animationTypes, int nFrame, float secFrameDuration,
                          boolean loop) {
        this.animationTypes = animationTypes;
        this.nFrame = nFrame;
        this.secFrameDuration = secFrameDuration;
        this.loop = loop;
        this.scale = 1;
        this.offset = new Vector2<>(0, 0);

        frames = new BufferedImage[nFrame];
    }

    /**
     * @param animationTypes   tipi di animazione
     * @param nFrame           numero di frame
     * @param secFrameDuration durata di ciascun frame
     * @param loop             se l'animazione deve ripetersi
     * @param offset           offset dell'animazione
     * @param scale            scala che la sequenza deve avere durante la rappresentazione
     */
    public AnimatedSprite(Pair<CommonEnumStates, Integer> animationTypes, int nFrame, float secFrameDuration,
                          boolean loop, Vector2<Integer> offset, float scale) {
        this.animationTypes = animationTypes;
        this.nFrame = nFrame;
        this.secFrameDuration = secFrameDuration;
        this.loop = loop;
        this.scale = scale;
        this.offset = offset;

        frames = new BufferedImage[nFrame];
    }

    /**
     * Aggiunge un frame all'animazione all'indice specificato.
     */
    public void addFrame(int index, BufferedImage frame) {
        if (index < nFrame) {
            frames[index] = frame;
        }
    }

    /**
     * @return il frame corrente dell'animazione
     */
    public BufferedImage getCurrentFrame() {
        if (currentIndexFrame >= nFrame) {
            return frames[nFrame - 1];
        }
        return frames[currentIndexFrame];
    }

    /**
     * @return l'offset dell'animazione
     */
    public Vector2<Integer> getOffset() { return offset; }

    /**
     * @return la scala dell'animazione
     */
    public float getScale() {
        return scale;
    }

    /**
     * Avvia l'animazione.
     */
    public void start() {
        isRunning = true;
        if (delete) {
            delete = false;
            RenderManager.addToRender(this);
        }
    }

    /**
     * Riavvia l'animazione dall'inizio.
     */
    public void restart() {
        currentIndexFrame = 0;
        time = 0d;
        if (delete) {
            start();
            return;
        }
        isRunning = true;
    }

    /**
     * Ferma l'animazione.
     */
    public void stop() { isRunning = false; }

    /**
     * @return il numero totale di frame nell'animazione
     */
    public int getNFrame() { return nFrame; }

    /**
     * @return true se l'animazione è in esecuzione, false altrimenti
     */
    public boolean isRunning() { return isRunning; }

    /**
     * @return il tipo di animazione per lo sprite
     */
    public Pair<CommonEnumStates, Integer> getAnimationTypes() { return animationTypes; }

    /**
     * Elimina l'animazione.
     */
    public void delete() {
        stop();
        delete = true;
    }

    /**
     * Aggiorna l'animazione in base al tempo trascorso.
     */
    @Override
    public void render(double delta) {
        if (isRunning) {
            if (time >= secFrameDuration) {
                // non time = 0d perché se time è 1.5 quando viene impostato a 0 si perdono 0.5s
                time -= secFrameDuration;
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
    public boolean renderFinished() { return delete; }
}