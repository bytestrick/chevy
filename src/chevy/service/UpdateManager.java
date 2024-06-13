package chevy.service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class UpdateManager {
    private static final List<Update> updateList = new LinkedList<>();
    private static final List<Update> toAdd = new LinkedList<>();


    public static void addToUpdate(Update u) {
        toAdd.add(u);
    }


    public static void update(double delta) {
        updateList.addAll(toAdd);
        toAdd.clear();

        Iterator<Update> iterator = updateList.iterator();
        while (iterator.hasNext()) {
            Update current = iterator.next();
            current.update(delta);
            if (current.updateIsEnd())
                iterator.remove();
        }
    }
}
