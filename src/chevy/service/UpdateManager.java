package chevy.service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Aggiorna tutti gli oggetti di model.
 */
public class UpdateManager {
    private static final List<Update> updateList = new LinkedList<>();
    private static final List<Update> toAdd = new LinkedList<>();


    public static void addToUpdate(Update u) {
        synchronized (toAdd) {
            toAdd.add(u);
        }
    }

    public static void update(double delta) {
        synchronized (toAdd) {
            updateList.addAll(toAdd);
            toAdd.clear();
        }

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
