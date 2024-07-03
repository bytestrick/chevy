package chevy.model.pathFinding;

import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.utils.Log;
import chevy.utils.Vector2;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Algoritmo euristico per la ricerca del cammino minimo.
 */
public class AStar {
    private final Chamber chamber;
    private final int nRows;
    private final int nCols;

    public AStar(Chamber chamber) {
        this.chamber = chamber;
        this.nCols = chamber.getNCols();
        this.nRows = chamber.getNRows();
    }

    /**
     * @return true se la coppia cell(row, col) è valida, false altrimenti.
     */
    private boolean isValid(Vector2<Integer> cell) {
        return chamber.validatePosition(cell);
    }

    /**
     * @return true se non è possibile passare su quella cella.
     */
    private boolean isBlocked(Vector2<Integer> cell) {
        return !chamber.isSafeToCross(cell);
    }

    /**
     * @return true se si è raggiunta la destinazione, false altrimenti.
     */
    private boolean isDestination(Vector2<Integer> position, Vector2<Integer> destination) {
        return position.equals(destination);
    }

    /**
     * Calcolo della funzione euristica.
     */
    private double calculateHValue(Vector2<Integer> src, Vector2<Integer> destination) {
        // distanza tra i due punti
        return Math.sqrt(Math.pow((src.first - destination.first), 2) + Math.pow((src.second - destination.second), 2));
    }

    // Ricostruisce il path minimo
    private List<Vector2<Integer>> tracePath(Cell[][] cellDetails, Vector2<Integer> destination) {
        List<Vector2<Integer>> path = new LinkedList<>();

        // scorre a ritroso finché non arriva al nodo iniziale
        int row = destination.first;
        int col = destination.second;
        Vector2<Integer> nextNode;
        do {
            path.addFirst(new Vector2<>(row, col));
            nextNode = cellDetails[row][col].parent;
            row = nextNode.first;
            col = nextNode.second;
        } while (cellDetails[row][col].parent != nextNode);

        return path;
    }

    public List<Vector2<Integer>> find(Entity src, Entity destination) {
        return find(new Vector2<>(src.getRow(), src.getCol()), new Vector2<>(destination.getRow(),
                destination.getCol()));
    }

    public List<Vector2<Integer>> find(Vector2<Integer> src, Vector2<Integer> destination) {
        if (!isValid(src)) {
            Log.error("Il punto di partenza non è valido");
            return null;
        }
        if (!isValid(destination)) {
            Log.error("Il punto d'arrivo non è valido");
            return null;
        }
        if (isDestination(src, destination)) {
            Log.info("Il punto d'arrivo coincide con il punto di partenza");
            return null;
        }

        boolean[][] closedList = new boolean[nRows][nCols]; // Celle già esplorate
        Cell[][] cellDetails = new Cell[nRows][nCols];
        PriorityQueue<Details> openList = new PriorityQueue<>(); // Celle da visitare

        // Inizializzazione della cella di partenza
        Vector2<Integer> currentCell = new Vector2<>(src.first, src.second);
        cellDetails[currentCell.first][currentCell.second] = new Cell();
        cellDetails[currentCell.first][currentCell.second].f = 0.0;
        cellDetails[currentCell.first][currentCell.second].g = 0.0;
        cellDetails[currentCell.first][currentCell.second].h = 0.0;
        cellDetails[currentCell.first][currentCell.second].parent = new Vector2<>(currentCell.first,
                currentCell.second);
        // Aggiunge la prima cella nella coda dei nodi da esplorare con valore della funzione euristica pari a 0
        openList.add(new Details(0.0d, currentCell.first, currentCell.second));

        while (!openList.isEmpty()) {
            Details details = openList.peek();
            currentCell.first = details.row;
            currentCell.second = details.col;
            openList.poll();
            closedList[currentCell.first][currentCell.second] = true;

            // Crea e inizializza le celle adiacenti alla cella corrente
            for (int i = -1; i < 2; ++i) {
                for (int j = -1; j < 2; ++j) {

                    if (Math.abs(i + j) != 1) {
                        continue; // non spostarti in diagonale
                    }

                    Vector2<Integer> neighbor = new Vector2<>(currentCell.first + i, currentCell.second + j);
                    if (isValid(neighbor)) {
                        // crea una riga della matrice se non esiste
                        if (cellDetails[neighbor.first] == null) {
                            cellDetails[neighbor.first] = new Cell[nCols];
                        }
                        // crea la cella se non esiste
                        if (cellDetails[neighbor.first][neighbor.second] == null) {
                            cellDetails[neighbor.first][neighbor.second] = new Cell();
                        }

                        if (isDestination(neighbor, destination)) {
                            cellDetails[neighbor.first][neighbor.second].parent = new Vector2<>(currentCell.first,
                                    currentCell.second);
                            Log.info("Punto d'arrivo trovato");
                            return tracePath(cellDetails, destination);
                        } else if (!closedList[neighbor.first][neighbor.second] && !isBlocked(neighbor)) {
                            // se la cella del vicinato non è stata esplorata e ci si può passare sopra

                            double gNew = cellDetails[currentCell.first][currentCell.second].g + 1.0d; // costo del
                            // cammino fino al nodo corrente + 1
                            double hNew = calculateHValue(neighbor, destination);
                            double fNew = gNew + hNew;
                            // se la cella del vicinato non ha valore f, oppure, ha un valore migliore
                            if (cellDetails[neighbor.first][neighbor.second].f == -1 || cellDetails[neighbor.first][neighbor.second].f > fNew) {
                                openList.add(new Details(fNew, neighbor.first, neighbor.second)); // aggiungi la
                                // cella del vicinato come visitabile
                                // aggiorna i valori della cella, del vicinato considerata
                                cellDetails[neighbor.first][neighbor.second].g = gNew;
                                cellDetails[neighbor.first][neighbor.second].h = hNew;
                                cellDetails[neighbor.first][neighbor.second].f = fNew;
                                cellDetails[neighbor.first][neighbor.second].parent =
                                        new Vector2<>(currentCell.first, currentCell.second);
                            }
                        }
                    }
                }
            }
        }
        Log.warn("Percorso non trovato");
        return null;
    }
}