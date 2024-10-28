package chevy.view.entities.animated.player;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.liveEntity.player.Archer;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player.State;
import chevy.model.entity.stateMachine.EntityState;
import chevy.view.entities.animated.LiveEntityView;

public final class ArcherView extends LiveEntityView {
    public ArcherView(Archer archer) {
        super(archer);

        final String res = "/sprites/player/archer/";
        final float idleDuration = 1f;
        final float moveDuration = entity.getState(State.MOVE).getDuration();
        final int[] attackDurationSizes = new int[]{6, 8, 4, 8};
        final float attackDuration = entity.getState(State.ATTACK).getDuration();
        final float hitDuration = entity.getState(State.HIT).getDuration();
        final float deadDuration = entity.getState(State.DEAD).getDuration();
        final float sludgeDuration = entity.getState(State.SLUDGE).getDuration();
        float fallDuration = entity.getState(State.FALL).getDuration();
        float glideDuration = entity.getState(State.GLIDE).getDuration();
        for (Direction direction : Direction.values()) {
            final String dir = direction.toString().toLowerCase();
            animate(State.IDLE, direction, 2, 2, idleDuration, res + "idle/" + dir);
            animate(State.MOVE, direction, 2, moveDuration, res + "move/" + dir);
            animate(State.ATTACK, direction, attackDurationSizes[direction.ordinal()],
                    attackDuration, res + "attack/" + dir);
            animate(State.HIT, direction, 4, hitDuration, res + "hit/" + dir);
            if (direction == Direction.RIGHT || direction == Direction.LEFT) {
                animate(State.DEAD, direction, 5, deadDuration, res + "dead/" + dir);
                animate(State.SLUDGE, direction, 1, sludgeDuration, res + "glide/" + dir);
                animate(State.FALL, direction, 2, fallDuration, res + "dead/" + dir);
                animate(State.GLIDE, direction, 3, glideDuration, res + "glide/" + dir);
            }
        }
    }

    @Override
    protected Direction getAnimationDirection(EntityState state) {
        Direction direction = entity.getDirection();
        return switch (state) {
            case State.ATTACK, State.IDLE, State.MOVE, State.HIT -> direction;
            case State.SLUDGE, State.GLIDE, State.FALL, State.DEAD ->
                    direction == Direction.LEFT ? direction : Direction.RIGHT;
            default -> null;
        };
    }
}
