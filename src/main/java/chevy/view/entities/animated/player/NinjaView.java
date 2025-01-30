package chevy.view.entities.animated.player;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.liveEntity.player.Ninja;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player.State;
import chevy.model.entity.stateMachine.EntityState;
import chevy.view.entities.animated.LiveEntityView;

public final class NinjaView extends LiveEntityView {
    public NinjaView(Ninja ninja) {
        super(ninja);

        String res = "/sprites/player/ninja/";
        float idleDuration = 1f;
        float moveDuration = entity.getState(State.MOVE).getDuration();
        float attackDuration = entity.getState(State.ATTACK).getDuration();
        float hitDuration = entity.getState(State.HIT).getDuration();
        float deadDuration = entity.getState(State.DEAD).getDuration();
        animate(State.DEAD, null, 1, deadDuration, res + "dead");
        float sludgeDuration = entity.getState(State.SLUDGE).getDuration();
        float fallDuration = entity.getState(State.FALL).getDuration();
        animate(State.FALL, null, 1, fallDuration, res + "dead");
        float glideDuration = entity.getState(State.GLIDE).getDuration();
        for (Direction direction : Direction.values()) {
            String dir = direction.toString().toLowerCase();
            animate(State.IDLE, direction, 2, 2, idleDuration, res + "idle/" + dir);
            animate(State.MOVE, direction, 3, moveDuration, res + "move/" + dir);
            animate(State.ATTACK, direction, direction == Direction.UP ? 2 : 3, attackDuration, res + "attack/" + dir);
            animate(State.HIT, direction, 1, hitDuration, res + "hit/" + dir);
            if (direction == Direction.RIGHT || direction == Direction.LEFT) {
                animate(State.SLUDGE, direction, 1, sludgeDuration, res + "idle/" + dir);
                animate(State.GLIDE, direction, 1, glideDuration, res + "glide/" + dir);
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
