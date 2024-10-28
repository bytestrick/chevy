package chevy.model.entity.dynamicEntity;

import chevy.model.entity.Entity;
import chevy.utils.Utils;

import java.awt.Point;

/**
 * Punti cardinali
 */
public enum Direction {
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0);

    public final int x, y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Direction getRandom() {
        Direction[] directions = values();
        return directions[Utils.random.nextInt(directions.length)];
    }

    public static Direction positionToDirection(Entity a, Entity b) {
        return positionToDirection(a.getPosition(), b.getPosition());
    }

    public static Direction positionToDirection(Point a, Point b) {
        final int x = b.x - a.x,
                  y = b.y - a.y;

        if (x == -1 && y == 0) {
            return LEFT;
        } else if (x == 0 && y == 1) {
            return DOWN;
        } else if (x == 1 && y == 0) {
            return RIGHT;
        } else if (x == 0 && y == -1) {
            return UP;
        }
        return null;
    }

    /**
     * Avanza una posizione di una cella nella direzione corrente
     *
     * @param position posizione corrente
     * @return la posizione successiva
     */
    public Point advance(Point position) {
        position.translate(x, y);
        return position;
    }

    public Direction getOpposite() {
        return switch (this) {
            case UP -> DOWN;
            case LEFT -> RIGHT;
            case DOWN -> UP;
            case RIGHT -> LEFT;
        };
    }
}
