package chevy.model.staticEntity.powerUp;

import chevy.model.staticEntity.Entity;
import chevy.utilz.Vector2;

public abstract class PowerUp extends Entity {
    PowerUpTypes type;

    public PowerUp(Vector2<Integer> initVelocity, PowerUpTypes type) {
        super(initVelocity);
        this.type = type;
    }

}
