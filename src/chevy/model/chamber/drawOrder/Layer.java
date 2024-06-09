package chevy.model.chamber.drawOrder;

import chevy.model.entity.Entity;

import java.util.LinkedList;
import java.util.List;

public class Layer implements Comparable<Layer> {
    private final int nLayer;
    private final List<Entity> layer;

    public Layer(Integer n) {
        this.nLayer = n;
        this.layer = new LinkedList<>();
    }

    public List<Entity> getLayer() {
        return layer;
    }

    @Override
    public int compareTo(Layer other) {
        return Integer.compare(this.nLayer, other.nLayer);
    }
}

