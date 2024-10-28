package chevy.view.entities.animated.enemy;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Enemy.State;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.stateMachine.EntityState;
import chevy.view.entities.animated.LiveEntityView;

public final class SlimeView extends LiveEntityView {
    public SlimeView(Slime slime) {
        super(slime);

        final String res = "/sprites/enemy/slime/";
        float idleDuration = entity.getState(State.IDLE).getDuration();
        animate(State.IDLE, null, 4, 3, idleDuration, res + "idle");

        float moveDuration = entity.getState(State.MOVE).getDuration();
        animate(State.MOVE, null, 4, moveDuration, res + "move");

        float attackDuration = entity.getState(State.ATTACK).getDuration();
        for (Direction direction : Direction.values()) {
            final String dir = direction.toString().toLowerCase();
            animate(State.ATTACK, direction, 4, attackDuration, res + "attack/" + dir);
        }

        float hitDuration = entity.getState(State.HIT).getDuration();
        animate(State.HIT, null, 1, hitDuration, res + "hit");

        float deadDuration = entity.getState(State.DEAD).getDuration();
        animate(State.DEAD, null, 4, deadDuration, res + "dead");
    }

    @Override
    protected Direction getAnimationDirection(EntityState state) {
        return state == State.ATTACK ? entity.getDirection() : null;
    }
}
