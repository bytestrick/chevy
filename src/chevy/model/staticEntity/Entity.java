package chevy.model.staticEntity;

import chevy.utilz.Vector2;


public abstract class Entity {
    protected Vector2<Integer> velocity;

    public Entity(Vector2<Integer> initVelocity) {
        this.velocity = initVelocity;
    }
}
