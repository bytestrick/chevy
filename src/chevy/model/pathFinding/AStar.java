package chevy.model.pathFinding;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.utilz.Vector2;

import java.util.*;


public class AStar {
    private final Chamber chamber;
    private final int nRows;
    private final int nCols;
    
    
    public AStar(Chamber chamber) {
        this.chamber = chamber;
        this.nCols = chamber.getNCols();
        this.nRows = chamber.getNRows();
    }


    // Controlla se la coppia cell(row, col) è valida
    private boolean isValid(Vector2<Integer> cell) {
        return chamber.validatePosition(cell);
    }

    // Controlla se è possibile passare su quella cella
    private boolean isBlocked(Vector2<Integer> cell) {
        return !chamber.isSafeToCross(cell);
    }

    // Contolla se si è raggiunta la posizione obiettivo
    private boolean isDestination(Vector2<Integer> position, Vector2<Integer> dest) {
        return position.equals(dest);
    }

    // Calcolo della funzione euristica
    private double calculateHValue(Vector2<Integer> src, Vector2<Integer> dest) {
        // distanza tra i due punti
        return Math.sqrt(Math.pow((src.first - dest.first), 2) + Math.pow((src.second - dest.second), 2));
    }

    // Ricostruisce il path minimo
    private List<Vector2<Integer>> tracePath(Cell[][] cellDetails, Vector2<Integer> dest) {
        List<Vector2<Integer>> path = new LinkedList<>();

        int row = dest.first;
        int col = dest.second;
        Vector2<Integer> nextNode;
        do {
            path.addFirst(new Vector2<>(row, col));
            nextNode = cellDetails[row][col].parent;
            row = nextNode.first;
            col = nextNode.second;
        } while (cellDetails[row][col].parent != nextNode); // scorre a ritroso finché non arriva al nodo iniziale

        return path;
    }


    public List<Vector2<Integer>> find(Entity src, Entity dest) {
        return find(new Vector2<>(src.getRow(), src.getCol()), new Vector2<>(dest.getRow(), dest.getCol()));
    }


    public List<Vector2<Integer>> find(Vector2<Integer> src, Vector2<Integer> dest) {
        if (!isValid(src)) {
            System.out.println("Il punto di partenza non è valido");
            return null;
        }
        if (!isValid(dest)) {
            System.out.println("Il punto d'arrivo non è valido");
            return null;
        }
        if (isDestination(src, dest)) {
            System.out.println("Il punto d'arrivo coincide con il punto di partenza");
            return null;
        }

        boolean[][] closedList = new boolean[nRows][nCols]; // Celle già esplorate
        Cell[][] cellDetails = new Cell[nRows][nCols];
        PriorityQueue<Details> openList = new PriorityQueue<>(); // Creazione delle coda con priorità contenete row nodi da visitare

        // Inizializzazione della cella di partenza
        Vector2<Integer> currentCell = new Vector2<>(src.first, src.second);
        cellDetails[currentCell.first][currentCell.second] = new Cell();
        cellDetails[currentCell.first][currentCell.second].f = 0.0;
        cellDetails[currentCell.first][currentCell.second].g = 0.0;
        cellDetails[currentCell.first][currentCell.second].h = 0.0;
        cellDetails[currentCell.first][currentCell.second].parent = new Vector2<>( currentCell.first, currentCell.second );
        // Aggiunge la prima cella nella coda dei nodi da esplorare con valore della funzioine euristica pari a 0
        openList.add(new Details(0.0d, currentCell.first, currentCell.second));

        while (!openList.isEmpty()) {
            Details details = openList.peek();
            // Add to the closed list
            currentCell.first = details.row;
            currentCell.second = details.col;
            // Remove from the open list
            openList.poll();
            closedList[currentCell.first][currentCell.second] = true;

            // Crea e inizializza le celle adiacenti alla cella corrente
            for (int i = -1; i < 2; ++i) {
                for (int j = -1; j < 2; ++j) {

                    if (Math.abs(i + j) != 1) // non spostarti in diagonale
                        continue;

                    Vector2<Integer> neighbour = new Vector2<>(currentCell.first + i, currentCell.second + j);
                    if (isValid(neighbour)) {
                        // crea una riga della matrice se non esiste
                        if(cellDetails[neighbour.first] == null) {
                            cellDetails[neighbour.first] = new Cell[nCols];
                        }
                        // crea la cella se non c'è
                        if (cellDetails[neighbour.first][neighbour.second] == null) {
                            cellDetails[neighbour.first][neighbour.second] = new Cell();
                        }

                        if (isDestination(neighbour, dest)) {
                            cellDetails[neighbour.first][neighbour.second].parent = new Vector2<>(currentCell.first, currentCell.second);
                            System.out.println("Punto d'arrivo trovato");
                            return tracePath(cellDetails, dest);
                        }
                        // se la cella del vicinato non è stata esplorata e ci si può passare sopra
                        else if (!closedList[neighbour.first][neighbour.second] && !isBlocked(neighbour)) {
                            double gNew = cellDetails[currentCell.first][currentCell.second].g + 1.0d; // costo del cammino fino al nodo corrente + 1
                            double hNew = calculateHValue(neighbour, dest);
                            double fNew = gNew + hNew;
                            // se la cella del vicinato non ha valore f oppure ha un valore migliore
                            if (cellDetails[neighbour.first][neighbour.second].f == -1 || cellDetails[neighbour.first][neighbour.second].f > fNew) {
                                openList.add(new Details(fNew, neighbour.first, neighbour.second)); // aggiungi la cella del vicinato come visitabile
                                // aggiorna i valori della cella, del vicinato considerata
                                cellDetails[neighbour.first][neighbour.second].g = gNew;
                                cellDetails[neighbour.first][neighbour.second].h = hNew;
                                cellDetails[neighbour.first][neighbour.second].f = fNew;
                                cellDetails[neighbour.first][neighbour.second].parent = new Vector2<>(currentCell.first, currentCell.second);
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Percorso non trovato");
        return null;
    }
}