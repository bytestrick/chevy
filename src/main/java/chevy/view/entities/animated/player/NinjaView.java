package chevy.view.entities.animated.player;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.liveEntity.player.Ninja;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player.State;
import chevy.model.entity.stateMachine.EntityState;
import chevy.view.entities.animated.LiveEntityView;

public final class NinjaView extends LiveEntityView {
    private static final String RES = "/sprites/player/ninja/";

    public NinjaView(Ninja ninja) {super(ninja);}

    @Override
    protected void initializeAnimation() {
        final float idleDuration = 1f;
        final float moveDuration = entity.getState(State.MOVE).getDuration();
        final float attackDuration = entity.getState(State.ATTACK).getDuration();
        final float hitDuration = entity.getState(State.HIT).getDuration();
        final float deadDuration = entity.getState(State.DEAD).getDuration();
        animate(State.DEAD, null, 1, deadDuration, RES + "dead");
        final float sludgeDuration = entity.getState(State.SLUDGE).getDuration();
        float fallDuration = entity.getState(State.FALL).getDuration();
        animate(State.FALL, null, 1, fallDuration, RES + "dead");
        float glideDuration = entity.getState(State.GLIDE).getDuration();
        for (Direction direction : Direction.values()) {
            final String dir = direction.toString().toLowerCase();
            animate(State.IDLE, direction, 2, 2, idleDuration, RES + "idle/" + dir);
            animate(State.MOVE, direction, 3, moveDuration, RES + "move/" + dir);
            animate(State.ATTACK, direction, direction == Direction.UP ? 2 : 3, attackDuration, RES + "attack/" + dir);
            animate(State.HIT, direction, 1, hitDuration, RES + "hit/" + dir);
            if (direction == Direction.RIGHT || direction == Direction.LEFT) {
                animate(State.SLUDGE, direction, 1, sludgeDuration, RES + "idle/" + dir);
                animate(State.GLIDE, direction, 1, glideDuration, RES + "glide/" + dir);
            }
        }
    }

    @Override
    protected Direction getAnimationDirection(EntityState state) {
        Direction direction = entity.getDirection();
        return switch (state) {
            case State.ATTACK, State.IDLE, State.MOVE, State.HIT -> direction;
            case State.SLUDGE, State.GLIDE -> direction == Direction.LEFT ? direction : Direction.RIGHT;
            default -> null;
        };
    }
}