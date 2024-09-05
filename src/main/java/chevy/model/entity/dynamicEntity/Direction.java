package chevy.model.entity.dynamicEntity;

import chevy.model.entity.Entity;
import chevy.utils.Utils;
import chevy.utils.Vector2;

public enum Direction {
    DOWN(new Vector2<>(1, 0)),
    LEFT(new Vector2<>(0, -1)),
    RIGHT(new Vector2<>(0, 1)),
    UP(new Vector2<>(-1, 0));

    private final Vector2<Integer> direction;

    Direction(Vector2<Integer> direction) {
        this.direction = direction;
    }

    public static Direction getRandom() {
        Direction[] directions = values();
        return directions[Utils.random.nextInt(directions.length)];
    }

    public static Direction positionToDirection(Entity a, Entity b) {
        return positionToDirection(a.getPosition(), b.getPosition());
    }

    public static Direction positionToDirection(Vector2<Integer> a, Vector2<Integer> b) {
        int i = b.first - a.first;
        int j = b.second - a.second;

        if (i == 1 && j == 0) {
            return DOWN;
        } else if (i == -1 && j == 0) {
            return UP;
        } else if (i == 0 && j == 1) {
            return RIGHT;
        } else if (i == 0 && j == -1) {
            return LEFT;
        }
        return null;
    }

    public Direction getOpposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }

    public Integer col() {return direction.second;}

    public Integer row() {return direction.first;}
}