package chevy.service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class UpdateManager {
    private static final List<Update> udateAnimationList = new LinkedList<>();


    public static void addToUpdate(Update u) {
        udateAnimationList.add(u);
    }


    public static void update(double delta) {
        updateAnimation(delta);
    }


    private static void updateAnimation(double delta) {
        // update all
        Iterator<Update> iterator = udateAnimationList.iterator();
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
