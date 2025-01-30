package chevy.service;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Update the components of {@link chevy.model}
 */
public final class UpdateManager {
    private static final Collection<Updatable> updatables = new LinkedList<>();
    private static final Collection<Updatable> queued = new LinkedList<>();

    /**
     * Register an object for updating
     */
    public static void register(Updatable updatable) {
        synchronized (queued) {
            queued.add(updatable);
        }
    }

    /**
     * Update all registered objects
     *
     * @param delta time since last update
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
