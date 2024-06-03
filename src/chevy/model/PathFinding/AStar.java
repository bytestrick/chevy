package chevy.model.PathFinding;
import chevy.utilz.Vector2;

import java.util.PriorityQueue;
import java.util.Stack;


public class AStar {
    // Controlla se la coppia cell(row, col) è valida
    boolean isValid(int rows, int cols, Vector2<Integer> cell) {
        if (rows > 0 && cols > 0) // positivi e non 0
            return (cell.first >= 0) && (cell.first < rows)
                    && (cell.second >= 0) && (cell.second < cols);

        return false;
    }

    // Controlla se è possibile passare su quella cella
    boolean isBlocked(int[][] grid, int rows, int cols, Vector2<Integer> point) {
        return !isValid(rows, cols, point) || grid[point.first][point.second] == 0;
    }

    // Contolla se si è raggiunta la posizione obiettivo
    boolean isDestination(Vector2<Integer> position, Vector2<Integer> dest) {
        return position.equals(dest);
    }

    // Calcolo della funzione euristica
    double calculateHValue(Vector2<Integer> src, Vector2<Integer> dest) {
        // distanza tra i due punti
        return Math.sqrt(Math.pow((src.first - dest.first), 2) + Math.pow((src.second - dest.second), 2));
    }

    // Ricostruisce il path minimo
    void tracePath(Cell[][] cellDetails, Vector2<Integer> dest) {
        System.out.println("Percorso:  ");

        Stack<Vector2<Integer>> path = new Stack<>();

        int row = dest.first;
        int col = dest.second;
        Vector2<Integer> nextNode;
        do {
            path.push(new Vector2<>(row, col));
            nextNode = cellDetails[row][col].parent;
            row = nextNode.first;
            col = nextNode.second;
        } while (cellDetails[row][col].parent != nextNode); // scorre a ritroso finché non arriva al nodo iniziale


        // stampa il percorso
        while (!path.empty()) {
            Vector2<Integer> p = path.peek();
            path.pop();
            System.out.println("-> (" + p.first + "," + p.second + ") ");
        }
    }


    void find(int[][] grid, int rows, int cols, Vector2<Integer> src, Vector2<Integer> dest) {
        if (!isValid(rows, cols, src)) {
            System.out.println("Il punto di partenza non è valido");
            return;
        }
        if (!isValid(rows, cols, dest)) {
            System.out.println("Il punto d'arrivo non è valido");
            return;
        }
        if (isBlocked(grid, rows, cols, src)
                || isBlocked(grid, rows, cols, dest)) {
            System.out.println("Il punto di partenza o il punto d'arrivo sono celle dove non è possibile passare");
            return;
        }
        if (isDestination(src, dest)) {
            System.out.println("Il punto d'arrivo coincide con il punto di partenza");
            return;
        }

        boolean[][] closedList = new boolean[rows][cols]; // Celle già esplorate
        Cell[][] cellDetails = new Cell[rows][cols];
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
                    if (isValid(rows, cols, neighbour)) {
                        // crea una riga della matrice se non esiste
                        if(cellDetails[neighbour.first] == null) {
                            cellDetails[neighbour.first] = new Cell[cols];
                        }
                        // crea la cella se non c'è
                        if (cellDetails[neighbour.first][neighbour.second] == null) {
                            cellDetails[neighbour.first][neighbour.second] = new Cell();
                        }

                        if (isDestination(neighbour, dest)) {
                            cellDetails[neighbour.first][neighbour.second].parent = new Vector2<>(currentCell.first, currentCell.second);
                            System.out.println("Punto d'arrivo trovato");
                            tracePath(cellDetails, dest);
                            return;
                        }
                        // se la cella del vicinato non è stata esplorata e ci si può passare sopra
                        else if (!closedList[neighbour.first][neighbour.second] && !isBlocked(grid, rows, cols, neighbour)) {
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
    }


    public static void main(String[] args) {
        //0: The cell is blocked
        // 1: The cell is not blocked

        int[][] grid = {
                { 1, 1, 1, 1, 1, 1, 1, 1 },
                { 1, 0, 1, 1, 1, 1, 1, 1 },
                { 1, 0, 0, 1, 1, 1, 1, 1 },
                { 1, 0, 1, 0, 1, 1, 1, 1 },
                { 1, 0, 1, 1, 0, 1, 1, 1 },
                { 1, 0, 1, 1, 1, 0, 1, 1 },
                { 1, 0, 1, 1, 1, 1, 0, 0 },
                { 1, 1, 1, 1, 1, 1, 1, 1 }

        };


        // Punto d'inizio
        Vector2<Integer> src = new Vector2<>(0, 0);
        // Punto d'arrivo
        Vector2<Integer> dest = new Vector2<>(1, 1);

        AStar app = new AStar();
        app.find(grid, grid.length , grid[0].length, src, dest);

    }
}