package chevy.model.entity.dinamicEntity;

import chevy.utilz.Vector2;

import java.util.Random;

public enum DirectionsModel {
    DOWN(new Vector2<>(1, 0)),
    LEFT(new Vector2<>(0, -1)),
    RIGHT(new Vector2<>(0, 1)),
    UP(new Vector2<>(-1, 0));


    private final Vector2<Integer> direction;

    DirectionsModel(Vector2<Integer> direction) {
        this.direction = direction;
    }

    public Vector2<Integer> getVector2() {
        return direction;
    }

    public static DirectionsModel getRandom() {
        Random random = new Random();
        DirectionsModel[] directionsModels = values();
        return directionsModels[random.nextInt(directionsModels.length)];
    }

    public Integer col() {
        return direction.second;
    }

    public Integer row() {
        return direction.first;
    }
}
