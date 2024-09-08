package chevy.view.animation;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.stateMachine.EntityState;
import chevy.service.Render;
import chevy.service.RenderManager;
import chevy.utils.Pair;
import chevy.utils.Vector2;

import java.awt.image.BufferedImage;

/**
 * Gestisce le animazioni sprite nel gioco.
 */
public final class AnimatedSprite implements Render {
    /** Tipi di animazione in base alla direzione */
    private final Pair<EntityState, Direction> type;
    /** // Array di frame per l'animazione */
    private final BufferedImage[] frames;
    /** Numero totale di frame nell'animazione. */
    private final int nFrame;
    /** Durata in secondi di ciascun frame */
    private final float frameDuration;
    /** Se l'animazione deve ripetersi o meno */
    private final boolean loop;
    /** Scala dell'animazione */
    private final float scale;
    /** Offset dell'animazione */
    private final Vector2<Integer> offset;
    /** Indice del frame corrente nell'animazione */
    private int currentIndexFrame;
    /** Se l'animazione è attualmente in esecuzione */
    private boolean running;
    /** Se l'animazione deve essere eliminata */
    private boolean delete = true;
    /** Tempo trascorso dall'inizio */
    private double time;

    /**
     * @param type          tipi di animazione in base alla direzione
     * @param nFrames       numero di frame
     * @param frameDuration durata di ciascun frame in secondi
     * @param loop          se l'animazione deve ripetersi
     * @param offset        offset dell'animazione
     * @param scale         scala che la sequenza deve avere durante la rappresentazione
     */
    public AnimatedSprite(Pair<EntityState, Direction> type, int nFrames, float frameDuration,
                          boolean loop, Vector2<Integer> offset, float scale) {
        this.type = type;
        nFrame = nFrames;
        this.frameDuration = frameDuration;
        this.loop = loop;
        this.scale = scale;
        this.offset = offset;

        frames = new BufferedImage[nFrames];
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
    public BufferedImage getFrame() {
        if (currentIndexFrame >= nFrame) {
            return frames[nFrame - 1];
        }
        return frames[currentIndexFrame];
    }

    /**
     * @return l'offset dell'animazione
     */
    public Vector2<Integer> getOffset() {return offset;}

    /**
     * @return la scala dell'animazione
     */
    public float getScale() { return scale; }

    /**
     * Avvia l'animazione.
     */
    public void start() {
        running = true;
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
        running = true;
    }

    /**
     * Ferma l'animazione.
     */
    public void stop() {running = false;}

    /**
     * @return il numero totale di frame nell'animazione
     */
    public int getNFrame() {return nFrame;}

    /**
     * @return true se l'animazione è in esecuzione, false altrimenti
     */
    public boolean isRunning() {return running;}

    /**
     * @return il tipo di animazione per lo sprite
     */
    public Pair<EntityState, Direction> getType() {return type;}

    /**
     * Elimina l'animazione
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
        if (running) {
            if (time >= frameDuration) {
                // non time = 0d perché se time è 1.5 quando viene impostato a 0 si perdono 0.5s
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
    public boolean renderFinished() {return delete;}
}