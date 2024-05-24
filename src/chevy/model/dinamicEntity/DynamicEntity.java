package chevy.model.dinamicEntity;

import chevy.model.staticEntity.Entity;
import chevy.utilz.Vector2;

public abstract class DynamicEntity extends Entity {
    public DynamicEntity(Vector2<Integer> initVelocity) {
        super(initVelocity);
    }

    public void changeVelocity(Vector2<Integer> velocity) {
        getVelocity().changePosition(velocity);
    }

    public void move(Directions direction) {

    }
}


