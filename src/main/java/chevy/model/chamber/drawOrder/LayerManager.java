package chevy.model.chamber.drawOrder;

import chevy.model.entity.Entity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * La classe LayerManager gestisce l'ordine di disegno delle entità nel gioco.
 * Utilizza una lista di Layer, ognuno dei quali contiene un insieme di entità da disegnare.
 */
public final class LayerManager {
    private final List<Layer> drawOrder;

    public LayerManager() {
        this.drawOrder = new LinkedList<>();
    }

    /**
     * Trova un Layer specifico nella lista. Se il Layer non esiste, lo crea e lo aggiunge alla lista.
     *
     * @param nLayer priorità di ridisegno del livello da trovare.
     * @return ll Layer trovato o creato.
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
     * Aggiunge un'entità a un Layer specifico.
     *
     * @param entity L'entità da aggiungere.
     * @param nLayer Il numero del Layer a cui aggiungere l'entità.
     */
    public synchronized void add(Entity entity, int nLayer) {
        Layer layer = findLayer(nLayer);
        layer.add(entity);
    }

    public synchronized List<Layer> getDrawOrder() { return drawOrder; }
}