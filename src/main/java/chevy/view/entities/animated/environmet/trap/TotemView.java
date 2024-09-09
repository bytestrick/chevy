package chevy.view.entities.animated.environmet.trap;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.staticEntity.environment.traps.Totem;
import chevy.model.entity.staticEntity.environment.traps.Totem.State;
import chevy.utils.Vector2;

import java.awt.image.BufferedImage;

public final class TotemView extends TrapView {
    private static final String RES = "/sprites/traps/totem/";

    public TotemView(Totem totem) {super(totem);}

    @Override
    protected void initializeAnimation() {
        final Vector2<Integer> offset = new Vector2<>(0, -6);
        final float shotDuration = trap.getState(State.SHOT).getDuration();
        final float reloadDuration = trap.getState(State.RELOAD).getDuration();
        for (Direction direction : Direction.values()) {
            final String dir = direction.toString().toLowerCase();
            animate(State.SHOT, direction, 1, shotDuration, offset, RES + dir);
            animate(State.RELOAD, direction, 1, reloadDuration, offset, RES + dir);
        }
    }

    private Direction getAnimationDirection(EntityState currentState) {
        return switch (currentState) {
            case State.SHOT, State.RELOAD -> trap.getShotDirection();
            default -> null;
        };
    }

    @Override
    public BufferedImage getFrame() {
        final EntityState state = trap.getState();
        return getAnimatedSprite(state, getAnimationDirection(state)).getFrame();
    }

    @Override
    public Vector2<Integer> getOffset() {
        final EntityState state = trap.getState();
        return getAnimatedSprite(state, getAnimationDirection(state)).getOffset();
    }
}