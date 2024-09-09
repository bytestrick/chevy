package chevy.view.entities.animated.enemy;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.BigSlime;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Enemy.State;
import chevy.model.entity.stateMachine.EntityState;
import chevy.view.entities.animated.LiveEntityView;

public final class BigSlimeView extends LiveEntityView {
    private static final String RES = "/sprites/enemy/bigSlime/";

    public BigSlimeView(BigSlime bigSlime) {super(bigSlime);}

    @Override
    protected void initializeAnimation() {
        float idleDuration = entity.getState(State.IDLE).getDuration();
        animate(State.IDLE, null, 4, 2, idleDuration, RES + "idle");

        float moveDuration = entity.getState(State.MOVE).getDuration();
        animate(State.MOVE, null, 4, moveDuration, RES + "move");

        float attackDuration = entity.getState(State.ATTACK).getDuration();
        for (Direction direction : Direction.values()) {
            final String path = RES + "attack/" + direction.toString().toLowerCase();
            animate(State.ATTACK, direction, 4, attackDuration, path);
        }

        float hitDuration = entity.getState(State.HIT).getDuration();
        animate(State.HIT, null, 1, hitDuration, RES + "hit");

        float deadDuration = entity.getState(State.DEAD).getDuration();
        animate(State.DEAD, null, 4, deadDuration, RES + "dead");
    }

    @Override
    protected Direction getAnimationDirection(EntityState state) {
        return state == State.ATTACK ? entity.getDirection() : null;
    }
}