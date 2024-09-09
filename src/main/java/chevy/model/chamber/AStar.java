package chevy.model.chamber;

import chevy.utils.Log;

import java.awt.Dimension;
import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Algoritmo euristico per la ricerca del cammino minimo
 */
final class AStar {
    private final Chamber chamber;
    private final Dimension size;

    AStar(Chamber chamber) {
        this.chamber = chamber;
        size = chamber.getSize();
    }

    /**
     * @return {@code true} se si è raggiunta la destinazione, {@code false} altrimenti
     */
    private static boolean isDestination(Point position, Point dst) {return position.equals(dst);}

    /**
     * Calcolo della funzione euristica
     */
    private static double calculateHValue(Point src, Point dst) {return src.distance(dst);}

    /**
     * Ricostruisce il percorso minimo
      */
    private static List<Point> tracePath(Cell[][] cellDetails, Point dst) {
        List<Point> path = new LinkedList<>();

        // scorre a ritroso finché non arriva al nodo iniziale
        int row = dst.y;
        int col = dst.x;
        Point nextNode;
        do {
            path.addFirst(new Point(col, row));
            nextNode = cellDetails[row][col].parent;
            row = nextNode.y;
            col = nextNode.x;
        } while (cellDetails[row][col].parent != nextNode);

        return path;
    }

    /**
     * @return true se la coppia cell(row, col) è valida, false altrimenti.
     */
    private boolean isValid(Point cell) {return chamber.validatePosition(cell);}

    /**
     * @return true se non è possibile passare su quella cella.
     */
    private boolean isBlocked(Point cell) {
        return !chamber.isSafeToCross(cell);
    }

    List<Point> find(Point src, Point dst) {
        if (!isValid(src)) {
            Log.error("Il punto di partenza non è valido");
            return null;
        }
        if (!isValid(dst)) {
            Log.error("Il punto d'arrivo non è valido");
            return null;
        }
        if (isDestination(src, dst)) {
            Log.info("Il punto d'arrivo coincide con il punto di partenza");
            return null;
        }

        boolean[][] closedList = new boolean[size.height][size.width]; // Celle già esplorate
        Cell[][] cellDetails = new Cell[size.height][size.width];
        PriorityQueue<Details> openList = new PriorityQueue<>(); // Celle da visitare

        // Inizializzazione della cella di partenza
        final Point currentCell = new Point(src);
        cellDetails[currentCell.y][currentCell.x] = new Cell();
        cellDetails[currentCell.y][currentCell.x].f = 0;
        cellDetails[currentCell.y][currentCell.x].g = 0;
        cellDetails[currentCell.y][currentCell.x].parent = new Point(currentCell);
        // Aggiunge la prima cella nella coda dei nodi da esplorare con valore della funzione
        // euristica pari a 0
        openList.add(new Details(0, currentCell));

        while (!openList.isEmpty()) {
            Details details = openList.peek();
            currentCell.setLocation(details.position);
            openList.poll();
            closedList[currentCell.y][currentCell.x] = true;

            // Crea e inizializza le celle adiacenti alla cella corrente
            for (int i = -1; i < 2; ++i) {
                for (int j = -1; j < 2; ++j) {

                    if (Math.abs(i + j) != 1) {
                        continue; // non spostarti in diagonale
                    }

                    final Point neighbor = new Point(currentCell);
                    neighbor.translate(j, i);
                    if (isValid(neighbor)) {
                        // crea una riga della matrice se non esiste
                        if (cellDetails[neighbor.y] == null) {
                            cellDetails[neighbor.y] = new Cell[size.width];
                        }
                        // crea la cella se non esiste
                        if (cellDetails[neighbor.y][neighbor.x] == null) {
                            cellDetails[neighbor.y][neighbor.x] = new Cell();
                        }

                        if (isDestination(neighbor, dst)) {
                            cellDetails[neighbor.y][neighbor.x].parent = new Point(currentCell);
                            Log.info("Punto d'arrivo trovato");
                            return tracePath(cellDetails, dst);
                        } else if (!closedList[neighbor.y][neighbor.x] && !isBlocked(neighbor)) {
                            // se la cella del vicinato non è stata esplorata e ci si può passare
                            // sopra

                            double gNew = cellDetails[currentCell.y][currentCell.x].g + 1;
                            // costo del cammino fino al nodo corrente + 1
                            double hNew = calculateHValue(neighbor, dst);
                            double fNew = gNew + hNew;
                            // se la cella del vicinato non ha valore f, oppure, ha un valore
                            // migliore
                            if (cellDetails[neighbor.y][neighbor.x].f == -1
                                    || cellDetails[neighbor.y][neighbor.x].f > fNew) {
                                openList.add(new Details(fNew, neighbor)); // aggiungi la
                                // cella del vicinato come visitabile
                                // aggiorna i valori della cella, del vicinato considerata
                                cellDetails[neighbor.y][neighbor.x].g = gNew;
                                cellDetails[neighbor.y][neighbor.x].f = fNew;
                                cellDetails[neighbor.y][neighbor.x].parent = new Point(currentCell);
                            }
                        }
                    }
                }
            }
        }
        Log.warn("Percorso non trovato");
        return null;
    }

    /**
     * Contiene le informazioni necessarie per ricostruire il cammino minimo
     */
    private static class Cell {
        Point parent = new Point(-1, -1);
        double f = -1;
        /** Costo esatto del cammino fino al nodo corrente */
        double g = -1;
    }

    /**
     * Contiene informazioni sul nodo (row, col) {@link #f} è il valore della funzione euristica
     */
    private record Details(double f, Point position) implements Comparable<Details> {

        @Override
        public int compareTo(Details o) {return (int) Math.round(f - o.f);}
    }
}