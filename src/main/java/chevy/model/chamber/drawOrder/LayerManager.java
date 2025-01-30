package chevy.model.chamber.drawOrder;

import chevy.model.entity.Entity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Draw order of the entities in the game.
 * Uses a list of Layers, each of which contains a set of entities to draw.
 */
public final class LayerManager {
    private final List<Layer> drawOrder;

    public LayerManager() {
        drawOrder = new LinkedList<>();
    }

    /**
     * Find a specific Layer in the list. If the Layer does not exist, it creates it and adds it to the
     *
     * @param nLayer priority of the level to find.
     * @return the level found or created.
     */
    private Layer findLayer(int nLayer) {
        Layer newLayer = new Layer(nLayer);
        int index = Collections.binarySearch(drawOrder, newLayer);

        if (index < 0) {
            index = -1 * (index + 1);
            drawOrder.add(index, newLayer);
        }

        return drawOrder.get(index);
    }

    /**
     * Adds an entity to a specific Layer
     *
     * @param entity the entity to add
     * @param nLayer the number of the level to add the entity to
     */
    public synchronized void add(Entity entity, int nLayer) {
        Layer layer = findLayer(nLayer);
        layer.add(entity);
    }

    public synchronized List<Layer> getDrawOrder() {
        return drawOrder;
    }
}
