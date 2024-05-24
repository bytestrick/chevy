package chevy.model.dinamicEntity;

import chevy.utilz.Vector2;

public enum Directions {
    DOWN(new Vector2<>(0, 1)),
    LEFT(new Vector2<>(-1, 0)),
    RIGHT(new Vector2<>(1, 0)),
    UP(new Vector2<>(0, -1));


    private final Vector2<Integer> direction;

    Directions(Vector2<Integer> direction) {
        this.direction = direction;
    }

    public Vector2<Integer> getVector2() {
        return direction;
    }
}
