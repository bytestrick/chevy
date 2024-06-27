package chevy.model.entity.dinamicEntity.projectile;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.utilz.Vector2;

public class Arrow extends Projectile {

    public Arrow(Vector2<Integer> initPosition, DirectionsModel direction) {
        super(initPosition, Type.ARROW, direction, 1f);
    }
}
