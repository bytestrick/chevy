package chevy.view.entities.animated.player;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.liveEntity.player.Archer;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player.State;
import chevy.model.entity.stateMachine.EntityState;
import chevy.view.entities.animated.LiveEntityView;

public final class ArcherView extends LiveEntityView {
    private static final String RES = "/sprites/player/archer/";

    public ArcherView(Archer archer) {super(archer);}

    @Override
    protected void initializeAnimation() {
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
            animate(State.IDLE, direction, 2, 2, idleDuration, RES + "idle/" + dir);
            animate(State.MOVE, direction, 2, moveDuration, RES + "move/" + dir);
            animate(State.ATTACK, direction, attackDurationSizes[direction.ordinal()],
                    attackDuration, RES + "attack/" + dir);
            animate(State.HIT, direction, 4, hitDuration, RES + "hit/" + dir);
            if (direction == Direction.RIGHT || direction == Direction.LEFT) {
                animate(State.DEAD, direction, 5, deadDuration, RES + "dead/" + dir);
                animate(State.SLUDGE, direction, 1, sludgeDuration, RES + "glide/" + dir);
                animate(State.FALL, direction, 2, fallDuration, RES + "dead/" + dir);
                animate(State.GLIDE, direction, 3, glideDuration, RES + "glide/" + dir);
            }
        }
    }

    @Override
    protected Direction getAnimationDirection(EntityState state) {
        final Direction direction = entity.getDirection();
        return switch (state) {
            case State.ATTACK, State.IDLE, State.MOVE, State.HIT -> direction;
            case State.GLIDE, State.SLUDGE, State.DEAD, State.FALL ->
                    direction == Direction.LEFT ? direction : Direction.RIGHT;
            default -> null;
        };
    }
}