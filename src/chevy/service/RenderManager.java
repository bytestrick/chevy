package chevy.service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe che aggiorna i componenti della view
 */
public class RenderManager {
    private static final List<Render> renderList = new LinkedList<>();
    private static final List<Render> toAdd = new LinkedList<>();


    public synchronized static void addToRender(Render r) {
        toAdd.add(r);
//        System.out.println("[-] Aggiunto al render: " + r);
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
            if (currentRenderElement.renderIsEnd()) {
                it.remove();
//                System.out.println("[-] Rimosso dal render: " + currentRenderElement);
            }
        }
    }
}
