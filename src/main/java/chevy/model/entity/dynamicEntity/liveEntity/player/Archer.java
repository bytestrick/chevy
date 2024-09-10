package chevy.model.entity.dynamicEntity.liveEntity.player;

import java.awt.Point;

public final class Archer extends Player {
    public Archer(Point initPosition) {
        super(initPosition, Type.ARCHER, .15f, 8, 0, 5, 3, .6f, .3f);
        fall.linkVertex(hit);
        stateMachine.setName("Archer");
    }
}