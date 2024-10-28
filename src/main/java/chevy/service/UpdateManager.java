package chevy.service;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Aggiorna tutti gli oggetti di {@link chevy.model}
 */
public final class UpdateManager {
    private static final Collection<Updatable> updatables = new LinkedList<>();
    private static final Collection<Updatable> queued = new LinkedList<>();

    /**
     * Registra un oggetto per l'aggiornamento
     */
    public static void register(Updatable updatable) {
        synchronized (queued) {
            queued.add(updatable);
        }
    }

    /**
     * Aggiorna tutti gli oggetti registrati
     *
     * @param delta tempo trascorso dall'ultimo aggiornamento
     */
    static void update(double delta) {
        synchronized (queued) {
            updatables.addAll(queued);
            queued.clear();
        }

        Iterator<Updatable> it = updatables.iterator();
        while (it.hasNext()) {
            Updatable updatable = it.next();
            updatable.update(delta);
            if (updatable.updateFinished()) {
                it.remove();
            }
        }
    }
}
