package chevy.model.chamber.drawOrder;

import chevy.model.entity.Entity;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LayerManager {
    private final List<Layer> drawOrder;


    public LayerManager() {
        this.drawOrder = new LinkedList<>();
    }


    private Layer findLayer(int nLayer) {
        Layer newLayer = new Layer(nLayer);
        int index = Collections.binarySearch(drawOrder, newLayer);

        if (index < 0) {
            index = -1 * (index + 1);
            drawOrder.add(index, newLayer);
        }

        return drawOrder.get(index);
    }

    public synchronized void add(Entity entity, int nLayer) {
        Layer layer = findLayer(nLayer);
        layer.add(entity);
    }

    public synchronized List<Layer> getDrawOrder() {
        return drawOrder;
    }
}

