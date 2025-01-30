package chevy.model.chamber;

import chevy.utils.Log;

import java.awt.Dimension;
import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Heuristic algorithm for finding the shortest path
 */
final class AStar {
    private final Chamber chamber;
    private final Dimension size;

    AStar(Chamber chamber) {
        this.chamber = chamber;
        size = chamber.getSize();
    }

    /**
     * Returns the shortest path
     */
    private static List<Point> tracePath(Cell[][] cellDetails, Point dst) {
        List<Point> path = new LinkedList<>();

        // iterates backwards until it reaches the initial node
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

    List<Point> find(Point src, Point dst) {
        if (!chamber.isValid(src)) {
            Log.error("The starting point is not valid");
            return null;
        }
        if (!chamber.isValid(dst)) {
            Log.error("The destination point is not valid");
            return null;
        }
        if (src.equals(dst)) {
            Log.info("The starting point is the same as the destination point");
            return null;
        }

        boolean[][] closedList = new boolean[size.height][size.width]; // already visited cells
        Cell[][] cells = new Cell[size.height][size.width];
        PriorityQueue<Details> openList = new PriorityQueue<>(); // cells to visit

        final Point currentCell = new Point(src); // initializes the starting cell
        cells[currentCell.y][currentCell.x] = new Cell(new Point(currentCell));
        // Adds the first cell in the queue of nodes to explore with a heuristic function value equal
        openList.add(new Details(.0d, currentCell));

        while (!openList.isEmpty()) {
            Details details = openList.peek();
            currentCell.setLocation(details.position);
            openList.poll();
            closedList[currentCell.y][currentCell.x] = true;

            // Create and initialize the cells adjacent to the current cell
            for (int i = -1; i < 2; ++i) {
                for (int j = -1; j < 2; ++j) {

                    if (Math.abs(i + j) != 1) {
                        continue; // don't move in diagonal
                    }

                    final Point neighbor = new Point(currentCell.x + j, currentCell.y + i);
                    if (chamber.isValid(neighbor)) {
                        // create a row of the matrix if it does not exist
                        if (cells[neighbor.y] == null) {
                            cells[neighbor.y] = new Cell[size.width];
                        }
                        // create the cell if it does not exist
                        if (cells[neighbor.y][neighbor.x] == null) {
                            cells[neighbor.y][neighbor.x] = new Cell();
                        }

                        if (neighbor.equals(dst)) {
                            cells[neighbor.y][neighbor.x].parent = new Point(currentCell);
                            Log.info("Path found");
                            return tracePath(cells, dst);
                        } else if (!closedList[neighbor.y][neighbor.x] && chamber.isSafeToCross(neighbor)) {
                            // if the cell is not in the open list, or, if it is, it has a better
                            double gNew = cells[currentCell.y][currentCell.x].g + 1.0d;
                            // cost of the path up to the current node + 1
                            double hNew = neighbor.distance(dst);
                            double fNew = gNew + hNew;
                            // if the cell of the neighborhood does not have a value f, or, has a
                            if (cells[neighbor.y][neighbor.x].f == -1.0d || cells[neighbor.y][neighbor.x].f > fNew) {
                                // add a cell of the neighborhood as visitable
                                // update the values of the cell, of the neighborhood considered
                                openList.add(new Details(fNew, neighbor));
                                cells[neighbor.y][neighbor.x].set(new Point(currentCell), fNew, gNew);
                            }
                        }
                    }
                }
            }
        }
        Log.warn("Path not found");
        return null;
    }

    /**
     * Contains the necessary information to reconstruct the shortest path
     */
    private static class Cell {
        Point parent = new Point(-1, -1);
        double f = -1;
        /**
         * Cost of the exact path to the current node
         */
        double g = -1;

        Cell(Point parent) {set(parent, 0.0, 0.0);}

        Cell() {}

        void set(Point parent, double f, double g) {
            this.parent = parent;
            this.f = f;
            this.g = g;
        }
    }

    /**
     * Contains information about the node (row, col) {@link #f} is the value of the heuristic function
     */
    private record Details(double f, Point position) implements Comparable<Details> {

        @Override
        public int compareTo(Details o) {return (int) Math.round(f - o.f);}
    }
}
