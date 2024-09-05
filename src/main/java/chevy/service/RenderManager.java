package chevy.service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Aggiorna i componenti di {@link chevy.view}
 */
public class RenderManager {
    private static final List<Render> renderList = new LinkedList<>();
    private static final List<Render> toAdd = new LinkedList<>();

    public static void addToRender(Render r) {
        synchronized (toAdd) {
            toAdd.add(r);
        }
    }

    public static void render(double delta) {
        synchronized (toAdd) {
            renderList.addAll(toAdd);
            toAdd.clear();
        }

        Iterator<Render> it = renderList.iterator();
        while (it.hasNext()) {
            Render currentRenderElement = it.next();
            currentRenderElement.render(delta);
            if (currentRenderElement.renderFinished()) {
                it.remove();
            }
        }
    }
}
