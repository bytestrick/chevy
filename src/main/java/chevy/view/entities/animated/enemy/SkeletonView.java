package chevy.view.entities.animated.enemy;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Enemy.State;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Skeleton;
import chevy.view.entities.animated.LiveEntityView;

public final class SkeletonView extends LiveEntityView {
    public SkeletonView(Skeleton skeleton) {
        super(skeleton);

        final String res = "/sprites/enemy/skeleton/";
        float idleDuration = entity.getState(State.IDLE).getDuration();
        float moveDuration = entity.getState(State.MOVE).getDuration();
        float attackDuration = entity.getState(State.ATTACK).getDuration();
        float hitDuration = entity.getState(State.HIT).getDuration();
        for (Direction direction : Direction.values()) {
            final String dir = direction.toString().toLowerCase();
            animate(State.IDLE, direction, 4, 3, idleDuration, res + "idle/" + dir);
            animate(State.MOVE, direction, 4, moveDuration, res + "move/" + dir);
            animate(State.ATTACK, direction, 4, attackDuration, res + "attack/" + dir);
            animate(State.HIT, direction, 1, hitDuration, res + "hit/" + dir);
        }

        float deadDuration = entity.getState(State.DEAD).getDuration();
        animate(State.DEAD, Direction.LEFT, 4, deadDuration, res + "dead/left");
        animate(State.DEAD, Direction.RIGHT, 4, deadDuration, res + "dead/right");
    }
}