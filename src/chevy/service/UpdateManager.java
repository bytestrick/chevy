package chevy.service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class UpdateManager {
    private static final List<Update> updateList = new LinkedList<>();


    public static void addToUpdate(Update u) {
        updateList.add(u);
    }


    public static void update(double delta) {
        Iterator<Update> iterator = updateList.iterator();
        while (iterator.hasNext()) {
            Update current = iterator.next();
            if (!current.isEnd())
                current.update(delta);
            else {
                iterator.remove();
            }
        }
    }
}
