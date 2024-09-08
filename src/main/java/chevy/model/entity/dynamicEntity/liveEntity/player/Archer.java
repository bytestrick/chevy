package chevy.model.entity.dynamicEntity.liveEntity.player;

import chevy.utils.Vector2;

public final class Archer extends Player {

    public Archer(Vector2<Integer> initPosition) {
        super(initPosition, Type.ARCHER, .15f, 8, 0, 5, 3, .6f, .2f, .3f);
        stateMachine.setName("Archer");
        initStateMachine();
    }
}