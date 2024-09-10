package chevy.view.entities.animated.enemy;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Beetle;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Enemy.State;
import chevy.view.entities.animated.LiveEntityView;

import java.awt.Point;

public final class BeetleView extends LiveEntityView {
    public BeetleView(Beetle beetle) {
        super(beetle);

        final String res = "/sprites/enemy/beetle/";
        final float idleDuration = entity.getState(State.IDLE).getDuration();
        final float moveDuration = entity.getState(State.MOVE).getDuration();
        final float attackDuration = entity.getState(State.ATTACK).getDuration();
        final float hitDuration = entity.getState(State.HIT).getDuration();
        final Point offsetAttack = new Point(-1, -3);
        for (Direction direction : Direction.values()) {
            final String dir = direction.toString().toLowerCase();
            animate(State.IDLE, direction, 4, 4, idleDuration, res + "idle/" + dir);
            animate(State.MOVE, direction, 4, moveDuration, res + "move/" + dir);
            animate(State.ATTACK, direction, 4, attackDuration, offsetAttack, res + "attack/" + dir);
            animate(State.HIT, direction, 1, hitDuration, res + "hit/" + dir);
        }

        float deadDuration = entity.getState(State.DEAD).getDuration();
        animate(State.DEAD, Direction.LEFT, 4, deadDuration, res + "dead/left");
        animate(State.DEAD, Direction.RIGHT, 4, deadDuration, res + "dead/right");
    }
}