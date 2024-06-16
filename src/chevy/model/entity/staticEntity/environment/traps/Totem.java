package chevy.model.entity.staticEntity.environment.traps;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.utilz.Vector2;

public class Totem extends Trap {
    private final DirectionsModel directionShot;


    public Totem(Vector2<Integer> initVelocity, DirectionsModel directionShot) {
        super(initVelocity, TrapsTypes.TOTEM);
        this.directionShot = directionShot;
        this.crossable = false;
        this.updateEverySecond = 10;
        this.canHitFlingEntity = true;
    }


    public DirectionsModel getDirectionShot() {
        return directionShot;
    }
}
