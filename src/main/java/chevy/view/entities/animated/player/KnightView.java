package chevy.view.entities.animated.player;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.liveEntity.player.Knight;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player.State;
import chevy.model.entity.stateMachine.EntityState;
import chevy.utils.Vector2;
import chevy.view.entities.animated.LiveEntityView;

public final class KnightView extends LiveEntityView {
    private static final String RES = "/sprites/player/knight/";

    public KnightView(Knight knight) {super(knight);}

    @Override
    protected void initializeAnimation() {
        final Vector2<Integer> offset = new Vector2<>(-8, -8);
        final float idleDuration = 1f;
        final float moveDuration = entity.getState(State.MOVE).getDuration();
        final float attackDuration = entity.getState(State.ATTACK).getDuration();
        final float hitDuration = entity.getState(State.HIT).getDuration();
        final float deadDuration = entity.getState(State.DEAD).getDuration();
        final float sludgeDuration = entity.getState(State.SLUDGE).getDuration();
        float fallDuration = entity.getState(State.FALL).getDuration();
        float glideDuration = entity.getState(State.GLIDE).getDuration();
        for (Direction direction : Direction.values()) {
            final String dir = direction.toString().toLowerCase();
            animate(State.IDLE, direction, 2, true, 2, idleDuration, offset, 1,
                    RES + "idle/" + dir);
            animate(State.MOVE, direction, 8, moveDuration, offset, RES + "move/" + dir);
            animate(State.ATTACK, direction, 6, attackDuration, offset, RES + "attack/" + dir);
            animate(State.HIT, direction, 1, hitDuration, offset, RES + "hit/" + dir);
            if (direction == Direction.RIGHT || direction == Direction.LEFT) {
                animate(State.DEAD, direction, 8, deadDuration, offset, RES + "dead/" + dir);
                animate(State.SLUDGE, direction, 1, sludgeDuration, offset, RES + "idle/" + dir);
                animate(State.FALL, direction, 2, fallDuration, offset, RES + "dead/" + dir);
                animate(State.GLIDE, direction, 1, glideDuration, offset, RES + "idle/" + dir);
            }
        }
    }

    @Override
    protected Direction getAnimationDirection(EntityState state) {
        Direction direction = entity.getDirection();
        return switch (state) {
            case State.ATTACK, State.IDLE, State.MOVE, State.HIT -> direction;
            case State.SLUDGE, State.GLIDE, State.DEAD, State.FALL ->
                    direction == Direction.LEFT ? direction : Direction.RIGHT;
            default -> null;
        };
    }
}