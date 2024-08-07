package chevy.model.entity.dinamicEntity;

import chevy.model.entity.Entity;
import chevy.utils.Vector2;

import java.util.Random;

public enum DirectionsModel {
    DOWN(new Vector2<>(1, 0)), LEFT(new Vector2<>(0, -1)), RIGHT(new Vector2<>(0, 1)), UP(new Vector2<>(-1, 0));

    private final Vector2<Integer> direction;

    DirectionsModel(Vector2<Integer> direction) {
        this.direction = direction;
    }

    public static DirectionsModel getRandom() {
        Random random = new Random();
        DirectionsModel[] directionsModels = values();
        return directionsModels[random.nextInt(directionsModels.length)];
    }

    public static DirectionsModel positionToDirection(Entity a, Entity b) {
        Vector2<Integer> v = new Vector2<>(a.getRow(), a.getCol());
        Vector2<Integer> v2 = new Vector2<>(b.getRow(), b.getCol());
        return positionToDirection(v, v2);
    }

    public static DirectionsModel positionToDirection(Vector2<Integer> a, Vector2<Integer> b) {
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

    public DirectionsModel getOpposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }

    public Vector2<Integer> getVector2() { return direction; }

    public Integer col() { return direction.second; }

    public Integer row() { return direction.first; }
}