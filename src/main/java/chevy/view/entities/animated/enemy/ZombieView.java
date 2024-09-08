package chevy.view.entities.animated.enemy;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Zombie;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Enemy.State;
import chevy.view.entities.animated.LiveEntityView;

public final class ZombieView extends LiveEntityView {
    private static final String RES = "/sprites/enemy/zombie/";

    public ZombieView(Zombie zombie) {super(zombie);}

    @Override
    protected void initializeAnimation() {
        float idleDuration = entity.getState(State.IDLE).getDuration();
        float moveDuration = entity.getState(State.MOVE).getDuration();
        float attackDuration = entity.getState(State.ATTACK).getDuration();
        float hitDuration = entity.getState(State.HIT).getDuration();
        for (Direction direction : Direction.values()) {
            final String dir = direction.toString().toLowerCase();
            animate(State.IDLE, direction, 4, 3, idleDuration, RES + "idle/" + dir);
            animate(State.MOVE, direction, 4, moveDuration, RES + "move/" + dir);
            animate(State.ATTACK, direction, 4, attackDuration, RES + "attack/" + dir);
            animate(State.HIT, direction, 1, hitDuration, RES + "hit/" + dir);
        }

        float deadDuration = entity.getState(State.DEAD).getDuration();
        animate(State.DEAD, Direction.LEFT, 4, deadDuration, RES + "dead/left");
        animate(State.DEAD, Direction.RIGHT, 4, deadDuration, RES + "dead/right");
    }
}