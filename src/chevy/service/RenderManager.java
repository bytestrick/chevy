package chevy.service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RenderManager {
    private static final List<Render> renderList = new LinkedList<>();


    public static void addToRender(Render r) {
        renderList.add(r);
    }

    public static void render() {
        for (Render currentRenderElement : renderList) {
            currentRenderElement.render();
        }
    }

    public static void removeFormRender(Render r) {
        renderList.remove(r);
    }
}
