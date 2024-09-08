package chevy.service;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Aggiorna i componenti di {@link chevy.view}
 */
public final class RenderManager {
    private static final Collection<Render> renderList = new LinkedList<>();
    private static final Collection<Render> toAdd = new LinkedList<>();

    public synchronized static void addToRender(Render r) {toAdd.add(r);}

    static synchronized void render(double delta) {
        renderList.addAll(toAdd);
        toAdd.clear();

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
