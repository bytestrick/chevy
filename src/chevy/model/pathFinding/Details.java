package chevy.model.pathFinding;

/**
 * Contiene informazioni sul nodo (row, col)
 * f è il valore della funzione euristica
 */
public class Details implements Comparable<Details> {
    public double f;
    public int row;
    public int col;

    public Details(double f, int row, int col) {
        this.f = f;
        this.row = row;
        this.col = col;
    }


    @Override
    public int compareTo(Details o) {
        return (int) Math.round(f - o.f);
    }
}