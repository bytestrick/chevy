package chevy.model.entity.staticEntity.environment;

import chevy.utils.Vector2;

public class Chest extends Environment {
    public Chest(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.CHEST);
    }
}
