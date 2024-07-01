package chevy.model.entity.staticEntity.environment.traps;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.utils.Vector2;

public class Totem extends Trap {
    private final DirectionsModel directionShot;


    public Totem(Vector2<Integer> initVelocity, DirectionsModel directionShot) {
        super(initVelocity, Type.TOTEM);
        this.directionShot = directionShot;
        this.crossable = false;
        this.mustBeUpdated = true;
    }


    public DirectionsModel getDirectionShot() {
        return directionShot;
    }
}
