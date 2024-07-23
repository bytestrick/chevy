package chevy.model.entity.collectable;

import chevy.model.entity.Entity;
import chevy.utils.Vector2;

public class Coin extends Collectable {
    public Coin(Vector2<Integer> initPosition) {
        super(initPosition, Type.COIN);
    }
}
