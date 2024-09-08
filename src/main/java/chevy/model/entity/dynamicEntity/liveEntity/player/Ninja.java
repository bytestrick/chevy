package chevy.model.entity.dynamicEntity.liveEntity.player;

import chevy.utils.Vector2;

public final class Ninja extends Player {
    public Ninja(Vector2<Integer> initPosition) {
        super(initPosition, Type.NINJA, .1f, 6, 2, 5, 4, .3f, .2f, .3f);
        stateMachine.setName("Ninja");
        initStateMachine();
    }
}
