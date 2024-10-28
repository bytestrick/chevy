package chevy.view.entities.animated.player;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.liveEntity.player.Knight;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player.State;
import chevy.model.entity.stateMachine.EntityState;
import chevy.view.entities.animated.LiveEntityView;

import java.awt.Point;

public final class KnightView extends LiveEntityView {
    public KnightView(Knight knight) {
        super(knight);

        final String res = "/sprites/player/knight/";
        final Point offset = new Point(-8, -8);
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
                    res + "idle/" + dir);
            animate(State.MOVE, direction, 8, moveDuration, offset, res + "move/" + dir);
            animate(State.ATTACK, direction, 6, attackDuration, offset, res + "attack/" + dir);
            animate(State.HIT, direction, 1, hitDuration, offset, res + "hit/" + dir);
            if (direction == Direction.RIGHT || direction == Direction.LEFT) {
                animate(State.DEAD, direction, 8, deadDuration, offset, res + "dead/" + dir);
                animate(State.SLUDGE, direction, 1, sludgeDuration, offset, res + "idle/" + dir);
                animate(State.FALL, direction, 2, fallDuration, offset, res + "dead/" + dir);
                animate(State.GLIDE, direction, 1, glideDuration, offset, res + "idle/" + dir);
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
