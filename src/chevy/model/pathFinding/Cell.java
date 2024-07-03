package chevy.model.pathFinding;

import chevy.utils.Vector2;

/**
 * Contiene le informazioni necessarie per ricostruire il cammino minimo
 */
public class Cell {
    public Vector2<Integer> parent;
    public double f; // valore di valutazione f = h + g
    public double g; // costo esatto del cammino fino al nodo corrente
    public double h; // costo stimato del cammino fino all'obiettivo

    public Cell() {
        // -1 valore di non inizializzato
        this.parent = new Vector2<>(-1, -1);
        this.g = -1;
        this.h = -1;
        this.f = -1;
    }
}