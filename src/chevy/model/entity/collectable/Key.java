package chevy.model.entity.collectable;

import chevy.utils.Vector2;

public class Key extends Collectable {
    public Key(Vector2<Integer> initPosition) {
        super(initPosition, Type.KEY);
    }
}
