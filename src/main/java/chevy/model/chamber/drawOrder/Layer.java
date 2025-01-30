package chevy.model.chamber.drawOrder;

import chevy.model.entity.Entity;

import java.util.LinkedList;
import java.util.List;

/**
 * Priority with which an entity must be redrawn. An entity with {@code layer = 5} will be drawn on top of an entity with {@code layer = 3} (or any number less than 5),
 * if the layer is the same it will have the same priority as the added entity.
 */
public final class Layer implements Comparable<Layer> {
    /**
     * Priority
     */
    private final int nLayer;
    /**
     * The list containing the entities with the same priority level.
     */
    private final List<Entity> layer;
    /**
     * The list of entities to add to the level.
     */
    private final List<Entity> entitiesToAdd;

    /**
     * Initialize the Layer with the specified priority level.
     *
     * @param n the number of the level
     */
    Layer(int n) {
        nLayer = n;
        layer = new LinkedList<>();
        entitiesToAdd = new LinkedList<>();
    }

    /**
     * Apply the changes to the level, adding all the entities to add to the level.
     *
     * @return the list of entities in the level
     */
    public synchronized List<Entity> getLayer() {
        layer.addAll(entitiesToAdd);
        entitiesToAdd.clear();
        return layer;
    }

    /**
     * Add an entity to the Layer.
     *
     * @param entity entity to add
     */
    public synchronized void add(Entity entity) {entitiesToAdd.addLast(entity);}

    @Override
    public int compareTo(Layer other) {return Integer.compare(nLayer, other.nLayer);}
}
