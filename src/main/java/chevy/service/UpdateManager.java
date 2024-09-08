package chevy.service;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Aggiorna tutti gli oggetti di {@link chevy.model}
 */
public final class UpdateManager {
    private static final Collection<Update> updateList = new LinkedList<>();
    private static final Collection<Update> toAdd = new LinkedList<>();

    public synchronized static void addToUpdate(Update u) {toAdd.add(u);}

    public synchronized static void update(double delta) {
        updateList.addAll(toAdd);
        toAdd.clear();

        Iterator<Update> iterator = updateList.iterator();
        while (iterator.hasNext()) {
            Update current = iterator.next();
            current.update(delta);
            if (current.updateFinished()) {
                iterator.remove();
            }
        }
    }
}