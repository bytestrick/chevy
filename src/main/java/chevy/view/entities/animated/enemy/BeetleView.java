package chevy.view.entities.animated.enemy;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Beetle;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Enemy.State;
import chevy.utils.Vector2;
import chevy.view.entities.animated.LiveEntityView;

public final class BeetleView extends LiveEntityView {
    private static final String RES = "/sprites/enemy/beetle/";

    public BeetleView(Beetle beetle) {super(beetle);}

    @Override
    protected void initializeAnimation() {
        final float idleDuration = entity.getState(State.IDLE).getDuration();
        final float moveDuration = entity.getState(State.MOVE).getDuration();
        final float attackDuration = entity.getState(State.ATTACK).getDuration();
        final float hitDuration = entity.getState(State.HIT).getDuration();
        final Vector2<Integer> offsetAttack = new Vector2<>(-1, -3);
        for (Direction direction : Direction.values()) {
            final String dir = direction.toString().toLowerCase();
            animate(State.IDLE, direction, 4, 4, idleDuration, RES + "idle/" + dir);
            animate(State.MOVE, direction, 4, moveDuration, RES + "move/" + dir);
            animate(State.ATTACK, direction, 4, attackDuration, offsetAttack, RES +
                    "attack/" + dir);
            animate(State.HIT, direction, 1, hitDuration, RES + "hit/" + dir);
        }

        float deadDuration = entity.getState(State.DEAD).getDuration();
        animate(State.DEAD, Direction.LEFT, 4, deadDuration, RES + "dead/left");
        animate(State.DEAD, Direction.RIGHT, 4, deadDuration, RES + "dead/right");
    }
}