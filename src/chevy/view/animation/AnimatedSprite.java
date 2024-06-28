package chevy.view.animation;

import chevy.model.entity.dinamicEntity.stateMachine.CommonEnumStates;
import chevy.service.Render;
import chevy.service.RenderManager;
import chevy.utils.Pair;
import chevy.utils.Vector2;

import java.awt.image.BufferedImage;

/**
 * La classe AnimatedSprite implementa l'interfaccia Render e gestisce le animazioni sprite nel gioco.
 * Questa classe contiene metodi per gestire lo stato dell'animazione, come l'avvio, l'arresto e l'aggiornamento dell'animazione.
 */
public class AnimatedSprite implements Render {
    // Tipo di animazione.
    private final Pair<CommonEnumStates, Integer> animationTypes;
    // Array di frame per l'animazione.
    private final BufferedImage[] frames;
    // Numero totale di frame nell'animazione.
    private final int nFrame;
    // Durata in secondi di ciascun frame.
    private final float secFrameDuration;
    // Indice del frame corrente nell'animazione.
    private int currentIndexFrame = 0;
    // Se l'animazione deve ripetersi o meno.
    private final boolean loop;
    // Se l'animazione è attualmente in esecuzione.
    private boolean isRunning = false;
    // Se l'animazione deve essere eliminata.
    private boolean delete = true;
    // Tempo trascorso dall'inizio.
    private double time = 0d;
    // Scala dell'animazione.
    private final float scale;
    // Offset dell'animazione.
    private final Vector2<Integer> offset;

    /**
     * Costruttore della classe AnimatedSprite. Inizializza l'oggetto con i tipo di animazione, il numero di frame,
     * la durata di ciascun frame e se l'animazione deve ripetersi.
     */
    public AnimatedSprite(Pair<CommonEnumStates, Integer> animationTypes, int nFrame, float secFrameDuration, boolean loop) {
        this.animationTypes = animationTypes;
        this.nFrame = nFrame;
        this.secFrameDuration = secFrameDuration;
        this.loop = loop;

        this.scale = 1;
        this.offset = new Vector2<>(0, 0);

        frames = new BufferedImage[nFrame];
    }

    /**
     * Inizializza l'oggetto con i tipi di animazione, il numero di frame, la durata di ciascun frame,
     * se l'animazione deve ripetersi, l'offset e la scale che la sequenza di sprite deve avere quando
     * viene mostrato.
     */
    public AnimatedSprite(Pair<CommonEnumStates, Integer> animationTypes, int nFrame, float secFrameDuration, boolean loop, Vector2<Integer> offset, float scale) {
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
        if (index < nFrame)
            frames[index] = frame;
    }

    /**
     * Restituisce il frame corrente dell'animazione.
     */
    public BufferedImage getCurrentFrame() {
        if (currentIndexFrame >= nFrame) {
            return frames[nFrame - 1];
        }
        return frames[currentIndexFrame];
    }

    /**
     * Restituisce l'offset dell'animazione.
     */
    public Vector2<Integer> getOffset() {
        return offset;
    }

    /**
     * Restituisce la scala dell'animazione.
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
    public void stop() {
        isRunning = false;
    }

    /**
     * Restituisce il numero totale di frame nell'animazione.
     */
    public int getNFrame() {
        return nFrame;
    }

    /**
     * Restituisce se l'animazione è attualmente in esecuzione.
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Restituisce il tipo di animazione per lo sprite.
     */
    public Pair<CommonEnumStates, Integer> getAnimationTypes() {
        return animationTypes;
    }

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
                time -= secFrameDuration; // non time = 0d perché se time è 1.5 quando viene impostato a 0 si perdono 0.5s
                ++currentIndexFrame;
                if (loop) {
                    currentIndexFrame = currentIndexFrame % nFrame;
                }
                else if (currentIndexFrame >= nFrame) {
                    delete();
                }
            }
            else
                time += delta;
        }
    }

    @Override
    public boolean renderIsEnd() {
        return delete;
    }
}
