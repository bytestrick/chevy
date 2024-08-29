package chevy.model.chamber.drawOrder;

import chevy.model.entity.Entity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * La classe Layer rappresenta la priorità con cui un entità deve essere ridisegnata. Ad esempio,
 * un entità con layer 5 verrà disegnata sopra un entità con layer 3 (o un qualsiasi numero inferiore al 5), se
 * il layer è lo stesso avrà la priorità inserita dopo nella lista.
 */
public class Layer implements Comparable<Layer> {
    /**
     * Livello di priorità.
     */
    private final int nLayer;
    /**
     * La lista contenente le entità con lo stesso livello di priorità.
     */
    private final List<Entity> layer;
    /**
     * La lista di entità da aggiungere al livello.
     */
    private final List<Entity> entitiesToAdd;

    /**
     * Inizializza il Layer con il livello di priorità specificato.
     * @param n il numero del livello
     */
    public Layer(int n) {
        this.nLayer = n;
        this.layer = new LinkedList<>();
        this.entitiesToAdd = new LinkedList<>();
    }

    /**
     * Applica i cambiamenti al livello, aggiungendo tutte le entità da aggiungere al livello.
     * Poi svuota la lista di entità da aggiungere.
     * @return la lista di entità nel livello
     */
    public synchronized List<Entity> getLayer() {
        layer.addAll(entitiesToAdd);
        entitiesToAdd.clear();
        return layer;
    }

    /**
     * Aggiunge un'entità all Layer.
     * @param entity l'entità da aggiungere
     */
    public synchronized void add(Entity entity) {
        entitiesToAdd.addLast(entity);
    }

    @Override
    public int compareTo(Layer other) {
        return Integer.compare(this.nLayer, other.nLayer);
    }
}