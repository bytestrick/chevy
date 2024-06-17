package chevy.model.chamber.drawOrder;

import chevy.model.entity.Entity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Layer implements Comparable<Layer> {
    private final int nLayer;
    private final List<Entity> layer;
    private final List<Entity> entitiesToAdd;


    public Layer(int n) {
        this.nLayer = n;
        this.layer = new LinkedList<>();
        this.entitiesToAdd = new LinkedList<>();
    }

    public List<Entity> getLayer() {
        applyChanges();
        return layer;
    }

    public void add(Entity entity) {
        entitiesToAdd.addLast(entity);
    }


    private void applyChanges() {

        layer.addAll(entitiesToAdd);
        entitiesToAdd.clear();
    }


    @Override
    public int compareTo(Layer other) {
        return Integer.compare(this.nLayer, other.nLayer);
    }
}

