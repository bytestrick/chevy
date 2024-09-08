package chevy.model.entity.dynamicEntity;

import chevy.model.entity.Entity;
import chevy.utils.Utils;
import chevy.utils.Vector2;

/**
 * Punti cardinali
 */
public enum Direction {
    UP(new Vector2<>(-1, 0)),
    RIGHT(new Vector2<>(0, 1)),
    DOWN(new Vector2<>(1, 0)),
    LEFT(new Vector2<>(0, -1));

    private final Vector2<Integer> direction;

    Direction(Vector2<Integer> direction) {this.direction = direction;}

    public static Direction getRandom() {
        Direction[] directions = values();
        return directions[Utils.random.nextInt(directions.length)];
    }

    public static Direction positionToDirection(Entity a, Entity b) {
        return positionToDirection(a.getPosition(), b.getPosition());
    }

    public static Direction positionToDirection(Vector2<Integer> a, Vector2<Integer> b) {
        final int x = b.first - a.first, y = b.second - a.second;

        if (x == -1 && y == 0) {
            return UP;
        } else if (x == 0 && y == 1) {
            return RIGHT;
        } else if (x == 1 && y == 0) {
            return DOWN;
        } else if (x == 0 && y == -1) {
            return LEFT;
        }
        return null;
    }

    public Direction getOpposite() {
        return switch (this) {
            case UP -> DOWN;
            case LEFT -> RIGHT;
            case DOWN -> UP;
            case RIGHT -> LEFT;
        };
    }

    public Integer col() {return direction.second;}

    public Integer row() {return direction.first;}
}