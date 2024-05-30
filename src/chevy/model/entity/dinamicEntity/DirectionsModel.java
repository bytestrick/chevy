package chevy.model.entity.dinamicEntity;

import chevy.utilz.Vector2;

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

    public Integer col() {
        return direction.first();
    }

    public Integer row() {
        return direction.second();
    }
}
