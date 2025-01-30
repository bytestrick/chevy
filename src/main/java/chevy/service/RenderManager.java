package chevy.service;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Update the components of {@link chevy.view}
 */
public final class RenderManager {
    private static final Collection<Renderable> renderables = new LinkedList<>();
    private static final Collection<Renderable> queued = new LinkedList<>();

    public static void register(Renderable r) {
        synchronized (queued) {
            queued.add(r);
        }
    }

    static void render(double delta) {
        synchronized (queued) {
            renderables.addAll(queued);
            queued.clear();
        }

        Iterator<Renderable> it = renderables.iterator();
        while (it.hasNext()) {
            Renderable renderable = it.next();
            renderable.render(delta);
            if (renderable.renderFinished()) {
                it.remove();
            }
        }
    }
}
